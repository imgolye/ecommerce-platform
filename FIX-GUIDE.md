# B2B2C电商小程序平台 - 完整修复指南

## 📋 修复优先级

### P0 - 立即修复（40分钟，通过率40%+）

#### 1. 商家服务修复（15分钟）

**问题：** 商家注册/登录返回500错误

**修复步骤：**
```java
// MerchantService.java - 简化方法
public interface MerchantService extends IService<Merchant> {
    Long register(MerchantRegisterDTO dto);
    MerchantLoginVO login(MerchantLoginDTO dto);
}

// MerchantServiceImpl.java - 简化实现
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {
    @Override
    public Long register(MerchantRegisterDTO dto) {
        // 检查用户名
        if (baseMapper.selectByUsername(dto.getUsername()) != null) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        // 创建商家
        Merchant merchant = new Merchant();
        merchant.setUsername(dto.getUsername());
        merchant.setPassword(dto.getPassword());
        merchant.setPhone(dto.getPhone());
        merchant.setCompanyName(dto.getCompanyName());
        merchant.setStatus(1);
        
        baseMapper.insert(merchant);
        return merchant.getId();
    }
    
    @Override
    public MerchantLoginVO login(MerchantLoginDTO dto) {
        Merchant merchant = baseMapper.selectByUsername(dto.getUsername());
        if (merchant == null || !dto.getPassword().equals(merchant.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        
        String token = jwtUtil.generateToken(merchant.getId(), merchant.getUsername());
        MerchantLoginVO vo = new MerchantLoginVO();
        vo.setMerchantId(merchant.getId());
        vo.setToken(token);
        vo.setExpiresIn(jwtUtil.getExpiration());
        return vo;
    }
}
```

#### 2. 商品服务修复（20分钟）

**问题：** 分类/商品接口返回500错误

**修复步骤：**
```java
// 1. 检查数据库连接
@SpringBootApplication
public class GoodsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsServiceApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner checkDatabase(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("✅ 数据库连接成功: " + conn.getMetaData().getURL());
            } catch (Exception e) {
                System.err.println("❌ 数据库连接失败: " + e.getMessage());
            }
        };
    }
}

// 2. 添加Mapper方法
@Mapper
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {
    @Select("SELECT * FROM goods_category WHERE parent_id = 0 ORDER BY sort")
    List<GoodsCategory> selectRootCategories();
    
    @Select("SELECT * FROM goods_category WHERE parent_id = #{parentId} ORDER BY sort")
    List<GoodsCategory> selectByParentId(Long parentId);
}

// 3. 简化Service
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {
    @Override
    public List<GoodsCategory> getCategoryTree() {
        List<GoodsCategory> roots = baseMapper.selectRootCategories();
        for (GoodsCategory root : roots) {
            List<GoodsCategory> children = baseMapper.selectByParentId(root.getId());
            root.setChildren(children);
        }
        return roots;
    }
}
```

#### 3. Actuator配置（5分钟）

**问题：** 健康检查返回404

**修复步骤：**
```xml
<!-- pom.xml添加 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
# application.yml添加
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: always
```

### P1 - 次要修复（45分钟，通过率60%+）

#### 4. 用户列表（10分钟）

```java
@GetMapping("/list")
public Result<IPage<UserVO>> list(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
    Page<User> pageParam = new Page<>(page, size);
    IPage<User> userPage = userService.page(pageParam);
    
    IPage<UserVO> voPage = userPage.convert(user -> {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    });
    
    return Result.success(voPage);
}
```

#### 5. 购物车添加（15分钟）

```java
@Override
@Transactional(rollbackFor = Exception.class)
public Long addToCart(CartAddDTO dto) {
    // 检查商品是否存在
    Goods goods = goodsClient.getGoods(dto.getGoodsId());
    if (goods == null) {
        throw new BusinessException(404, "商品不存在");
    }
    
    // 检查库存
    if (goods.getStock() < dto.getQuantity()) {
        throw new BusinessException(400, "库存不足");
    }
    
    // 添加到购物车
    Cart cart = new Cart();
    cart.setUserId(UserContext.getCurrentUserId());
    cart.setGoodsId(dto.getGoodsId());
    cart.setQuantity(dto.getQuantity());
    cart.setPrice(goods.getPrice());
    
    baseMapper.insert(cart);
    return cart.getId();
}
```

#### 6. 创建订单（20分钟）

```java
@Override
@Transactional(rollbackFor = Exception.class)
public Long createOrder(OrderCreateDTO dto) {
    Long userId = UserContext.getCurrentUserId();
    
    // 检查购物车
    List<Cart> carts = cartMapper.selectByUserId(userId);
    if (carts.isEmpty()) {
        throw new BusinessException(400, "购物车为空");
    }
    
    // 创建订单
    Order order = new Order();
    order.setOrderNo(generateOrderNo());
    order.setUserId(userId);
    order.setStatus(OrderStatus.PENDING.getCode());
    order.setTotalAmount(calculateTotal(carts));
    
    baseMapper.insert(order);
    
    // 创建订单明细
    for (Cart cart : carts) {
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setGoodsId(cart.getGoodsId());
        item.setQuantity(cart.getQuantity());
        item.setPrice(cart.getPrice());
        
        orderItemMapper.insert(item);
    }
    
    // 清空购物车
    cartMapper.deleteByUserId(userId);
    
    return order.getId();
}

private String generateOrderNo() {
    return "ORD" + System.currentTimeMillis();
}

private BigDecimal calculateTotal(List<Cart> carts) {
    return carts.stream()
        .map(c -> c.getPrice().multiply(new BigDecimal(c.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
}
```

### P2 - 完整优化（70分钟，通过率80%+）

#### 7. 商品分类管理（30分钟）

```java
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {
    
    @Autowired
    private GoodsCategoryMapper categoryMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public List<GoodsCategoryTreeVO> getCategoryTree() {
        // 尝试从缓存获取
        String cacheKey = "goods:category:tree";
        List<GoodsCategoryTreeVO> tree = (List<GoodsCategoryTreeVO>) redisTemplate.opsForValue().get(cacheKey);
        
        if (tree != null) {
            return tree;
        }
        
        // 查询所有分类
        List<GoodsCategory> allCategories = categoryMapper.selectList(null);
        
        // 构建树形结构
        tree = buildTree(allCategories, 0L);
        
        // 缓存1小时
        redisTemplate.opsForValue().set(cacheKey, tree, 1, TimeUnit.HOURS);
        
        return tree;
    }
    
    private List<GoodsCategoryTreeVO> buildTree(List<GoodsCategory> categories, Long parentId) {
        return categories.stream()
            .filter(c -> parentId.equals(c.getParentId()))
            .map(c -> {
                GoodsCategoryTreeVO vo = new GoodsCategoryTreeVO();
                BeanUtils.copyProperties(c, vo);
                vo.setChildren(buildTree(categories, c.getId()));
                return vo;
            })
            .collect(Collectors.toList());
    }
}
```

#### 8. 商品列表/详情（40分钟）

```java
@Service
public class GoodsServiceImpl implements GoodsService {
    
    @Autowired
    private GoodsMapper goodsMapper;
    
    @Autowired
    private ElasticsearchRestTemplate esTemplate;
    
    @Override
    public IPage<GoodsVO> getGoodsList(GoodsQueryDTO query) {
        Page<Goods> page = new Page<>(query.getPage(), query.getSize());
        
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        
        // 分类筛选
        if (query.getCategoryId() != null) {
            wrapper.eq(Goods::getCategoryId, query.getCategoryId());
        }
        
        // 关键词搜索
        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.like(Goods::getName, query.getKeyword())
                   .or()
                   .like(Goods::getDescription, query.getKeyword());
        }
        
        // 价格区间
        if (query.getMinPrice() != null) {
            wrapper.ge(Goods::getPrice, query.getMinPrice());
        }
        if (query.getMaxPrice() != null) {
            wrapper.le(Goods::getPrice, query.getMaxPrice());
        }
        
        // 排序
        wrapper.orderByDesc(Goods::getSalesCount);
        
        IPage<Goods> goodsPage = goodsMapper.selectPage(page, wrapper);
        
        return goodsPage.convert(this::convertToVO);
    }
    
    @Override
    public GoodsDetailVO getGoodsDetail(Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            throw new BusinessException(404, "商品不存在");
        }
        
        GoodsDetailVO vo = new GoodsDetailVO();
        BeanUtils.copyProperties(goods, vo);
        
        // 查询SKU
        vo.setSkus(goodsSkuMapper.selectByGoodsId(goodsId));
        
        // 查询图片
        vo.setImages(goodsImageMapper.selectByGoodsId(goodsId));
        
        // 查询规格
        vo.setSpecs(goodsSpecMapper.selectByGoodsId(goodsId));
        
        return vo;
    }
}
```

## 📊 预期效果

### P0修复后（40分钟）
- 通过率：40%+（9/22）
- 通过：用户3个 + 商家3个 + 订单2个 + 健康1个

### P1修复后（1.5小时）
- 通过率：60%+（13/22）
- 新增：用户列表 + 购物车添加 + 创建订单

### P2修复后（2.5小时）
- 通过率：80%+（18/22）
- 新增：商品分类3个 + 商品2个 + 健康3个

## 🚀 执行建议

1. **立即执行P0：** 40分钟达到40%+通过率
2. **按需执行P1：** 提升至60%+通过率
3. **计划执行P2：** 达到生产级80%+通过率

## ✅ 验证方法

```bash
# 修复后重新测试
bash test-api-new-ports.sh

# 检查日志
tail -f logs/user-service-9001.log
tail -f logs/merchant-service-9002.log
tail -f logs/goods-service-9003.log
tail -f logs/order-service-9007.log
```

---

*修复指南版本: 1.0*
*最后更新: 2026-03-03 23:59*
