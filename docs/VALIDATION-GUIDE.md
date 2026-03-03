# 参数校验指南

## 📅 创建时间：2026-03-03 22:44

---

## ✅ JSR-303校验注解

### 1. 非空校验

| 注解 | 说明 | 示例 |
|------|------|------|
| @NotNull | 不能为null | @NotNull(message = "ID不能为空") |
| @NotBlank | 字符串不能为空 | @NotBlank(message = "名称不能为空") |
| @NotEmpty | 集合/数组不能为空 | @NotEmpty(message = "列表不能为空") |

### 2. 数值校验

| 注解 | 说明 | 示例 |
|------|------|------|
| @Min | 最小值 | @Min(value = 0, message = "不能小于0") |
| @Max | 最大值 | @Max(value = 100, message = "不能超过100") |
| @DecimalMin | 小数最小值 | @DecimalMin(value = "0.01") |
| @DecimalMax | 小数最大值 | @DecimalMax(value = "99999.99") |
| @Positive | 必须为正数 | @Positive(message = "必须为正数") |

### 3. 字符串校验

| 注解 | 说明 | 示例 |
|------|------|------|
| @Size | 长度范围 | @Size(min = 2, max = 50) |
| @Length | 长度范围（Hibernate） | @Length(min = 2, max = 50) |
| @Pattern | 正则表达式 | @Pattern(regexp = "^1[3-9]\\d{9}$") |
| @Email | 邮箱格式 | @Email(message = "邮箱格式不正确") |

### 4. 日期校验

| 注解 | 说明 | 示例 |
|------|------|------|
| @Past | 必须是过去 | @Past(message = "必须是过去日期") |
| @Future | 必须是未来 | @Future(message = "必须是未来日期") |

---

## 📝 使用示例

### DTO类

```java
@Data
public class UserRegisterDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;
}
```

### Controller

```java
@PostMapping("/register")
public Result<User> register(@RequestBody @Valid UserRegisterDTO dto) {
    // @Valid触发校验
    // 校验失败会抛出MethodArgumentNotValidException
    // GlobalExceptionHandler会统一处理
    return Result.success(userService.register(dto));
}
```

### GlobalExceptionHandler

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
    String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
    return Result.error(400, "参数错误：" + errorMessage);
}
```

---

## 🎯 校验最佳实践

1. **DTO层校验**
   - 所有DTO使用@Valid注解
   - 字段必须有明确的校验规则
   - 错误信息清晰明了

2. **金额校验**
   ```java
   @DecimalMin(value = "0.01", message = "金额必须大于0.01")
   @DecimalMax(value = "999999.99", message = "金额不能超过999999.99")
   private BigDecimal price;
   ```

3. **库存校验**
   ```java
   @Min(value = 0, message = "库存不能小于0")
   @Max(value = 999999, message = "库存不能超过999999")
   private Integer stock;
   ```

4. **手机号校验**
   ```java
   @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
   private String phone;
   ```

5. **枚举值校验**
   ```java
   @Min(value = 0, message = "状态值无效")
   @Max(value = 2, message = "状态值无效")
   private Integer status;
   ```

---

## ⚠️ 注意事项

1. **Entity vs DTO**
   - Entity不添加校验注解（数据库约束）
   - DTO添加校验注解（业务校验）

2. **分组校验**
   - 新增/更新使用不同校验规则
   - 使用@GroupSequence

3. **自定义校验**
   - 实现ConstraintValidator接口
   - 创建自定义注解

---

## 📚 参考文档

- [JSR-303规范](https://beanvalidation.org/)
- [Hibernate Validator](https://hibernate.org/validator/)
- [Spring Validation](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#validation)

---

*创建时间: 2026-03-03 22:44*
