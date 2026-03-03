# JWT配置指南

## 📅 更新时间：2026-03-03 22:24

---

## ⚠️ 重要安全说明

**JWT密钥必须正确配置！**
- ✅ 密钥长度：>=32字符（256位）
- ✅ 使用强随机密钥
- ❌ 禁止使用默认密钥
- ❌ 禁止使用示例密钥

---

## 🔧 配置方法

### 方法1：application.yml（开发环境）

```yaml
jwt:
  secret: your-very-secure-jwt-secret-key-must-be-at-least-32-characters-long
  expiration: 86400000 # 24小时（单位：毫秒）
```

### 方法2：环境变量（生产环境推荐）

```bash
# Linux/Mac
export JWT_SECRET=your-production-jwt-secret-key-at-least-32-characters

# Windows
set JWT_SECRET=your-production-jwt-secret-key-at-least-32-characters
```

application.yml配置：
```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000
```

### 方法3：KMS/Secrets Manager（最佳实践）

```yaml
jwt:
  secret: ${AWS_SECRETS_MANAGER_JWT_SECRET}
  expiration: 86400000
```

---

## 🔐 生成强密钥

### Linux/Mac
```bash
# 方法1：OpenSSL
openssl rand -base64 32

# 方法2：/dev/urandom
head -c 32 /dev/urandom | base64

# 方法3：Python
python3 -c "import secrets; print(secrets.token_urlsafe(32))"
```

### Windows
```powershell
# PowerShell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

---

## ✅ 配置验证

启动应用时，系统会自动验证JWT配置：

**成功示例：**
```
Application started successfully!
JWT配置验证通过
```

**失败示例：**
```
ERROR: JWT密钥必须配置！请在application.yml中设置jwt.secret
ERROR: JWT密钥长度必须>=32字符（256位），当前长度：20
ERROR: 禁止使用默认或示例密钥！请使用强密钥
```

---

## 🔄 密钥轮换

**建议：** 每3-6个月轮换一次JWT密钥

**步骤：**
1. 生成新密钥
2. 在KMS/Secrets Manager中更新
3. 重启应用（会导致所有旧token失效）
4. 通知用户重新登录

---

## 📝 安全检查清单

- [ ] 密钥长度>=32字符
- [ ] 使用随机生成的密钥
- [ ] 生产环境使用环境变量或KMS
- [ ] 密钥不在代码库中提交
- [ ] 定期轮换密钥
- [ ] 密钥访问权限最小化

---

*配置指南创建时间: 2026-03-03 22:24*
