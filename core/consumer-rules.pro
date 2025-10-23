# Consumer ProGuard rules for core module
# 这些规则会自动应用到依赖core的模块

# 保持TogetherAd核心类
-keep class com.ifmvo.togetherad.core.** { *; }
-keep interface com.ifmvo.togetherad.core.** { *; }

# 保持广告回调接口
-keepclassmembers class * {
    public void onAd*(...);
}
