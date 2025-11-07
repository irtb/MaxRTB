package com.maxrtb.zx.utils

import android.util.Log

/**
 * ZX工具类 - 参考zhixuan的实现
 */
object ZXHelper {
    private const val TAG = "ZX_SDK"
    var isDebug = true

    fun logD(tag: String = TAG, msg: String) {
        if (isDebug) Log.d(tag, msg)
    }

    fun logI(tag: String = TAG, msg: String) {
        if (isDebug) Log.i(tag, msg)
    }

    fun logE(tag: String = TAG, msg: String, throwable: Throwable? = null) {
        if (isDebug) {
            if (throwable != null) Log.e(tag, msg, throwable)
            else Log.e(tag, msg)
        }
    }
    
    fun logW(tag: String = TAG, msg: String) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }
    
    fun setDebugMode(debug: Boolean) {
        isDebug = debug
    }
}
