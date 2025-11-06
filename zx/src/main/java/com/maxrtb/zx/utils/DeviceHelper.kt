package com.maxrtb.zx.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.maxrtb.zx.model.Device

object DeviceHelper {
    @SuppressLint("MissingPermission")
    fun getDeviceInfo(context: Context): Device {
        return Device(
            deviceId = getDeviceId(context),
            androidId = getAndroidId(context),
            model = getModel(),
            brand = getBrand(),
            osVersion = getOsVersion(),
            screenWidth = ScreenUtil.getScreenWidth(context),
            screenHeight = ScreenUtil.getScreenHeight(context),
            screenDpi = ScreenUtil.getScreenDpi(context),
            networkType = getNetworkType(context),
            carrier = getCarrier(context),
            language = getLanguage(),
            timezone = getTimezone(),
        )
    }
    
    @SuppressLint("MissingPermission")
    private fun getDeviceId(context: Context): String {
        return try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.deviceId ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    
    private fun getAndroidId(context: Context): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    
    private fun getModel(): String = Build.MODEL ?: ""
    private fun getBrand(): String = Build.BRAND ?: ""
    private fun getOsVersion(): String = Build.VERSION.RELEASE ?: ""
    
    @SuppressLint("MissingPermission")
    private fun getNetworkType(context: Context): String {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            when (activeNetwork?.type) {
                ConnectivityManager.TYPE_WIFI -> "wifi"
                ConnectivityManager.TYPE_MOBILE -> {
                    when (activeNetwork.subtype) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE,
                        TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT,
                        TelephonyManager.NETWORK_TYPE_IDEN -> "2g"
                        TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0,
                        TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA,
                        TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA,
                        TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD,
                        TelephonyManager.NETWORK_TYPE_HSPAP -> "3g"
                        TelephonyManager.NETWORK_TYPE_LTE -> "4g"
                        TelephonyManager.NETWORK_TYPE_NR -> "5g"
                        else -> "unknown"
                    }
                }
                else -> "unknown"
            }
        } catch (e: Exception) {
            "unknown"
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun getCarrier(context: Context): String {
        return try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.networkOperatorName ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    
    private fun getLanguage(): String = java.util.Locale.getDefault().language ?: "unknown"
    private fun getTimezone(): String = java.util.TimeZone.getDefault().id ?: "unknown"
}
