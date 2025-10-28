# Zhixuan SDK 混淆规则
# Add project specific ProGuard rules here.

# 保留SDK所有公开API
-keep public class com.maxrtb.zhixuan.provider.ZhixuanProvider { *; }

# 保留数据模型
-keep class com.maxrtb.zhixuan.api.model.** { *; }

# 保留监听器接口
-keep interface com.maxrtb.zhixuan.** { *; }

# Gson 序列化
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# 保留泛型
-keepattributes Signature
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
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
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
    *** rewind();
}

# 避免混淆日志
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
