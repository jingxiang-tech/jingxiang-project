# 通用 - 字典组件
## 字典类规范

### 枚举字典
- 类名【Enum】结尾
- 包含 public final String name 字段，注意属性要 public final 声明
- 枚举对象使用大写下划线命名方式
```
public enum CurrencyEnum {

    USD("美元"),
    RMB("人民币"),
    HKD("港币"),
    GBP("英镑"),
    CAD("加拿大元"),
    AUD("澳大利亚元"),
    JPY("日元"),
    KRW("韩元"),
    EUR("欧元");

    public final String name;

    CurrencyEnum(String name) {
        this.name = name;
    }

}

```

### Dict 字典
- 类名【dict】结尾
- 包含以下两个字段，以及全参构造
  - public final String name
  - public final int code
- 枚举对象使用大写下划线命名方式
```
public enum WhetherDict {

    No(0, "否"),
    Yes(1, "是");

    public final int code;
    public final String name;

    WhetherDict(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
```