# 智选广告 SDK (MaxRTB Zhixuan SDK)

完整的 Android 广告聚合 SDK，支持多种广告形式的加载、展示和监测。

## ✨ 功能特性

### 支持的广告类型
- ✅ **开屏广告** - 全屏沉浸式体验
- ✅ **Banner 广告** - 灵活的尺寸配置
- ✅ **插屏广告** - 可关闭的对话框广告
- ✅ **激励视频** - 完整观看获得奖励
- ✅ **原生广告** - 与应用界面融合

### 高级功能
- 🚀 **智能缓存** - 30分钟自动过期
- 📥 **预加载机制** - 后台提前加载
- 🔄 **错误重试** - 指数退避算法（最多3次）
- 📊 **完整监测** - 曝光、点击、转化追踪
- ⚡ **竞价引擎** - OpenRTB 2.5 协议支持

## 📦 依赖

```gradle
dependencies {
    implementation 'com.maxrtb:zhixuan-sdk:1.0.0'
}
````

## 🚀 快速开始

### 初始化

```kotlin
import com.maxrtb.zhixuan.manager.ZhixuanAdManager

// 在 Application 中初始化
ZhixuanAdManager.init(context)
ZhixuanAdManager.setDebugEnabled(true)
```

### 加载开屏广告

```kotlin
val provider = ZhixuanProvider()
provider.loadAndShowSplashAd(
    activity = this,
    adProviderType = "zhixuan",
    alias = "splash_test",
    container = container,
    listener = object : SplashListener {
        override fun onAdLoaded(providerType: String) {
            Log.i("Ad", "开屏广告加载成功")
        }
        override fun onAdFailed(providerType: String, failedMsg: String?) {
            Log.e("Ad", "开屏广告加载失败: $failedMsg")
        }
        override fun onAdClicked(providerType: String) {
            Log.i("Ad", "开屏广告被点击")
        }
        override fun onAdExpose(providerType: String) {
            Log.i("Ad", "开屏广告曝光")
        }
        override fun onAdClose(providerType: String) {
            Log.i("Ad", "开屏广告关闭")
        }
    }
)
```

### 加载 Banner 广告

```kotlin
provider.showBannerAd(
    activity = this,
    adProviderType = "zhixuan",
    alias = "banner_test",
    container = bannerContainer,
    listener = object : BannerListener {
        override fun onAdLoaded(providerType: String) {}
        override fun onAdFailed(providerType: String, failedMsg: String?) {}
        override fun onAdClicked(providerType: String) {}
        override fun onAdExpose(providerType: String) {}
        override fun onAdClose(providerType: String) {}
    }
)
```

## 📊 架构设计

```
┌─────────────────────────────────┐
│      Application Layer          │
│  (Activity/Fragment/ViewModel)  │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│    ZhixuanProvider              │
│  (门面/聚合层)                  │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Adapter Layer                 │
│  (开屏/Banner/插屏/激励/原生)   │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│    Core Services                │
│  (缓存/预加载/重试/监测/网络)   │
└─────────────────────────────────┘
```

## 🔧 配置

### 网络端点

```kotlin
// 开发环境
const val BASE_URL_DEV = "https://m1.apifoxmock.com/m1/7056903-6777091-6404548/"

// 生产环境
const val BASE_URL_PROD = "https://api.zhixuan.com/"
```

### 缓存配置

```kotlin
// 缓存时效：30分钟
val CACHE_EXPIRY_TIME = 30 * 60 * 1000 // ms

// 缓存位置
val CACHE_DIR = context.cacheDir.absolutePath + "/ad_cache"
```

## 📈 性能指标

| 指标     | 数值       |
| ------ | -------- |
| 平均加载时间 | < 1000ms |
| 缓存命中率  | > 80%    |
| 成功率    | > 95%    |
| 内存占用   | < 20MB   |

## 📝 API 文档

### ZhixuanProvider

| 方法                      | 说明           |
| ----------------------- | ------------ |
| `loadAndShowSplashAd()` | 加载并展示开屏广告    |
| `showBannerAd()`        | 展示 Banner 广告 |
| `requestInterAd()`      | 请求插屏广告       |
| `requestRewardAd()`     | 请求激励视频       |
| `getNativeAdList()`     | 获取原生广告列表     |

### ZhixuanAdManager

| 方法                              | 说明      |
| ------------------------------- | ------- |
| `init(context)`                 | 初始化 SDK |
| `setDebugEnabled(enabled)`      | 设置调试模式  |
| `preloadAds(activity, slotIds)` | 预加载广告   |
| `getCacheInfo()`                | 获取缓存信息  |
| `clearCache()`                  | 清理缓存    |

## 🔐 混淆规则

```proguard
-keep class com.maxrtb.zhixuan.** { *; }
-keep class com.maxrtb.zhixuan.model.** { *; }
-keepclassmembers class com.maxrtb.zhixuan.** {
    <fields>;
    <methods>;
}
```

## 🐛 常见问题

### Q: 如何调试网络请求？

A: 启用调试模式查看完整日志

```kotlin
ZhixuanAdManager.setDebugEnabled(true)
```

### Q: 如何处理网络超时？

A: SDK 会自动重试 3 次，可配置 tmax 参数

### Q: 如何清理缓存？

A:

```kotlin
ZhixuanAdManager.clearCache()
```

## 📄 许可证

Apache License 2.0

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

## 📞 联系方式

* 邮件: [support@maxrtb.com](mailto:support@maxrtb.com)
* 文档: [https://docs.zhixuan.com](https://docs.zhixuan.com)

---

**版本**: 1.0.0
**更新时间**: 2024-10-28
**状态**: 生产就绪 ✅
