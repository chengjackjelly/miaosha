阿里云ECS分布式集群配置：
 - 一台nginx服务器，放置前端资源和nginx反向代理
 - 两台后端服务器，运行后端jar包
 - 一台数据库和Redis服务器

性能提升：通过Nginx实现并发非阻塞会话

基于Redis存储的Token会话实现。


