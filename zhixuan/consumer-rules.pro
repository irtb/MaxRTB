# 智选广告混淆规则

# 保留数据模型类
-keep class com.maxrtb.zhixuan.api.model.** { *; }

# 保留Retrofit接口
-keep interface com.maxrtb.zhixuan.api.ZhixuanApiService { *; }

# 保留Provider
-keep class com.maxrtb.zhixuan.provider.ZhixuanProvider { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
