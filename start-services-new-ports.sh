#!/bin/bash

# 使用新端口启动所有服务
# 启动时间：2026-03-03 23:02

echo "======================================"
echo "  启动所有服务（新端口）"
echo "  启动时间: $(date '+%Y-%m-%d %H:%M:%S')"
echo "======================================"
echo ""

# 创建日志目录
mkdir -p logs

# 检查JAR文件
echo "检查JAR文件..."
for jar in user-service/target/user-service-1.0.0-SNAPSHOT.jar \
           merchant-service/target/merchant-service-1.0.0-SNAPSHOT.jar \
           goods-service/target/goods-service-1.0.0-SNAPSHOT.jar \
           order-service/target/order-service-1.0.0-SNAPSHOT.jar; do
    if [ ! -f "$jar" ]; then
        echo "❌ 文件不存在: $jar"
        echo "请先编译项目: mvn clean package"
        exit 1
    fi
done
echo "✅ JAR文件检查通过"
echo ""

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
echo "等待服务启动...（30秒）"
sleep 30
echo ""
echo "检查服务状态..."

# 检查服务状态
for port in 9001 9002 9003 9007; do
    if curl -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
        echo "✅ 端口 $port 服务正常"
    else
        echo "⚠️  端口 $port 服务可能未完全启动"
    fi
done

echo ""
echo "启动完成时间: $(date '+%Y-%m-%d %H:%M:%S')"
echo "======================================"
