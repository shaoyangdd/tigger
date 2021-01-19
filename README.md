## 真正意义上的去中心化的调度系统

### 1. 去中心化
####  没有任何中心节点，中间件，每个实例都是独立的。有一些号称是去中心化的框架，像elastic job,Apache DolphinScheduler等但是大多用到了zk，mq等中间件，做不到完全去中心化。
####  本产品的使用简单易用，只需要依赖SDK，调用启动方法然后丢到服务器上运行即可，做到傻瓜式部署。
### 2. 动态扩展
####  由于每个节点完全独立，所以解决大规模的数据问题时只需要加机器，做横向扩展，说白了就是丢jar包上去。
### 3. 智能调度
####  根据机器的资源使用率，功能的资源使用率，动态调配资源。
### 4. 轻量级，无入侵式设计
####  整个SDK只依赖了FastJSON和netty，没有其它依赖，代码轻量级
### 5. 代码易上手，功能易扩展
####  古有白居易写诗给老人听，老人听懂的才是好诗。今有本人写通俗代码，新手也能快速上手，没有那些装逼的设计模式，动辄几十层的方法调用，新手也能快速找到重点。不创造新概念，使用人们已经具有的概念，减少理解成本
### 6. 如何使用
####  依赖SDK,配置数据库连接信息，调用Starter的run()方法
### 7. 深入了解
####  请阅读doc/设计思路.txt   