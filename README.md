# install

如果已经安装brew，执行下列命令打包
   
```sh
brew install apache-geode
```

如果没有安装，可以选择源码编译

```sh
git clone https://github.com/apache/geode.git
./gradlew build -Dskip.test=true
```

# intro

distributed cloud architecture：基于分布式云架构的内存数据存储系统；具有高吞吐低延时，可分区，可伸缩，可容错，高一致，高可用等特点

# feature

* KV ： put/get
* OQL ： 类sql查询
* 事务管理
* 二级索引及多种索引类型
* 数据变更本地事件监听
* 服务端函数代码注册与触发

# component
![架构图](https://github.com/jianran/geode-demo/blob/master/geode-arch.png?raw=true)
* locator:
* server:
* pulse: admin/admin
* group:
* region:
* partiion:
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
```

# others

[geode-example](https://github.com/apache/geode-examples)


