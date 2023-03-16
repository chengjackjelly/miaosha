## 多级缓存
- redis缓存
- 热点内存本地缓存
- nginx proxy cache缓存
- nginx lua 缓存

### redis缓存
- 单机版：集中式，问题：如果服务器宕机了整个系统将无法运作。
- 哨兵模式：服务器和redis sentinal请求，redis sentinal通关心跳机制和redis保持联系。问题：容量问题，同一时间只有一台redis工作。
- 集群cluster：多台redis形成网状联系
- 三者的性能瓶颈仅存在于水平扩展后的容量问题。