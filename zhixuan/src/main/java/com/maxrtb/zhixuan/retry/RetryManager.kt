package com.maxrtb.zhixuan.retry

import com.maxrtb.zhixuan.helper.ZhixuanHelper
import kotlinx.coroutines.delay

object RetryManager {
    
    data class RetryConfig(
        val maxRetries: Int = 3,
        val initialDelay: Long = 1000L,
        val maxDelay: Long = 5000L,
        val backoffMultiplier: Float = 2f
    )
    
    interface RetryCallback<T> {
        suspend fun execute(): T
        fun onRetry(attempt: Int, nextDelay: Long)
        fun onSuccess(result: T)
        fun onFailed(error: Throwable)
    }
    
    suspend fun <T> executeWithRetry(
        config: RetryConfig = RetryConfig(),
        callback: RetryCallback<T>
    ): T? {
        var currentDelay = config.initialDelay
        var lastException: Throwable? = null
        
        repeat(config.maxRetries) { attempt ->
            try {
                ZhixuanHelper.logI("执行请求，第${attempt + 1}次尝试")
                val result = callback.execute()
                callback.onSuccess(result)
                return result
            } catch (e: Exception) {
                lastException = e
                ZhixuanHelper.logE("第${attempt + 1}次请求失败", e)
                
                if (attempt < config.maxRetries - 1) {
                    val nextDelay = minOf(currentDelay, config.maxDelay)
                    callback.onRetry(attempt + 1, nextDelay)
                    
                    ZhixuanHelper.logI("将在${nextDelay}ms后重试")
                    delay(nextDelay)
                    
                    currentDelay = (currentDelay * config.backoffMultiplier).toLong()
                }
            }
        }
        
        callback.onFailed(lastException ?: Exception("Unknown error"))
        return null
    }
    
    suspend fun <T> retry(
        times: Int = 3,
        initialDelay: Long = 1000L,
        factor: Float = 2f,
        maxDelay: Long = 5000L,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                ZhixuanHelper.logE("重试失败 (${attempt + 1}/$times)", e)
                delay(currentDelay)
                currentDelay = minOf((currentDelay * factor).toLong(), maxDelay)
            }
        }
        return block()
    }
}
