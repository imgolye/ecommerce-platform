# DTO重构计划

## 📅 创建时间：2026-03-03 22:31

---

## 🎯 重构目标

**问题：** Controller直接接收Entity，存在字段注入风险  
**解决方案：** 使用DTO入参，白名单映射

---

## 📋 重构清单

### 1. 创建DTO类

**User模块：**
- UserRegisterDTO.java（注册请求）
- UserLoginDTO.java（登录请求）
- UserUpdateDTO.java（更新请求）

**Merchant模块：**
- MerchantRegisterDTO.java（商家注册）
- MerchantLoginDTO.java（商家登录）
- StoreCreateDTO.java（店铺创建）

**Goods模块：**
- GoodsCreateDTO.java（商品创建）
- GoodsUpdateDTO.java（商品更新）
- SkuCreateDTO.java（SKU创建）

**Order模块：**
- OrderCreateDTO.java（订单创建）
- CartAddDTO.java（购物车添加）

---

### 2. Controller改造

**改造前：**
```java
@PostMapping
public Result<Order> createOrder(@RequestBody Order order) {
    // 直接使用Entity
}
```

**改造后：**
```java
@PostMapping
public Result<Order> createOrder(@RequestBody @Valid OrderCreateDTO dto) {
    // 使用DTO
    Order order = convertToEntity(dto);
    // ...
}
```

---

### 3. 转换工具类

创建通用转换工具：
- DTOConverter.java（转换工具）
- BeanCopyUtils.java（属性拷贝）

---

## ⏰ 重构时间

**预计：** 2-3天（完整重构）
**优先级：** P2（长期优化）

---

## ⚠️ 注意事项

1. 保持向后兼容
2. 添加参数校验注解
3. 编写单元测试
4. 更新API文档

---

## 📊 当前进度

**已标注：** Controller添加TODO注释  
**待实现：** DTO类创建 + Controller改造

---

*计划创建时间: 2026-03-03 22:31*
