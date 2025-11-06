package com.maxrtb.zx.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object ScreenUtil {
    fun getScreenWidth(context: Context): Int = getDisplayMetrics(context).widthPixels
    fun getScreenHeight(context: Context): Int = getDisplayMetrics(context).heightPixels
    fun getScreenDpi(context: Context): Int = getDisplayMetrics(context).densityDpi
    fun getScreenDensity(context: Context): Float = getDisplayMetrics(context).density
    
    fun dpToPx(context: Context, dp: Float): Int {
        return (dp * getScreenDensity(context) + 0.5f).toInt()
    }
    
    fun pxToDp(context: Context, px: Float): Int {
        return (px / getScreenDensity(context) + 0.5f).toInt()
    }
    
    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm
    }
}
