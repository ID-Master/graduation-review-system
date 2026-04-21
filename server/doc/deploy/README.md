## 部署脚本说明

## docker-standalone ---mac系统本地化脚本
已在mac系统上进行测试验证，支持docker-compose一键部署
- mysql 5.7
- redis 5.0

### 操作命令

- 启动
```bash
cd doc/deploy/docker-standalone

docker-compose up -d
```
- 其他
```bash
# 关闭
docker-compose stop
# 卸载
docer-compose down
# 启动
docker-compose start
```

## mysql ---docker安装5.7的脚本示例

## redis ---docker安装redis的脚本示例
