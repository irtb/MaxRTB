# Zhixuan SDK - 智选广告SDK

智选广告SDK是基于OpenRTB 2.5协议的程序化广告SDK，提供完整的广告请求、展示和监测功能。

## 📦 模块说明

- `api/` - OpenRTB接口定义和数据模型
- `provider/` - 广告提供商实现
- `network/` - 网络请求封装
- `tracker/` - 监测上报系统
- `view/` - 广告视图组件
- `helper/` - 工具类和设备信息采集

## 🚀 核心功能

### 1. 开屏广告

**支持格式：**
- 图片开屏（静态图片）
- 视频开屏（MP4等视频格式）

**基本用法：**

```kotlin
val provider = ZhixuanProvider()

provider.loadAndShowSplashAd(
   activity = this,
   adProviderType = "zhixuan",
   alias = "splash_slot_id",
   container = splashContainer,
   listener = object : SplashListener {
       override fun onAdLoaded(providerType: String) {}
       override fun onAdFailed(providerType: String, failedMsg: String?) {}
       override fun onAdClicked(providerType: String) {}
       override fun onAdDismissed(providerType: String) {}
   }
)
````

## 📡 OpenRTB 协议

### BidRequest 结构

```json
{
  "id": "请求ID",
  "app": {
    "id": "应用ID",
    "name": "应用名称",
    "bundle": "包名",
    "ver": "版本"
  },
  "device": {
    "os": "Android",
    "osv": "系统版本",
    "make": "设备厂商",
    "model": "设备型号",
    "w": 1080,
    "h": 2217,
    "devicetype": 4,
    "ip": "IP地址",
    "ua": "User-Agent"
  },
  "imp": [{
    "id": "1",
    "tagid": "广告位ID",
    "banner": {
      "w": 1080,
      "h": 2217,
      "pos": 7
    },
    "bidfloor": 0.0,
    "bidfloorcur": "CNY"
  }]
}
```

### BidResponse 结构

```json
{
  "id": "响应ID",
  "bidid": "竞价ID",
  "cur": "CNY",
  "seatbid": [{
    "bid": [{
      "id": "广告ID",
      "impid": "1",
      "price": 5.0,
      "iurl": "图片URL",
      "w": 1080,
      "h": 2217,
      "ext": {
        "ldp": "落地页URL",
        "imptrackers": ["曝光监测URL"],
        "clicktrackers": ["点击监测URL"],
        "native": {
          "title": "标题",
          "desc": "描述",
          "mainimg": "主图URL"
        },
        "video": {
          "videourl": "视频URL",
          "duration": 5
        }
      }
    }]
  }]
}
```

## 🔧 配置说明

### 环境配置

```kotlin
// ZhixuanProvider.kt
companion object {
    const val BASE_URL_DEV = "https://dev-api.zhixuan.com/"
    const val BASE_URL_PROD = "https://api.zhixuan.com/"
    const val APP_ID = "100010"
}
```

### 网络配置

* 连接超时：10秒
* 读取超时：10秒
* 写入超时：10秒
* 自动重试：不启用（由上层控制）

## 📊 监测上报

### 支持的监测类型

| 监测类型 | 字段名              | 说明      |
| ---- | ---------------- | ------- |
| 曝光监测 | imptrackers      | 广告展示时上报 |
| 点击监测 | clicktrackers    | 广告点击时上报 |
| 视频开始 | starttrackers    | 视频开始播放  |
| 视频完成 | completetrackers | 视频播放完成  |
| 视频关闭 | closetrackers    | 视频被关闭   |
| 竞价胜出 | nurl             | 竞价成功通知  |
| 竞价失败 | burl             | 竞价失败通知  |

### 宏替换支持

```
${AUCTION_PRICE}    → 竞价价格
${AUCTION_BID_ID}   → 竞价ID
${AUCTION_ID}       → 拍卖ID
```

## 🎨 视图组件

### SplashAdView

开屏广告视图，支持图片和视频两种格式。

**主要方法：**

```kotlin
// 加载广告
fun loadAd(bid: Bid, listener: AdListener)

// 销毁资源
fun destroy()
```

**回调接口：**

```kotlin
interface AdListener {
    fun onAdShown()
    fun onAdClicked()
    fun onAdDismissed()
    fun onAdSkip()
    fun onAdFailed(msg: String)
}
```

## 🛠️ 工具类

### ZhixuanHelper

日志输出和统一管理。

```kotlin
ZhixuanHelper.logI("信息日志")
ZhixuanHelper.logE("错误日志", exception)
```

### DeviceHelper

设备信息采集。

```kotlin
DeviceHelper.getDeviceInfo(context)  // 获取完整设备信息
DeviceHelper.getScreenWidth(context)
DeviceHelper.getScreenHeight(context)
DeviceHelper.getOsVersion()
```

## 📱 最低要求

* Android API 21+ (Android 5.0)
* Kotlin 1.9.20+
* 网络权限：`INTERNET`
* 可选权限：`READ_PHONE_STATE`（用于设备信息采集）

## 🔒 混淆规则

```proguard
# Zhixuan SDK
-keep class com.maxrtb.zhixuan.** { *; }
-keep class com.maxrtb.zhixuan.api.model.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
```

## 📈 性能优化

* 图片加载使用Glide，支持缓存
* 视频使用MediaPlayer，优化内存占用
* 网络请求复用OkHttpClient
* 及时释放资源，避免内存泄漏

## 🐛 调试模式

```kotlin
// 开启日志
ZhixuanHelper.printLogEnable = true

// 查看详细请求日志
adb logcat | grep "Zhixuan\|okhttp"
```

## 📞 技术支持

* 技术文档：[docs.zhixuan.com](https://docs.zhixuan.com)
* API参考：[api.zhixuan.com/docs](https://api.zhixuan.com/docs)
* Issues: GitHub Issues

---

**Version: 1.0.0**
**Last Updated: 2025-10-24**

