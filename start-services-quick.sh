#!/bin/bash

echo "======================================"
echo "  快速启动所有服务"
echo "  启动时间: $(date '+%Y-%m-%d %H:%M:%S')"
echo "======================================"
echo ""

# 创建日志目录
mkdir -p logs

# 启动用户服务（9001）
echo "启动用户服务（9001）..."
nohup java -Xms256m -Xmx512m \
    -jar user-service/target/user-service-1.0.0-SNAPSHOT.jar \
    --server.port=9001 \
    > logs/user-service-9001.log 2>&1 &
echo $! > logs/user-service-9001.pid
echo "✅ 用户服务启动中（PID: $(cat logs/user-service-9001.pid)）"

# 启动商家服务（9002）
echo "启动商家服务（9002）..."
nohup java -Xms256m -Xmx512m \
    -jar merchant-service/target/merchant-service-1.0.0-SNAPSHOT.jar \
    --server.port=9002 \
    > logs/merchant-service-9002.log 2>&1 &
echo $! > logs/merchant-service-9002.pid
echo "✅ 商家服务启动中（PID: $(cat logs/merchant-service-9002.pid)）"

# 启动商品服务（9003）
echo "启动商品服务（9003）..."
nohup java -Xms256m -Xmx512m \
    -jar goods-service/target/goods-service-1.0.0-SNAPSHOT.jar \
    --server.port=9003 \
    > logs/goods-service-9003.log 2>&1 &
echo $! > logs/goods-service-9003.pid
echo "✅ 商品服务启动中（PID: $(cat logs/goods-service-9003.pid)）"

# 启动订单服务（9007）
echo "启动订单服务（9007）..."
nohup java -Xms256m -Xmx512m \
    -jar order-service/target/order-service-1.0.0-SNAPSHOT.jar \
    --server.port=9007 \
    > logs/order-service-9007.log 2>&1 &
echo $! > logs/order-service-9007.pid
echo "✅ 订单服务启动中（PID: $(cat logs/order-service-9007.pid)）"

echo ""
echo "======================================"
echo "  所有服务已启动"
echo "======================================"
echo ""
echo "服务地址："
echo "  用户服务：http://localhost:9001"
echo "  商家服务：http://localhost:9002"
echo "  商品服务：http://localhost:9003"
echo "  订单服务：http://localhost:9007"
echo ""
echo "日志位置：logs/"
echo ""
echo "等待服务启动完成...（30秒）"
