# Nacos

> 启动
```
Linux/Unix/Mac：sh startup.sh -m standalone
Windows：cmd startup.cmd -m standalone
```

## Nacos配置

### 配置中心原理

> 更新

客户端是通过一个定时任务（10ms）来检查自己监听的配置项的数据的，一旦服务端的数据发生变化时，客户端将会获取到最新的数据，并将最新的数据保存在一个 CacheData 对象中，然后会重新计算 CacheData 的 md5 属性的值，此时就会对该 CacheData 所绑定的 Listener 触发 receiveConfigInfo 回调。

考虑到服务端故障的问题，客户端将最新数据获取后会保存在本地的 snapshot 文件中，以后会优先从文件中获取配置信息的值。

> 推与拉

如果用推的方式，服务端需要维持与客户端的长连接，这样的话需要耗费大量的资源，并且还需要考虑连接的有效性，例如需要通过心跳来维持两者之间的连接。
而用拉的方式，客户端只需要通过一个无状态的 `http` 请求即可获取到服务端的数据。

### 加载规则

默认值的应用要加载的配置规则就是：Data ID=${spring.application.name}.properties，Group=DEFAULT_GROUP

### 指定profiles

通过`spring.profiles.active`来指定具体的环境名后，此时`Data ID`为：
```
${spring.application.name}-${spring.profiles.active}.properties
```

### 实现多环境配置

#### 使用Data ID与profiles实现

- 在Nacos中新增配置文件Data Id：`alibaba-config-DEV.properties`，`group`默认值。
- 增加环境配置：`spring.profiles.active=DEV`

#### 使用group实现

- 在Nacos中新增配置文件Data Id：`alibaba-config.properties`，`group`：`DEV_GROUP`
- 增加环境配置：`spring.cloud.nacos.config.group=DEV_GROUP`

#### 使用Namespace实现

`spring.cloud.nacos.config.namespace=c8e0b680-e3e9-48f7-bf02-be99ef7b894d`

### 共享配置
```
spring.cloud.nacos.config.shared-dataids=actuator.properties,log.properties
# 默认情况下所有共享配置都不支持动态刷新
spring.cloud.nacos.config.refreshable-dataids=actuator.properties,log.properties
```

### 配置加载优先级

- 通过`spring.cloud.nacos.config.shared-dataids`定义的共享配置（A）
- 通过`spring.cloud.nacos.config.ext-config[n]`定义的加载配置（B）
- 通过内部规则（spring.cloud.nacos.config.prefix、spring.cloud.nacos.config.file-extension、spring.cloud.nacos.config.group这几个参数）拼接出来的配置（C）

优先级关系是：`A < B < C`

# Sentinel

## Nacos存储限流规则

- `Sentinel`控制台中修改规则：仅存在于服务的内存中，不会修改Nacos中的配置值，重启后恢复原来的值。
- `Nacos`控制台中修改规则：服务的内存中规则会更新，Nacos中持久化规则也会更新，重启后依然保持。
