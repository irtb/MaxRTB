# ZX SDK ProGuard 配置

# 保留 ZX SDK 的所有类
-keep class com.maxrtb.zx.** { *; }
-keep interface com.maxrtb.zx.** { *; }
-keep enum com.maxrtb.zx.** { *; }

# 保留所有公开的类
-keep public class com.maxrtb.zx.** {
    public *;
}

# 保留监听器接口
-keep interface com.maxrtb.zx.listener.** { *; }

# 保留 Model 类
-keep class com.maxrtb.zx.model.** { *; }

# 保留注解
-keep @interface com.maxrtb.zx.** { *; }

# 保留内部类
-keepclasseswithmembernames class com.maxrtb.zx.** {
    native <methods>;
}

# 保留枚举
-keepclassmembers enum com.maxrtb.zx.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 不混淆数据模型
-keep class com.maxrtb.zx.model.** { !transient <fields>; }

# 保留 Kotlin 相关
-keep class kotlin.** { *; }
-keep interface kotlin.** { *; }

# Glide 配置
-keep public class com.bumptech.glide.** { *; }
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
