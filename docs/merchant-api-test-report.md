# Merchant Service API 测试报告

**测试日期**: 2026-03-03  
**测试人员**: AI Assistant  
**服务端口**: 8002  
**测试环境**: 本地开发环境

---

## 1. 测试概览

### 1.1 测试接口列表
1. POST /merchant/register - 商家注册
2. POST /merchant/login - 商家登录
3. GET /merchant/info - 获取商家信息
4. GET /merchant/store - 获取店铺信息
5. PUT /merchant/store - 更新店铺信息
6. POST /merchant/qualification - 提交资质

### 1.2 测试场景说明
- ✅ 成功场景：正常参数，期望返回成功
- ❌ 失败场景：异常参数，期望返回错误
- ⚠️ 边界场景：边界值测试

---

## 2. 接口测试详情

### 2.1 POST /merchant/register - 商家注册

#### ✅ 测试用例 1.1：正常注册
**请求**:
```json
{
  "username": "test_merchant_001",
  "password": "password123",
  "phone": "13800138001",
  "email": "test001@example.com",
  "storeName": "测试店铺001"
}
```

**响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "username": "test_merchant_001",
        "phone": "13800138001",
        "email": "test001@example.com",
        "storeName": "测试店铺001",
        "status": 0,
        "level": 1,
        "createdAt": null
    }
}
```

**结果**: ✅ 通过 - 商家注册成功，返回商家信息

---

#### ❌ 测试用例 1.2：用户名重复
**请求**:
```json
{
  "username": "test_merchant_001",
  "password": "password123",
  "phone": "13900139001",
  "email": "test002@example.com",
  "storeName": "测试店铺002"
}
```

**响应**:
```json
{
    "code": 400,
    "message": "用户名已存在",
    "data": null
}
```

**结果**: ✅ 通过 - 正确拦截重复用户名

---

#### ❌ 测试用例 1.3：参数验证失败
**请求**:
```json
{
  "username": "ab",
  "password": "123",
  "phone": "123",
  "storeName": ""
}
```

**响应**:
```json
{
    "code": 400,
    "message": "参数校验失败: [用户名长度需在4到32位] [手机号格式不正确] [店铺名称不能为空] [密码长度需在6到20位]",
    "data": null
}
```

**结果**: ✅ 通过 - 正确拦截参数校验错误

---

### 2.2 POST /merchant/login - 商家登录

#### ✅ 测试用例 2.1：正常登录
**请求**:
```json
{
  "username": "test_merchant_001",
  "password": "password123"
}
```

**响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenHead": "Bearer",
        "expiresIn": 86400,
        "merchant": {
            "id": 1,
            "username": "test_merchant_001",
            "phone": "13800138001",
            "email": "test001@example.com",
            "storeName": "测试店铺001",
            "status": 1,
            "level": 1,
            "createdAt": "2026-03-03T08:10:17"
        }
    }
}
```

**结果**: ✅ 通过 - 登录成功，返回JWT Token和商家信息

**注意**: 需要先将商家状态更新为 1（已审核），否则会提示"商家账号待审核或已禁用"

---

#### ❌ 测试用例 2.2：密码错误
**请求**:
```json
{
  "username": "test_merchant_001",
  "password": "wrongpassword"
}
```

**响应**:
```json
{
    "code": 400,
    "message": "用户名或密码错误",
    "data": null
}
```

**结果**: ✅ 通过 - 正确拦截错误密码

---

#### ❌ 测试用例 2.3：用户名不存在
**请求**:
```json
{
  "username": "nonexistent_user",
  "password": "password123"
}
```

**响应**:
```json
{
    "code": 400,
    "message": "商家不存在",
    "data": null
}
```

**结果**: ✅ 通过 - 正确拦截不存在的用户名

---

### 2.3 GET /merchant/info - 获取商家信息

#### ✅ 测试用例 3.1：正常获取信息（需要Token）
**请求头**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "username": "test_merchant_001",
        "phone": "13800138001",
        "email": "test001@example.com",
        "storeName": "测试店铺001",
        "status": 1,
        "level": 1,
        "createdAt": "2026-03-03T08:10:17"
    }
}
```

**结果**: ✅ 通过 - 成功获取商家信息

---

#### ❌ 测试用例 3.2：无Token访问
**请求**: 无Authorization头

**响应**:
```json
{
    "code": 500,
    "message": "系统异常: Required request header 'Authorization' for method parameter type String is not present",
    "data": null
}
```

**结果**: ⚠️ 部分通过 - 拦截了无Token请求，但应返回401而非500

**问题**: 应该返回 401 Unauthorized，而不是 500 系统异常

---

#### ❌ 测试用例 3.3：无效Token
**请求头**:
```
Authorization: Bearer invalid_token
```

**响应**:
```json
{
    "code": 500,
    "message": "系统异常: JWT strings must contain exactly 2 period characters. Found: 0",
    "data": null
}
```

**结果**: ⚠️ 部分通过 - 拦截了无效Token，但应返回401而非500

**问题**: 应该返回 401 Unauthorized，而不是 500 系统异常

---

### 2.4 GET /merchant/store - 获取店铺信息

#### ✅ 测试用例 4.1：正常获取店铺信息
**请求头**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "merchantId": 1,
        "name": "测试店铺001",
        "logo": null,
        "description": null,
        "province": null,
        "city": null,
        "address": null,
        "businessLicense": null,
        "status": 0
    }
}
```

**结果**: ✅ 通过 - 成功获取店铺信息

---

### 2.5 PUT /merchant/store - 更新店铺信息

#### ✅ 测试用例 5.1：正常更新店铺信息
**请求头**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
Content-Type: application/json
```

**请求体**:
```json
{
  "name": "测试店铺001-已更新",
  "logo": "https://example.com/logo.png",
  "description": "这是一个测试店铺的描述",
  "province": "广东省",
  "city": "深圳市",
  "address": "南山区科技园",
  "businessLicense": "LICENSE123456"
}
```

**响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "merchantId": 1,
        "name": "测试店铺001-已更新",
        "logo": "https://example.com/logo.png",
        "description": "这是一个测试店铺的描述",
        "province": "广东省",
        "city": "深圳市",
        "address": "南山区科技园",
        "businessLicense": "LICENSE123456",
        "status": 0
    }
}
```

**结果**: ✅ 通过 - 成功更新店铺信息

---

#### ❌ 测试用例 5.2：店铺名称为空
**请求体**:
```json
{
  "name": ""
}
```

**响应**:
```json
{
    "code": 400,
    "message": "参数校验失败: [店铺名称不能为空]",
    "data": null
}
```

**结果**: ✅ 通过 - 正确拦截空店铺名称

---

### 2.6 POST /merchant/qualification - 提交资质

#### ✅ 测试用例 6.1：正常提交资质
**请求头**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
Content-Type: application/json
```

**请求体**:
```json
{
  "type": "营业执照",
  "imageUrl": "https://example.com/license.jpg"
}
```

**响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": null
}
```

**结果**: ✅ 通过 - 资质提交成功

---

#### ❌ 测试用例 6.2：资质类型为空
**请求体**:
```json
{
  "type": "",
  "imageUrl": "https://example.com/license.jpg"
}
```

**响应**:
```json
{
    "code": 400,
    "message": "参数校验失败: [资质类型不能为空]",
    "data": null
}
```

**结果**: ✅ 通过 - 正确拦截空资质类型

**数据库验证**: 资质信息已成功保存到 merchant_qualification 表
```
id: 1
merchant_id: 1
type: 营业执照
image_url: https://example.com/license.jpg
status: 0
created_at: 2026-03-03 08:13:19
```

---

## 3. 测试总结

### 3.1 测试统计

| 接口 | 测试用例数 | 通过数 | 失败数 | 通过率 |
|------|-----------|--------|--------|--------|
| POST /merchant/register | 3 | 3 | 0 | 100% |
| POST /merchant/login | 3 | 3 | 0 | 100% |
| GET /merchant/info | 3 | 1 | 2 | 33% |
| GET /merchant/store | 1 | 1 | 0 | 100% |
| PUT /merchant/store | 2 | 2 | 0 | 100% |
| POST /merchant/qualification | 2 | 2 | 0 | 100% |
| **总计** | **14** | **12** | **2** | **86%** |

### 3.2 发现的问题

#### 🔴 严重问题（需立即修复）

**问题 1：无Token访问返回500错误**
- **接口**: GET /merchant/info
- **测试用例**: 3.2
- **预期**: 返回 401 Unauthorized
- **实际**: 返回 500 系统异常
- **错误信息**: "Required request header 'Authorization' for method parameter type String is not present"
- **影响**: 错误码不规范，前端难以统一处理认证失败
- **建议**: 在 parseMerchantId 方法中捕获 MissingRequestHeaderException，返回 401

**问题 2：无效Token返回500错误**
- **接口**: GET /merchant/info
- **测试用例**: 3.3
- **预期**: 返回 401 Unauthorized
- **实际**: 返回 500 系统异常
- **错误信息**: "JWT strings must contain exactly 2 period characters. Found: 0"
- **影响**: 错误码不规范，暴露了JWT实现细节
- **建议**: 在 JwtUtil.validateToken 中捕获 JWT 解析异常，返回友好的错误信息

#### ⚠️ 中等问题（建议优化）

**问题 3：商家注册后默认状态为0（待审核）**
- **影响**: 新注册商家无法立即登录
- **建议**: 
  - 如果是业务需求，应该在注册时明确提示用户
  - 如果不是，考虑默认状态为1（已激活）

**问题 4：错误信息中包含技术细节**
- **接口**: 多个接口
- **示例**: JWT解析错误、Spring验证框架错误信息
- **建议**: 对用户返回友好的错误信息，技术细节记录到日志

### 3.3 优点

1. ✅ 参数验证完整且准确
2. ✅ JWT Token 认证机制正常工作
3. ✅ 数据持久化正常
4. ✅ 业务逻辑正确（用户名唯一性、密码验证等）
5. ✅ 接口响应格式统一
6. ✅ 所有必填字段验证到位

### 3.4 建议

#### 短期（本周完成）
1. 修复认证失败时的错误码问题（返回401而非500）
2. 优化错误信息，避免暴露技术细节
3. 统一异常处理机制

#### 中期（下周完成）
1. 增加接口日志记录
2. 添加接口限流机制
3. 完善单元测试覆盖

#### 长期（未来迭代）
1. 考虑增加商家审核流程API
2. 增加商家资质审核状态查询接口
3. 完善API文档（Swagger）

---

## 4. 测试数据

### 4.1 测试账号
```
用户名: test_merchant_001
密码: password123
手机: 13800138001
邮箱: test001@example.com
店铺: 测试店铺001
状态: 1 (已审核)
```

### 4.2 测试Token
```
Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcmluY2lwYWwiOiJ0ZXN0X21lcmNoYW50XzAwMSIsInN1YiI6IjEiLCJleHAiOjE3NzI1ODMwNTksInVzZXJJZCI6MSwiaWF0IjoxNzcyNDk2NjU5fQ.5pMcpmQFMc-1XNgFc9c0mi8WXt0527KIAq0PxxvZnLn9U0JfYBfntZbaC3jzvJrBouPNx7hwV0qcbYWwwQrDUQ
```

---

## 5. 附录

### 5.1 数据库表结构

#### merchant 表
```sql
- id: 主键
- username: 用户名（唯一）
- password: 密码（加密）
- phone: 手机号
- email: 邮箱
- store_name: 店铺名称
- status: 状态（0-待审核，1-已审核，2-禁用）
- level: 等级
- created_at: 创建时间
```

#### merchant_store 表
```sql
- id: 主键
- merchant_id: 商家ID
- name: 店铺名称
- logo: 店铺Logo
- description: 店铺描述
- province: 省份
- city: 城市
- address: 详细地址
- business_license: 营业执照
- status: 状态
```

#### merchant_qualification 表
```sql
- id: 主键
- merchant_id: 商家ID
- type: 资质类型
- image_url: 图片URL
- status: 审核状态
- audit_remark: 审核备注
- created_at: 创建时间
```

---

**测试完成时间**: 2026-03-03 08:15  
**测试人员**: AI Assistant  
**测试环境**: macOS, Java 8, MySQL 8.0, Spring Boot 2.7.18

