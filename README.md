## 基于Socket的Java版聊天室

#### 版本1.0

------------2019/7/18------------

初步实现如下功能

​	1.注册

​	2.群聊

​	3.私聊

​	4.退出

##### 1.1

------------2019/7/19------------

优化如下

​	1.减少重复代码

​	2.动态配置服务端端口号与线程池（参数配置）

​	3.动态配置客户端IP地址与端口号（参数配置）

待扩展点

   代码层优化

​	1.参数校验：IP地址合法性

​	2.客户端异常关闭，服务端的处理

   功能扩展

​	1.注册时提供名称和密码，储存

​	2.登录功能

​	3.用户登录时，可发送历史消息