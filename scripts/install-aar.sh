#!/bin/bash

# 安装 AAR 到本地项目

AAR_PATH="zhixuan/build/outputs/aar/zhixuan-release-1.0.0.aar"
TARGET_DIR="$1"

if [ -z "$TARGET_DIR" ]; then
    echo "用法: ./install-aar.sh <目标项目路径>"
    exit 1
fi

# 创建 libs 目录
mkdir -p "$TARGET_DIR/app/libs"

# 复制 AAR
cp "$AAR_PATH" "$TARGET_DIR/app/libs/"

echo "✅ AAR 已安装到: $TARGET_DIR/app/libs/"
echo ""
echo "📝 请在 build.gradle 中添加："
echo "dependencies {"
echo "    implementation files('libs/zhixuan-release-1.0.0.aar')"
echo "    implementation 'com.google.code.gson:gson:2.10.1'"
echo "    implementation 'com.squareup.okhttp3:okhttp:4.12.0'"
echo "    implementation 'com.github.bumptech.glide:glide:4.16.0'"
echo "}"
