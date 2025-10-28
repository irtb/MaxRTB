# 常见问题

## 集成问题

### Q1: Gradle 编译失败

**错误信息：**
````

Could not resolve: com.maxrtb.zhixuan

```

**解决方案：**
1. 检查 `settings.gradle` 是否包含模块
2. 确认项目结构正确
3. 执行 `./gradlew clean build`

---

### Q2: 混淆后闪退

**错误信息：**
```

ClassNotFoundException: com.maxrtb.zhixuan.provider.ZhixuanProvider

````

**解决方案：**
添加混淆规则：
```proguard
-keep class com.maxrtb.zhixuan.** { *; }
````

---

## 广告加载问题

### Q3: 广告请求失败 - 网络错误

**错误信息：**

```
onAdFailed: java.net.UnknownHostException
```

**排查步骤：**

1. 检查网络权限
2. 确认BASE\_URL配置正确
3. 测试网络连通性：`ping api.zhixuan.com`
4. 查看防火墙/代理设置

---

### Q4: 广告请求成功但无返回

**可能原因：**

* 广告位ID错误
* 无可用广告
* 竞价价格过低

**解决方案：**

```kotlin
// 查看完整响应
adb logcat | grep "okhttp"
```

---

### Q5: 图片不显示

**排查步骤：**

1. 检查图片URL有效性
2. 确认Glide依赖正确
3. 查看网络请求日志
4. 测试素材URL：在浏览器打开

---

### Q6: 视频无法播放

**可能原因：**

* 视频格式不支持
* 视频URL失效
* MediaPlayer初始化失败

**支持格式：**

* MP4 (H.264)
* 3GP
* WebM

---

## 回调问题

### Q7: onAdLoaded 不触发

**检查项：**

```kotlin
// 确保回调实现完整
override fun onAdLoaded(providerType: String) {
    Log.d(TAG, "广告加载成功")
}
```

---

### Q8: onAdDismissed 未调用

**原因：**

* 视频播放异常
* 倒计时未完成
* Activity被销毁

**解决：**

```kotlin
override fun onDestroy() {
    super.onDestroy()
    provider.destroy()
}
```

---

## 性能问题

### Q9: 内存泄漏

**排查工具：**

```bash
# 使用 LeakCanary
debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.12'
```

**常见原因：**

* MediaPlayer未释放
* 倒计时未取消
* 监听器未清空

---

### Q10: 广告加载慢

**优化方案：**

1. 预加载广告
2. 使用图片缓存
3. 减少网络超时时间
4. 启用HTTP/2

---

## 调试技巧

### 启用详细日志

```kotlin
TogetherAd.printLogEnable = true
ZhixuanHelper.printLogEnable = true
```

### 查看网络请求

```bash
adb logcat | grep "okhttp"
```

### 过滤SDK日志

```bash
adb logcat *:S Zhixuan:V TogetherAd:V
```

### 抓包分析

使用 Charles/Fiddler 查看完整HTTP请求

---

## 测试环境

### Mock服务配置

使用 Apifox/Postman Mock：

```
BASE_URL = "https://mock.apifox.cn/xxx"
```

### 测试广告位

```kotlin
const val SPLASH_TEST_ID = "splash_test"
const val SPLASH_VIDEO_ID = "splash_video"
```

---

## 联系支持

**遇到无法解决的问题？**

* GitHub Issues: [提交Issue](https://github.com/maxrtb/issues)
* 技术支持邮箱: [support@maxrtb.com](mailto:support@maxrtb.com)
* 开发者社区: [forum.maxrtb.com](https://forum.maxrtb.com)

---

**更新时间:** 2025-10-24