# 消费者端 ProGuard 规则

# 保留 ZX SDK 的公开 API
-keep public class com.maxrtb.zx.ZX {
    public static *;
}

# 保留所有监听器接口
-keep public interface com.maxrtb.zx.listener.ZX* { *; }

# 保留配置类
-keep public class com.maxrtb.zx.config.ZXConfig { *; }

# 保留所有 Model 类
-keep public class com.maxrtb.zx.model.** { *; }
