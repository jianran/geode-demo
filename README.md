# install

如果已经安装brew，执行下列命令直接方案，如果没有安装brew，执行install_brew.sh安装brew
   
```sh
brew install apache-geode
```
或者选择源码编译安装，可能会编译报错，不建议

```sh
git clone https://github.com/apache/geode.git
./gradlew build -Dskip.test=true
```

# intro

distributed cloud architecture：基于分布式云架构的内存数据存储系统；具有高吞吐低延时，可分区，可伸缩，可容错，高一致，高可用等特点

# feature

* KV ： put/get
* OQL ： 类sql查询
* 强一致性事务保证
* 二级索引及多种索引类型
* 数据变更本地事件监听
* 服务端函数代码注册与触发

# component
![架构图](https://github.com/jianran/geode-demo/blob/master/geode-arch.png?raw=true)
* locator: 
* server:
* group:
* [region](https://geode.apache.org/docs/guide/16/developing/region_options/region_types.html):
* partiion:
* gfsh:
* pulse: admin/admin
* cq
* pdx

# kv
```sh
start locator --name="basicLocator"
start server --name="basicServer"
create region --name=regionA --type=REPLICATE_PERSISTENT
put --region=regionA --key="1" --value="one"
put --region=regionA --key="2" --value="two"
query --query="select * from /regionA"
start server --name=server2 --server-port=40412
describe region --name=regionA
```
# client

```java
ClientCache cache = new ClientCacheFactory().addPoolLocator("localhost", 10334).create();
Region<String, String> region = cache.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY).create("regionA");
```

# spring-data-gemfire

* @ClientCacheApplication
* @Region
* @ContinuousQuery

# others

[geode-example](https://github.com/apache/geode-examples)

* [partitioned](https://github.com/apache/geode-examples/blob/develop/partitioned/README.md)
* [Asynchronous Event Queues](https://github.com/apache/geode-examples/blob/develop/async/README.md)
* [functions](https://github.com/apache/geode-examples/tree/develop/functions)


