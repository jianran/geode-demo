# install

如果已经安装brew，执行下列命令打包
   
```sh
brew install apache-geode
```

如果没有安装，可以选择源码编译

```sh
git clone https://github.com/apache/geode.git
./gradlew build
```

# intro

distributed cloud architecture：基于分布式云架构的内存存储系统；

# feature

* KV ： put/get
* OQL ： 类sql查询
* 事务管理
* 二级索引及多种索引类型
* 数据变更本地事件监听
* 服务端函数代码注册与触发

# component
