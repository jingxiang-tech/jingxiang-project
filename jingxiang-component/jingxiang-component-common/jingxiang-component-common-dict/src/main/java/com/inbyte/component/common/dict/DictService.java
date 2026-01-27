package com.inbyte.component.common.dict;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.inbyte.commons.model.dto.Dict;
import com.inbyte.component.common.dict.dao.InbyteDictMapper;
import com.inbyte.component.common.dict.model.DictItemBrief;
import com.inbyte.component.common.dict.model.DictItemTreeBrief;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 字典服务
 *
 * @author chenjw
 * @date 2023-12-17 22:12:01
 **/
@Slf4j
@Service
public class DictService implements InitializingBean {

    private static final String SUFFIX = "dict|enum";
    private static final String DICT_SUFFIX = "dict";
    private static final String ENUM_SUFFIX = "enum";

    @Autowired
    private ComponentDictProperties componentDictProperties;

    @Override
    public void afterPropertiesSet() {
        init("com.inbyte.commons.model.dict", "com.inbyte.commons.model.enums", "com.inbyte.component");
//        init("com.inbyte");
        long startTime = System.currentTimeMillis();
        if (componentDictProperties.getPath() != null) {
            componentDictProperties.getPath().forEach(e -> init(e));
        }
        log.info("字典加载完成, 耗时:{}ms", System.currentTimeMillis() - startTime);
    }

    @Autowired
    private InbyteDictMapper inbyteDictMapper;// 创建缓存实例

    /**
     * 静态字典
     */
    private static final Map<String, Map<String, String>> SYSTEM_DICT_CACHE = new ConcurrentHashMap<>();

    /**
     * 业务动态字典
     * 字典缓存
     * 5分钟过期
     */
    private Cache<String, Map<String, String>> BUSINESS_DICT_CACHE = CacheBuilder.newBuilder()
            .maximumSize(100) // 设置缓存的最大大小
            .expireAfterWrite(5, TimeUnit.MINUTES) // 设置缓存项写入后过期时间
            .build();

//    /**
//     * 业务动态字典
//     * 字典缓存
//     * 10分钟过期
//     */
//    private Cache<String, Map<String, String>> TREE_DICT_CACHE = CacheBuilder.newBuilder()
//            .maximumSize(100) // 设置缓存的最大大小
//            .expireAfterWrite(10, TimeUnit.MINUTES) // 设置缓存项写入后过期时间
//            .build();

    private static final String CODE = "code";
    private static final String NAME = "name";

    /**
     * 字典加载入口
     *
     * @param dictPath 字典目录 例：com.maidao.dict
     */
    public void init(String... dictPath) {
        // 加载路径配置字典，计划将来废弃
        for (String path : dictPath) {
            Set<Class<? extends Enum<?>>> classes = DictScanner.scanDictBean(path);
            load(classes);
        }

    }

    private void load(Set<Class<? extends Enum<?>>> classes) {
        for (Class clz : classes) {
            boolean anEnum = clz.isEnum();
            if (anEnum) {
                Enum[] enums = (Enum[]) clz.getEnumConstants();
                load(enums);
            }
        }
    }
    public void load(Enum[] enums) {
        String dictName = enums[0].getClass().getSimpleName().toLowerCase();
        if (dictName.endsWith(DICT_SUFFIX)) {
            loadDict(enums, dictName);
        } else if (dictName.endsWith(ENUM_SUFFIX)) {
            loadEnum(enums, dictName);
        }
    }

    /**
     * 加载DICT字典
     *
     * @param enums
     * @param dictName
     */
    private void loadDict(Enum[] enums, String dictName) {
        try {
            Map<String, String> dict = new LinkedHashMap<>();
            for (int i = 0; i < enums.length; i++) {
                dict.put(String.valueOf(enums[i].getClass().getField(CODE).get(enums[i])),
                        String.valueOf(enums[i].getClass().getField(NAME).get(enums[i])));
            }
            SYSTEM_DICT_CACHE.put(dictName.replace(DICT_SUFFIX, ""), dict);
        } catch (Exception e) {
            log.warn("枚举字典{}命名不规范, 请增加字段code和name, 否则无法加入字典池, 详情请参考:https://github.com/inbytecc/inbyte-component/blob/dev/inbyte-component-common/inbyte-component-common-dict/README.md", dictName);
        }
    }

    /**
     * 加载ENUM字典
     *
     * @param enums
     * @param dictName
     */
    private void loadEnum(Enum[] enums, String dictName) {
        try {
            Map<String, String> dict = new LinkedHashMap<>();
            for (int i = 0; i < enums.length; i++) {
                dict.put(enums[i].name().toUpperCase(), // KEY
                        String.valueOf(enums[i].getClass().getField(NAME).get(enums[i]))); // VALUE
            }
            SYSTEM_DICT_CACHE.put(dictName.toLowerCase().replace(ENUM_SUFFIX, ""), dict);
        } catch (Exception e) {
            log.warn("枚举字典{}命名不规范, 请增加字段【name】, 否则无法加入字典池, 详情请参考:https://github.com/inbytecc/inbyte-component/blob/dev/inbyte-component-common/inbyte-component-common-dict/README.md", dictName);
        }
    }


    /**
     * 获取字典信息
     *
     * @param dictName
     * @return
     */
    public Map<String, String> getDict(String dictName) {
        dictName = dictName.toLowerCase().replaceAll(SUFFIX, "");
        Map<String, String> dict = SYSTEM_DICT_CACHE.get(dictName);
        if (dict != null) {
            return dict;
        }

        dict = BUSINESS_DICT_CACHE.getIfPresent(dictName);
        if (dict != null && dict.size() > 0) {
            return dict;
        }
        try {
            List<DictItemTreeBrief> list = inbyteDictMapper.list(dictName);
            dict = list.stream()
                    .sorted(Comparator.comparingInt(DictItemTreeBrief::getOrdinal)) // 根据ordinal升序排序
                    .collect(Collectors.toMap(
                            k -> k.getCode().toString(),   // 获取键的方法
                            v -> v.getName(),   // 获取值的方法
                            (existing, replacement) -> existing, // 如果键冲突时保留原有的值
                            LinkedHashMap::new // 指定Map的实现为LinkedHashMap
                    ));

            BUSINESS_DICT_CACHE.put(dictName, dict);
        } catch (Exception e) {
            log.warn("字典表未同步, 无法获取数据库字典信息");
        }
        return dict;
    }

    /**
     * 获取字典
     *
     * @param dictClass
     * @return
     */
    public Map<String, String> getDict(Class dictClass) {
        return getDict(dictClass.getSimpleName().toLowerCase().replace("dict", ""));
    }

    /**
     * 获取字典名称
     *
     * @param dictClass
     * @param code
     * @return
     */
    public String getName(Class dictClass, Integer code) {
        return getDict(dictClass).get(code);
    }

    /**
     * 获取字典名称
     *
     * @param dictName
     * @param code
     * @return
     */
    public String getName(String dictName, Integer code) {
        return getDict(dictName).get(code);
    }

    /**
     * 获取字典名称
     *
     * @param dictClass
     * @param codes
     * @return
     */
    public String getNames(Class dictClass, List<Integer> codes) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> dict = getDict(dictClass);
        for (int i = 0; i < codes.size(); i++) {
            sb.append(dict.get(codes.get(i))).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 获取字典名称
     *
     * @param dictName
     * @param codes
     * @return
     */
    public String getNames(String dictName, List<Integer> codes) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> dict = getDict(dictName);
        for (int i = 0; i < codes.size(); i++) {
            sb.append(dict.get(codes.get(i))).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public List<DictItemTreeBrief> getDictTree(String dictName) {
        List<DictItemTreeBrief> list = inbyteDictMapper.list(dictName);
        Map<Integer, List<DictItemTreeBrief>> map = list.stream().collect(Collectors.groupingBy(DictItemTreeBrief::getParentId));
        list.forEach(node -> node.setChildren(map.getOrDefault(node.getItemId(), new ArrayList<>())));
        return map.get(0);
    }

    /**
     * 获取字典列表
     *
     * @param clz
     * @return
     */
    public List<Dict> getListDict(Class clz) {
        List<Dict> list = new ArrayList<>();
        Enum[] enums = (Enum[]) clz.getEnumConstants();
        try {
            for (int i = 0; i < enums.length; i++) {
                Field code = enums[i].getClass().getField(CODE);
                Field name = enums[i].getClass().getField(NAME);
                list.add(new Dict(String.valueOf(code.get(enums[i])),
                        String.valueOf(name.get(enums[i]))));
            }
        } catch (Exception e) {
            log.error("字典{}命名不规范, 请增加字段code和name, 否则无法加入字典池", clz.getName());
        }
        return list;
    }

    public List<DictItemBrief> getIconDict(String dictName) {
        return inbyteDictMapper.getIconDict(dictName);
    }
}
