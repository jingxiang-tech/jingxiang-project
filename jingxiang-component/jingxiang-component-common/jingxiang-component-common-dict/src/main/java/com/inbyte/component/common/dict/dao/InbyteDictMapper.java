package com.inbyte.component.common.dict.dao;

import com.inbyte.commons.model.dto.Dict;
import com.inbyte.component.common.dict.model.DictItemBrief;
import com.inbyte.component.common.dict.model.DictItemTreeBrief;

import java.util.List;

/**
 * 字典
 *
 * 表名：  dict
 * @author chenjw
 * @date 2023-12-17 22:12:01
 */
public interface InbyteDictMapper {

    /**
     * 查询列表
     * @param dictName
     * @return List<DictBrief>
     **/
    List<Dict> dictList(String dictName);

    /**
     * 查询列表
     * @param dictName
     * @return List<DictBrief>
     **/
    List<DictItemTreeBrief> list(String dictName);

    List<DictItemBrief> getIconDict(String dictName);
}
