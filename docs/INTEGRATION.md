# 集成指南

## 前置条件

- Android Studio Arctic Fox+
- Gradle 8.0+
- Android SDK API 21+
- Kotlin 1.9.0+

## 第一步：添加依赖

### 1.1 项目级 build.gradle

```gradle
buildscript {
   ext.kotlin_version = '1.9.20'
   repositories {
       google()
       mavenCentral()
   }
   dependencies {
       classpath 'com.android.tools.build:gradle:8.2.0'
       classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
   }
}
````

### 1.2 模块级 build.gradle

```gradle
dependencies {
    // MaxRTB SDK
    implementation project(':core')
    implementation project(':zhixuan')
    
    // 或使用AAR包
    // implementation files('libs/maxrtb-core-1.0.0.aar')
    // implementation files('libs/zhixuan-1.0.0.aar')
    
    // 必需依赖
    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'
    implementation 'com.github.bumptech.glide:glide:4.16.0'
}
```

## 第二步：配置权限

### AndroidManifest.xml

```xml
<!-- 必需权限 -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- 可选权限 -->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```

## 第三步：初始化SDK

### 3.1 创建 Application 类

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initMaxRTB()
    }
    
    private fun initMaxRTB() {
        // 1. 注册广告提供商
        val zhixuanProvider = AdProviderEntity(
            providerType = "zhixuan",
            classPath = "com.maxrtb.zhixuan.provider.ZhixuanProvider",
            desc = "智选广告"
        )
        TogetherAd.addProvider(zhixuanProvider)
        
        // 2. 全局配置
        TogetherAd.printLogEnable = BuildConfig.DEBUG
        TogetherAd.failedSwitchEnable = false
        
        // 3. 设置权重
        val ratioMap = linkedMapOf<String, Int>()
        ratioMap["zhixuan"] = 1
        TogetherAd.setPublicProviderRatio(ratioMap)
    }
}
```

### 3.2 注册 Application

```xml
<application
    android:name=".MyApplication"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name">
    ...
</application>
```

## 第四步：接入开屏广告

### 4.1 布局文件

```xml
<!-- activity_splash.xml -->
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/splash_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white" />
```

### 4.2 Activity 代码

```kotlin
class SplashActivity : AppCompatActivity() {
    private val provider = ZhixuanProvider()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        loadSplashAd()
    }
    
    private fun loadSplashAd() {
        val container = findViewById<FrameLayout>(R.id.splash_container)
        
        provider.loadAndShowSplashAd(
            activity = this,
            adProviderType = "zhixuan",
            alias = "splash_001", // 广告位ID
            container = container,
            listener = object : SplashListener {
                override fun onAdLoaded(providerType: String) {
                    Log.d(TAG, "广告加载成功")
                }
                
                override fun onAdFailed(providerType: String, failedMsg: String?) {
                    Log.e(TAG, "广告失败: $failedMsg")
                    goToMain()
                }
                
                override fun onAdClicked(providerType: String) {
                    Log.d(TAG, "广告点击")
                }
                
                override fun onAdDismissed(providerType: String) {
                    Log.d(TAG, "广告关闭")
                    goToMain()
                }
                
                override fun onAdExposure(providerType: String) {
                    Log.d(TAG, "广告曝光")
                }
            }
        )
    }
    
    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
```

## 第五步：混淆配置

### proguard-rules.pro

```proguard
# MaxRTB Core
-keep class com.maxrtb.togetheradcore.** { *; }

# Zhixuan SDK
-keep class com.maxrtb.zhixuan.** { *; }
-keep class com.maxrtb.zhixuan.api.model.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# OkHttp & Okio
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
```

## 测试验证

### 启用日志

```kotlin
TogetherAd.printLogEnable = true
ZhixuanHelper.printLogEnable = true
```

### 查看日志

```bash
adb logcat | grep -E "TogetherAd|Zhixuan|okhttp"
```

### 预期输出

```
I/Zhixuan: 开始请求广告，alias=splash_001
I/okhttp: --> POST https://api.zhixuan.com/api/v1/bid
I/okhttp: <-- 200 https://api.zhixuan.com/api/v1/bid (234ms)
I/Zhixuan: 广告请求成功: bidId=bid_xxx, price=5.0
I/Zhixuan: 图片开屏广告展示成功
```

## 常见问题

**Q: 广告请求失败？**
A: 检查网络权限、BASE\_URL配置、广告位ID是否正确

**Q: 图片/视频不显示？**
A: 检查素材URL是否有效、网络连接、Glide初始化

**Q: 内存泄漏？**
A: 确保在Activity销毁时调用 `provider.destroy()`