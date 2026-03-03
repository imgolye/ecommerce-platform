#!/bin/bash

# API自动化测试脚本（新端口：9001-9007）
# 测试时间：2026-03-03 23:21

BASE_URL="http://localhost"
TOKEN=""
PASS=0
FAIL=0
TOTAL=0

# 颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# 测试结果数组
declare -a RESULTS

# 测试函数
test_api() {
    local method=$1
    local path=$2
    local data=$3
    local name=$4
    local port=$5
    
    TOTAL=$((TOTAL + 1))
    
    echo -n "测试 $TOTAL: $name ... "
    
    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" \
            -X $method \
            "${BASE_URL}:${port}${path}" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $TOKEN" 2>&1)
    else
        response=$(curl -s -w "\n%{http_code}" \
            -X $method \
            "${BASE_URL}:${port}${path}" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $TOKEN" \
            -d "$data" 2>&1)
    fi
    
    http_code=$(echo "$response" | tail -n 1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        echo -e "${GREEN}✓ PASS${NC} (HTTP $http_code)"
        PASS=$((PASS + 1))
        RESULTS+=("✅ $name (HTTP $http_code)")
    else
        echo -e "${RED}✗ FAIL${NC} (HTTP $http_code)"
        FAIL=$((FAIL + 1))
        RESULTS+=("❌ $name (HTTP $http_code)")
    fi
    
    # 提取token（登录成功后）
    if [[ $path == *"/login"* ]] && [ "$http_code" -eq 200 ]; then
        TOKEN=$(echo "$body" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    fi
    
    sleep 0.1
}

echo "======================================"
echo "  API自动化测试（新端口）"
echo "  测试时间: $(date '+%Y-%m-%d %H:%M:%S')"
echo "======================================"
echo ""

# ========== 1. 服务健康检查 ==========
echo "【1. 服务健康检查】"
test_api "GET" "/actuator/health" "" "用户服务健康检查" 9001
test_api "GET" "/actuator/health" "" "商家服务健康检查" 9002
test_api "GET" "/actuator/health" "" "商品服务健康检查" 9003
test_api "GET" "/actuator/health" "" "订单服务健康检查" 9007
echo ""

# ========== 2. 用户模块测试 ==========
echo "【2. 用户模块测试】"
test_api "POST" "/api/user/register" \
    '{"username":"testuser001","password":"123456","phone":"13800138001"}' \
    "用户注册" 9001

test_api "POST" "/api/user/login" \
    '{"username":"testuser001","password":"123456"}' \
    "用户登录" 9001

test_api "GET" "/api/user/1" "" "查询用户信息" 9001
test_api "GET" "/api/user/list?page=1&size=10" "" "用户列表" 9001
echo ""

# ========== 3. 商家模块测试 ==========
echo "【3. 商家模块测试】"
test_api "POST" "/api/merchant/register" \
    '{"username":"testmerchant001","password":"123456","phone":"13900139001","companyName":"测试公司"}' \
    "商家注册" 9002

test_api "POST" "/api/merchant/login" \
    '{"username":"testmerchant001","password":"123456"}' \
    "商家登录" 9002

test_api "GET" "/api/merchant/1" "" "查询商家信息" 9002
test_api "GET" "/api/merchant/store/list?merchantId=1" "" "店铺列表" 9002
echo ""

# ========== 4. 商品模块测试 ==========
echo "【4. 商品模块测试】"
test_api "POST" "/goods/category" \
    '{"name":"电子产品","parentId":0,"level":1}' \
    "创建分类" 9003

test_api "GET" "/goods/category/tree" "" "分类树" 9003
test_api "GET" "/goods/category/1" "" "分类详情" 9003
test_api "GET" "/goods/list?page=1&size=10" "" "商品列表" 9003
test_api "GET" "/goods/1" "" "商品详情" 9003
echo ""

# ========== 5. 订单模块测试 ==========
echo "【5. 订单模块测试】"
test_api "POST" "/cart" \
    '{"userId":1,"goodsId":1,"skuId":1,"quantity":2}' \
    "添加到购物车" 9007

test_api "GET" "/cart/user/1" "" "查询购物车" 9007
test_api "POST" "/order" \
    '{"userId":1,"merchantId":1,"totalAmount":199.98,"payAmount":199.98,"receiverName":"张三","receiverPhone":"13800138000","receiverAddress":"北京市朝阳区"}' \
    "创建订单" 9007

test_api "GET" "/order/1" "" "订单详情" 9007
test_api "GET" "/order/user/1" "" "用户订单列表" 9007
echo ""

# ========== 测试报告 ==========
echo "======================================"
echo "  测试报告"
echo "======================================"
echo ""
echo "测试总数: $TOTAL"
echo -e "${GREEN}通过: $PASS${NC}"
echo -e "${RED}失败: $FAIL${NC}"
echo ""

if [ $FAIL -eq 0 ]; then
    echo -e "${GREEN}✓ 所有测试通过！${NC}"
    echo "通过率: 100%"
else
    echo -e "${RED}✗ 部分测试失败${NC}"
    pass_rate=$(awk "BEGIN {printf \"%.1f\", ($PASS/$TOTAL)*100}")
    echo "通过率: $pass_rate%"
fi

echo ""
echo "======================================"
echo "  详细结果"
echo "======================================"
for result in "${RESULTS[@]}"; do
    echo "$result"
done

echo ""
echo "测试完成时间: $(date '+%Y-%m-%d %H:%M:%S')"
echo "======================================"
