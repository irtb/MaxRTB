#!/bin/bash

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}========== 智选SDK核心代码打包脚本 ==========${NC}"

# 检查是否在项目根目录
if [ ! -f "settings.gradle" ]; then
    echo -e "${RED}错误：请在项目根目录执行此脚本！${NC}"
    exit 1
fi

# 定义打包目录
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
PACKAGE_DIR="zx-core-backup-${TIMESTAMP}"
PACKAGE_FILE="${PACKAGE_DIR}.tar.gz"

echo -e "${YELLOW}创建打包目录...${NC}"
mkdir -p "${PACKAGE_DIR}"

# 1. 复制根级配置文件
echo -e "${YELLOW}复制根级配置文件...${NC}"
cp -v settings.gradle "${PACKAGE_DIR}/"
cp -v build.gradle "${PACKAGE_DIR}/"
cp -v config.gradle "${PACKAGE_DIR}/"
cp -v gradle.properties "${PACKAGE_DIR}/"
cp -v version.properties "${PACKAGE_DIR}/"
[ -f "local.properties" ] && cp -v local.properties "${PACKAGE_DIR}/"

# 2. 复制gradle wrapper
echo -e "${YELLOW}复制gradle wrapper...${NC}"
cp -rv gradle/ "${PACKAGE_DIR}/"
cp -v gradlew "${PACKAGE_DIR}/"
cp -v gradlew.bat "${PACKAGE_DIR}/"

# 3. 复制核心SDK模块
echo -e "${YELLOW}复制zx模块...${NC}"
mkdir -p "${PACKAGE_DIR}/zx"
cp -v zx/build.gradle "${PACKAGE_DIR}/zx/" 2>/dev/null || true
cp -v zx/proguard-rules.pro "${PACKAGE_DIR}/zx/" 2>/dev/null || true
cp -v zx/consumer-rules.pro "${PACKAGE_DIR}/zx/" 2>/dev/null || true
cp -rv zx/src "${PACKAGE_DIR}/zx/"

# 4. 复制Demo模块
echo -e "${YELLOW}复制zxdm模块...${NC}"
mkdir -p "${PACKAGE_DIR}/zxdm"
cp -v zxdm/build.gradle "${PACKAGE_DIR}/zxdm/" 2>/dev/null || true
cp -v zxdm/proguard-rules.pro "${PACKAGE_DIR}/zxdm/" 2>/dev/null || true
cp -rv zxdm/src "${PACKAGE_DIR}/zxdm/"

# 5. 复制core模块（如果存在）
if [ -d "core" ]; then
    echo -e "${YELLOW}复制core模块...${NC}"
    mkdir -p "${PACKAGE_DIR}/core"
    cp -v core/build.gradle "${PACKAGE_DIR}/core/" 2>/dev/null || true
    cp -v core/proguard-rules.pro "${PACKAGE_DIR}/core/" 2>/dev/null || true
    cp -v core/consumer-rules.pro "${PACKAGE_DIR}/core/" 2>/dev/null || true
    cp -rv core/src "${PACKAGE_DIR}/core/"
fi

# 6. 复制zhixuan模块（如果存在）
if [ -d "zhixuan" ]; then
    echo -e "${YELLOW}复制zhixuan模块...${NC}"
    mkdir -p "${PACKAGE_DIR}/zhixuan"
    cp -v zhixuan/build.gradle "${PACKAGE_DIR}/zhixuan/" 2>/dev/null || true
    cp -rv zhixuan/src "${PACKAGE_DIR}/zhixuan/"
fi

# 7. 复制参考SDK（可选）
echo -e "${YELLOW}复制参考SDK模块（csj/gdt/ks）...${NC}"
for sdk in csj gdt ks; do
    if [ -d "$sdk" ]; then
        mkdir -p "${PACKAGE_DIR}/${sdk}"
        cp -v ${sdk}/build.gradle "${PACKAGE_DIR}/${sdk}/" 2>/dev/null || true
        cp -rv ${sdk}/src/main/java "${PACKAGE_DIR}/${sdk}/" 2>/dev/null || true
        cp -rv ${sdk}/src/main/AndroidManifest.xml "${PACKAGE_DIR}/${sdk}/" 2>/dev/null || true
    fi
done

# 8. 复制文档
echo -e "${YELLOW}复制文档...${NC}"
cp -rv docs "${PACKAGE_DIR}/" 2>/dev/null || true
cp -rv doc "${PACKAGE_DIR}/" 2>/dev/null || true
[ -f "README.md" ] && cp -v README.md "${PACKAGE_DIR}/"

# 9. 创建信息文件
echo -e "${YELLOW}生成项目信息文件...${NC}"
cat > "${PACKAGE_DIR}/PROJECT_INFO.txt" << 'EOF'
【项目信息】
项目名称：智选SDK
当前版本：6.0.0
最小API Level：21
Target API Level：34

【模块清单】
✓ zx           - 主SDK模块
✓ zxdm         - Demo演示应用
✓ core         - 核心库（如有）
✓ csj/gdt/ks   - 参考SDK（架构对标）

【打包包含内容】
✓ 所有源代码（src/main/java & kotlin）
✓ 资源文件（src/main/res）
✓ 构建配置（build.gradle, gradle.properties等）
✓ 清单文件（AndroidManifest.xml）
✓ 混淆规则（proguard-rules.pro）
✓ gradle wrapper

【排除内容】
✗ build目录（编译输出）
✗ .gradle目录
✗ .idea目录
✗ 日志文件

【下一步】
1. 解压此包：tar -xzf 包名.tar.gz
2. 使用Android Studio打开根目录
3. 等待gradle同步完成
EOF

# 10. 打包成tar.gz
echo -e "${YELLOW}压缩打包...${NC}"
tar -czf "${PACKAGE_FILE}" "${PACKAGE_DIR}/" \
    --exclude='**/build' \
    --exclude='**/.gradle' \
    --exclude='**/.idea' \
    --exclude='**/*.log' \
    --exclude='**/.git'

# 11. 清理临时目录
echo -e "${YELLOW}清理临时文件...${NC}"
rm -rf "${PACKAGE_DIR}"

# 12. 生成校验和
echo -e "${YELLOW}生成校验和...${NC}"
md5sum "${PACKAGE_FILE}" > "${PACKAGE_FILE}.md5"
sha256sum "${PACKAGE_FILE}" > "${PACKAGE_FILE}.sha256"

# 13. 输出结果
echo ""
echo -e "${GREEN}========== 打包完成！ ==========${NC}"
echo -e "${GREEN}打包文件：${NC} ${PACKAGE_FILE}"
echo -e "${GREEN}文件大小：${NC} $(du -h "${PACKAGE_FILE}" | cut -f1)"
echo -e "${GREEN}MD5校验：${NC} $(cat ${PACKAGE_FILE}.md5)"
echo ""
echo -e "${YELLOW}快速开始：${NC}"
echo "  tar -xzf ${PACKAGE_FILE}"
echo "  cd zx-core-backup-*/zx"
echo ""
