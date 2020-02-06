### 需求：
求每个订单中成交金额最大的N个商品交易。

### 实现：
向MapReduce传入用户自定义参数

1、在conf对象中设置参数
```java
Configuration conf = new Configuration();
// 通过代码设置参数，传递给MapReduce程序内部使用
conf.set("topn","2");
```

2、在classpath下新建配置文件设置参数
```xml
<configuration>
    <property>
        <name>top</name>
        <value>3</value>
    </property>
</configuration>
```

```java
Configuration conf = new Configuration();
// 指定加载classpath下的用户自定义配置文件
conf.addResouce("config.xml");
conf.get("top");
```



