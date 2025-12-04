# 目录说明

这个目录下用于解析 Navicat 导出的表结构文件，并按照指定顺序 (order.json) 对表进行排序

- admin_template.sql:   Navicat 导出的表结构文件（去除注释头中的数据库地址等信息）
- initial_data.sql:     初始数据
- order.json:           表排序
- process-sql.js:       SQL 处理 node 脚本

- initial_data_develop.sql: 数据库调试用数据。仅用于开发环境，请勿在生产环境中使用此文件
