# 目录结构说明

- controller: 接口
- service: 业务逻辑
- repository: mybatis-plus 的方法和一些简单的查询逻辑
- mapper: Mapper 接口

- entity: 数据库实体类
- dto: 传给前端的对象，有时也用来接收前端传回的参数
- query: 接收前端传来的参数

- common: 包含枚举，工具类，配置类，全局异常处理等
- filter: 用户登录权限校验
- websocket: Websocket 逻辑