# API 参考文档

## TogetherAd 核心API

### 初始化配置

```kotlin
// 添加广告提供商
TogetherAd.addProvider(provider: AdProviderEntity)

// 全局日志开关
TogetherAd.printLogEnable: Boolean

// 失败自动切换
TogetherAd.failedSwitchEnable: Boolean

// 设置权重比例
TogetherAd.setPublicProviderRatio(ratioMap: LinkedHashMap<String, Int>)
````

## ZhixuanProvider API

### 开屏广告

```kotlin
fun loadAndShowSplashAd(
    activity: Activity,
    adProviderType: String,
    alias: String,
    container: ViewGroup,
    listener: SplashListener
)
```

**参数说明：**

| 参数             | 类型             | 说明                |
| -------------- | -------------- | ----------------- |
| activity       | Activity       | 当前Activity        |
| adProviderType | String         | 提供商类型，固定"zhixuan" |
| alias          | String         | 广告位ID             |
| container      | ViewGroup      | 广告容器              |
| listener       | SplashListener | 回调监听器             |

### SplashListener 回调

```kotlin
interface SplashListener {
    fun onAdLoaded(providerType: String)
    fun onAdFailed(providerType: String, failedMsg: String?)
    fun onAdClicked(providerType: String)
    fun onAdExposure(providerType: String)
    fun onAdDismissed(providerType: String)
}
```

## 数据模型

### BidRequest

```kotlin
data class BidRequest(
    val id: String,
    val app: App,
    val device: Device,
    val imp: List<Imp>,
    val test: Int = 0,
    val tmax: Int = 5000
)
```

### BidResponse

```kotlin
data class BidResponse(
    val id: String,
    val bidid: String?,
    val seatbid: List<SeatBid>?,
    val cur: String?,
    val nbr: Int? = null
)
```

### Bid

```kotlin
data class Bid(
    val id: String,
    val impid: String,
    val price: Double,
    val iurl: String?,
    val adid: String?,
    val w: Int?,
    val h: Int?,
    val nurl: String?,
    val burl: String?,
    val ext: BidExt?
)
```

### BidExt

```kotlin
data class BidExt(
    val ldp: String?,
    val imptrackers: List<String>?,
    val clicktrackers: List<String>?,
    val native: NativeExt?,
    val video: VideoExt?
)
```

## 工具类API

### DeviceHelper

```kotlin
object DeviceHelper {
    fun getDeviceInfo(context: Context): Device
    fun getScreenWidth(context: Context): Int
    fun getScreenHeight(context: Context): Int
    fun getOsVersion(): String
    fun getMake(): String
    fun getModel(): String
}
```

### ZhixuanHelper

```kotlin
object ZhixuanHelper {
    var printLogEnable: Boolean
    fun logI(msg: String)
    fun logE(msg: String, throwable: Throwable? = null)
}
```

## 监测上报API

### AdTracker

```kotlin
object AdTracker {
    fun trackImpression(urls: List<String>, context: Context)
    fun trackClick(urls: List<String>, context: Context)
    fun trackVideoStart(urls: List<String>, context: Context)
    fun trackVideoComplete(urls: List<String>, context: Context)
}
```

## 常量定义

```kotlin
// 广告位类型
const val AD_TYPE_SPLASH = 7  // 开屏
const val AD_TYPE_BANNER = 1  // Banner
const val AD_TYPE_INTERSTITIAL = 3  // 插屏

// 设备类型
const val DEVICE_TYPE_MOBILE = 4  // 手机
const val DEVICE_TYPE_TABLET = 5  // 平板

// 协议版本
const val OPENRTB_VERSION = "2.5"
```

---

**相关文档：** [集成指南](INTEGRATION.md) | [常见问题](FAQ.md)