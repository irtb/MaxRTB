package com.maxrtb.zx.utils

import android.util.Log

object ZXHelper {
    private const val TAG = "ZX_SDK"
    private var debugMode = false
    
    fun setDebugMode(debug: Boolean) {
        debugMode = debug
    }
    
    fun logI(tag: String = TAG, msg: String) {
        if (debugMode) {
            Log.i(tag, msg)
        }
    }
    
    fun logD(tag: String = TAG, msg: String) {
        if (debugMode) {
            Log.d(tag, msg)
        }
    }
    
    fun logW(tag: String = TAG, msg: String, throwable: Throwable? = null) {
        if (debugMode) {
            Log.w(tag, msg, throwable)
        }
    }
    
    fun logE(tag: String = TAG, msg: String, throwable: Throwable? = null) {
        Log.e(tag, msg, throwable)
    }
    
    fun generateRequestId(): String {
        return "zx_${System.currentTimeMillis()}_${(Math.random() * 10000).toInt()}"
    }
    
    fun isEmpty(str: String?): Boolean {
        return str == null || str.isEmpty() || str.isBlank()
    }
    
    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }
}
