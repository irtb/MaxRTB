package com.maxrtb.zx.exception

/**
 * 智选SDK基础异常
 */
open class ZXException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * 网络异常
 */
class NetworkException(
    message: String,
    val errorCode: Int = -1,
    cause: Throwable? = null
) : ZXException(message, cause)

/**
 * 广告异常
 */
class AdException(
    message: String,
    val slotId: String? = null,
    cause: Throwable? = null
) : ZXException(message, cause)

/**
 * 配置异常
 */
class ConfigException(
    message: String,
    cause: Throwable? = null
) : ZXException(message, cause)
