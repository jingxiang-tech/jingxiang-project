# jingxiang-project

公共库与 Spring Boot Starter 多模块项目。

## 构建环境

- JDK 25（编译、测试和使用这些组件的应用运行环境均需 Java 25 或以上）。
- Maven 3.6.3 或以上，推荐 Maven 3.9.x。
- 将 `JAVA_HOME` 指向 JDK 25，确认 `mvn -version` 中的 Java version 为 25。
- IntelliJ IDEA 中将 Project SDK、Maven Importer JDK 和 Maven Runner JRE 设置为 JDK 25，然后重新加载 Maven 项目。

在项目根目录执行：

```sh
mvn clean verify
```

## Java 25 兼容配置

父 POM 统一使用 `release=25`，所有子模块继承。`.java-version` 为支持该文件的 JDK 管理器提供版本提示，不会自动修改系统的 `JAVA_HOME`。

- Spring Boot 4.1.1 及其 BOM 管理的 Lombok 1.18.46 已支持 Java 25，继续由 BOM 统一管理依赖版本。
- Maven Compiler Plugin 使用 3.15.0；显式配置 Lombok 注解处理器及 `proc=full`，适配 JDK 23 起的注解处理规则。
- Maven Surefire Plugin 使用 3.5.6，与 Spring Boot 4.1.1 的插件版本保持一致，支持其 JUnit Platform 测试依赖。项目仅导入 Boot BOM，不会继承其中的插件管理，故在父 POM 中显式声明插件版本。

兼容性参考：[Spring Boot 系统要求](https://docs.spring.io/spring-boot/system-requirements.html)、[Lombok 更新记录](https://projectlombok.org/changelog)。
