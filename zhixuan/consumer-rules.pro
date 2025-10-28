# Zhixuan SDK Consumer ProGuard Rules
# 这些规则会自动应用到使用此SDK的应用中

# 保留SDK核心类
-keep class com.maxrtb.zhixuan.provider.ZhixuanProvider { *; }
-keep class com.maxrtb.zhixuan.api.model.** { *; }

# 保留接口
-keep interface com.maxrtb.zhixuan.** { *; }

# 数据模型序列化
-keepattributes Signature
-keepattributes *Annotation*
