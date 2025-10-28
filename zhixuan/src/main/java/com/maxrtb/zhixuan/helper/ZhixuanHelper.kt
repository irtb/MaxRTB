package com.maxrtb.zhixuan.helper

import android.util.Log

object ZhixuanHelper {
    private const val TAG = "Zhixuan"
    var isDebug = true

    fun logD(msg: String) {
        if (isDebug) Log.d(TAG, msg)
    }

    fun logI(msg: String) {
        if (isDebug) Log.i(TAG, msg)
    }

    fun logE(msg: String, throwable: Throwable? = null) {
        if (isDebug) {
            if (throwable != null) Log.e(TAG, msg, throwable)
            else Log.e(TAG, msg)
        }
    }
    
    fun logW(msg: String) {
        if (isDebug) {
            Log.w(TAG, msg)
        }
    }
}
