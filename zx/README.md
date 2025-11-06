# ZX 广告 SDK

ZX 是一个全新设计的、高效的、易于集成的移动广告 SDK。

## 特性

- ✅ **高效架构**: 模块化设计，单一职责原则
- ✅ **内存安全**: 完整的生命周期管理，防止内存泄漏
- ✅ **灵活扩展**: 基于工厂模式和策略模式的易扩展设计
- ✅ **完整监听**: 详细的事件监听接口
- ✅ **智能重试**: 内置重试机制和超时控制
- ✅ **图片缓存**: 高效的图片加载和缓存机制

## 支持的广告类型

- 📱 Banner 广告 (320x50, 300x250 等)
- 🎬 激励视频 (Rewarded Video)
- 🎯 插屏广告 (Interstitial)
- 🖼️ 原生广告 (Native Ads)
- 🚀 启屏广告 (Splash Ads)

## 快速开始

### 1. 初始化 SDK

```kotlin
import com.maxrtb.zx.ZX
import com.maxrtb.zx.config.ZXConfig

// 在 Application 中初始化
ZX.init(
    context = this,
    config = ZXConfig(
        appId = "your_app_id",
        debugMode = BuildConfig.DEBUG,
        enableDebugLog = BuildConfig.DEBUG
    )
)
### 2. 请求 Banner 广告

```kotlin
import com.maxrtb.zx.ZX
import com.maxrtb.zx.listener.ZXBannerListener

ZX.requestBannerAd(
    activity = this,
    slotId = "banner_slot_id",
    listener = object : ZXBannerListener {
        override fun onAdLoaded() {
            // Banner 已加载
        }
        
        override fun onAdShown() {
            // Banner 已展示
        }
        
        override fun onAdClicked() {
            // Banner 被点击
        }
        
        override fun onAdClosed() {
            // Banner 已关闭
        }
        
        override fun onAdFailed(msg: String) {
            // 加载失败
        }
    }
)
```

### 3. 请求激励视频

```kotlin
import com.maxrtb.zx.listener.ZXRewardListener

ZX.requestRewardedAd(
    activity = this,
    slotId = "reward_slot_id",
    listener = object : ZXRewardListener {
        override fun onAdLoaded() {}
        override fun onAdShown() {}
        override fun onAdClicked() {}
        override fun onVideoComplete() {}
        override fun onRewarded() {
            // 用户获得奖励
        }
        override fun onAdClosed() {}
        override fun onAdFailed(msg: String) {}
    }
)
```

## 文件结构

```
zx/
├── adapter/              # 广告适配器
├── api/                  # API 接口
├── base/                 # 基类
├── bidding/              # 竞价管理
├── cache/                # 缓存管理
├── config/               # 配置管理
├── factory/              # 工厂类
├── helper/               # 辅助工具
├── imageloader/          # 图片加载
├── listener/             # 监听器接口
├── manager/              # 管理器
├── model/                # 数据模型
├── native_/              # 原生广告
├── network/              # 网络管理
├── preload/              # 预加载
├── provider/             # 提供者
├── retry/                # 重试管理
├── tracker/              # 追踪
└── view/                 # 视图组件
```

## 版本历史

### v1.0.0 (当前)

* 初始版本
* 支持 5 种广告类型
* 完整的生命周期管理

## 许可证

MIT License

## 联系方式

* 官网: [https://maxrtb.com](https://maxrtb.com)
* 邮箱: [support@maxrtb.com](mailto:support@maxrtb.com)