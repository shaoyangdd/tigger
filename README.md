## 真正意义上的去中心化的调度系统

### 1. 去中心化

#### &emsp;&emsp;借鉴区块链接去中心化的设计思想，没有任何中心节点，不依赖任何中间件，每个实例都是独立的，类似独立独行的老虎，故命名为Tiger。

#### &emsp;&emsp;有一些号称是去中心化的框架，像elastic job,Apache DolphinScheduler等但是大多用到了zk，mq等中间件，做不到完全去中心化。

#### &emsp;&emsp;简单易用，只需要依赖SDK，将jar包丢到服务器上运行即可，做到傻瓜式部署。

### 2. 动态扩展

#### &emsp;&emsp;由于每个节点完全独立，所以解决大规模的数据问题时只需要加机器，做横向扩展。

### 3. 智能调度

#### &emsp;&emsp;根据机器的资源使用率，功能的资源使用率，动态调配资源。机器学习，智能调度（调整执行机上执行的任务、线程数，分片参数等等）

### 4. 轻量级，无入侵式设计

#### &emsp;&emsp;整个SDK只依赖了FastJSON、netty、quartz、protobuf没有其它依赖，代码轻量级。代码开源，如提供的功能不满足你的需求可以下载下来自己修改。

### 5. 如何使用

#### &emsp;&emsp;1） 依赖本SDK，配置数据库模式（无数据库模式或数据库模式，有数据库时需要配置数据库信息）、配置任务执行器

#### &emsp;&emsp;2） 调用Starter.run()方法异步启动

#### &emsp;&emsp;3） 打包后丢到有jdk的环境去运行(两种方式运行，自动和手动，自动需要去控制台配置cron表达式，手动需要去控制台)

### 6. 深入了解

#### 请阅读doc/设计思路.txt   
