package com.b2b2c.goods_service.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 商品创建DTO
 * 
 * ✅ 完整的参数校验示例
 */
@Data
public class GoodsCreateDTO {
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    
    @NotNull(message = "商家ID不能为空")
    private Long merchantId;
    
    @NotBlank(message = "商品名称不能为空")
    @Size(min = 2, max = 200, message = "商品名称长度必须在2-200个字符之间")
    private String name;
    
    @Size(max = 500, message = "副标题长度不能超过500个字符")
    private String subtitle;
    
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0.01")
    @DecimalMax(value = "999999.99", message = "价格不能超过999999.99")
    private BigDecimal price;
    
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    @Max(value = 999999, message = "库存不能超过999999")
    private Integer stock;
    
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值无效")
    @Max(value = 2, message = "状态值无效")
    private Integer status;
    
    @Size(max = 2000, message = "商品描述长度不能超过2000个字符")
    private String description;
}
