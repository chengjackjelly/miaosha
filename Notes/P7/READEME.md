## 交易性能瓶颈

- 交易验证完全以来数据库
- 库存行锁 (for update)
- 后置处理逻辑

### 交易验证优化

- 在UserService/ItemService里添加getByCache方法，加入Redis缓存。