# 部署指南

## 📅 更新时间：2026-03-03 22:48

---

## 🎯 环境要求

### 基础环境
- **Java：** JDK 1.8+
- **Maven：** 3.6+
- **MySQL：** 8.0+
- **Redis：** 6.0+

### 硬件要求（最小）
- **CPU：** 2核
- **内存：** 4GB
- **硬盘：** 20GB

### 硬件要求（推荐）
- **CPU：** 4核
- **内存：** 8GB
- **硬盘：** 50GB

---

## 📦 部署步骤

### 1. 克隆代码

```bash
git clone https://github.com/imgolye/ecommerce-platform.git
cd ecommerce-platform
```

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE b2b2c_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行SQL脚本
USE b2b2c_platform;
SOURCE sql/schema.sql;

# 添加约束（可选）
SOURCE sql/add_constraints.sql;
```

### 3. 配置修改

#### 3.1 数据库配置

编辑各服务的 `application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/b2b2c_platform?useUnicode=true&characterEncoding=utf8mb4
    username: root
    password: your_password
```

#### 3.2 Redis配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: # 如果有密码
```

#### 3.3 JWT密钥配置

```yaml
jwt:
  secret: your-very-secure-jwt-secret-key-must-be-at-least-32-characters-long
  expiration: 86400000 # 24小时
```

⚠️ **重要：** JWT密钥必须>=32字符，禁止使用默认密钥！

### 4. 编译项目

```bash
# 编译所有模块
mvn clean package -DskipTests

# 或者分别编译
cd user-service && mvn clean package
cd merchant-service && mvn clean package
cd goods-service && mvn clean package
cd order-service && mvn clean package
```

### 5. 启动服务

#### 方式1：直接启动JAR

```bash
# 用户服务（8001）
java -jar user-service/target/user-service-1.0.0-SNAPSHOT.jar --server.port=8001 &

# 商家服务（8002）
java -jar merchant-service/target/merchant-service-1.0.0-SNAPSHOT.jar --server.port=8002 &

# 商品服务（8003）
java -jar goods-service/target/goods-service-1.0.0-SNAPSHOT.jar --server.port=8003 &

# 订单服务（8007）
java -jar order-service/target/order-service-1.0.0-SNAPSHOT.jar --server.port=8007 &
```

#### 方式2：使用脚本启动

创建启动脚本 `start-all.sh`：

```bash
#!/bin/bash

echo "启动用户服务..."
nohup java -Xms256m -Xmx512m -jar user-service/target/user-service-1.0.0-SNAPSHOT.jar --server.port=8001 > logs/user-service.log 2>&1 &

echo "启动商家服务..."
nohup java -Xms256m -Xmx512m -jar merchant-service/target/merchant-service-1.0.0-SNAPSHOT.jar --server.port=8002 > logs/merchant-service.log 2>&1 &

echo "启动商品服务..."
nohup java -Xms256m -Xmx512m -jar goods-service/target/goods-service-1.0.0-SNAPSHOT.jar --server.port=8003 > logs/goods-service.log 2>&1 &

echo "启动订单服务..."
nohup java -Xms256m -Xmx512m -jar order-service/target/order-service-1.0.0-SNAPSHOT.jar --server.port=8007 > logs/order-service.log 2>&1 &

echo "所有服务启动完成！"
```

### 6. 验证服务

```bash
# 检查服务状态
curl http://localhost:8001/actuator/health
curl http://localhost:8002/actuator/health
curl http://localhost:8003/actuator/health
curl http://localhost:8007/actuator/health
```

---

## 🔧 生产环境部署

### 1. Nginx反向代理

```nginx
upstream user-service {
    server 127.0.0.1:8001;
}

upstream merchant-service {
    server 127.0.0.1:8002;
}

upstream goods-service {
    server 127.0.0.1:8003;
}

upstream order-service {
    server 127.0.0.1:8007;
}

server {
    listen 80;
    server_name api.example.com;

    location /api/user {
        proxy_pass http://user-service;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /api/merchant {
        proxy_pass http://merchant-service;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /goods {
        proxy_pass http://goods-service;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /order {
        proxy_pass http://order-service;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 2. Systemd服务

创建 `/etc/systemd/system/user-service.service`：

```ini
[Unit]
Description=User Service
After=network.target

[Service]
Type=simple
User=app
ExecStart=/usr/bin/java -jar /opt/ecommerce-platform/user-service.jar --server.port=8001
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务：

```bash
sudo systemctl daemon-reload
sudo systemctl enable user-service
sudo systemctl start user-service
sudo systemctl status user-service
```

### 3. Docker部署

创建 `Dockerfile`：

```dockerfile
FROM openjdk:8-jre-alpine
WORKDIR /app
COPY target/user-service-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "app.jar"]
```

构建和运行：

```bash
docker build -t user-service:1.0 .
docker run -d -p 8001:8001 --name user-service user-service:1.0
```

---

## 🔍 监控和日志

### 1. 日志配置

编辑 `application.yml`：

```yaml
logging:
  level:
    root: INFO
    com.b2b2c: DEBUG
  file:
    name: logs/user-service.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### 2. 健康检查

```bash
# 检查所有服务
curl http://localhost:8001/actuator/health
curl http://localhost:8002/actuator/health
curl http://localhost:8003/actuator/health
curl http://localhost:8007/actuator/health
```

### 3. 监控指标

访问：http://localhost:8001/actuator/metrics

---

## ⚠️ 安全建议

### 1. 数据库安全
- 使用强密码
- 限制访问IP
- 定期备份
- 使用SSL连接

### 2. JWT安全
- 密钥>=32字符
- 定期轮换密钥
- 使用环境变量
- 托管到KMS

### 3. 网络安全
- 使用HTTPS
- 配置防火墙
- 限制端口访问
- 使用VPN

### 4. 应用安全
- 启用参数校验
- SQL注入防护
- XSS防护
- CSRF防护

---

## 📞 故障排查

### 1. 服务无法启动

**检查：**
- 端口是否被占用
- 数据库连接是否正常
- 配置文件是否正确

**解决：**
```bash
# 检查端口
netstat -tlnp | grep 8001

# 检查日志
tail -f logs/user-service.log
```

### 2. 数据库连接失败

**检查：**
- MySQL是否运行
- 用户名密码是否正确
- 数据库是否存在

**解决：**
```bash
# 检查MySQL
systemctl status mysql

# 测试连接
mysql -u root -p -h localhost b2b2c_platform
```

### 3. API调用失败

**检查：**
- 服务是否运行
- URL是否正确
- 参数是否正确

**解决：**
```bash
# 检查服务
curl http://localhost:8001/actuator/health

# 查看日志
tail -f logs/user-service.log
```

---

## 📚 相关文档

- [JWT配置指南](JWT-CONFIG-GUIDE.md)
- [数据库约束说明](DATABASE-CONSTRAINTS.md)
- [API快速参考](API-QUICK-REFERENCE.md)
- [演示指南](DEMO-GUIDE.md)

---

*部署指南创建时间: 2026-03-03 22:48*
