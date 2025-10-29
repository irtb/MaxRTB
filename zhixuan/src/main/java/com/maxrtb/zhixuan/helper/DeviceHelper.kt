package com.maxrtb.zhixuan.helper

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import com.maxrtb.zhixuan.model.Device
import java.net.NetworkInterface
import java.util.*

object DeviceHelper {
    
    fun getDeviceInfo(context: Context): Device {
        return Device(
            os = "Android",
            osv = Build.VERSION.RELEASE,
            make = Build.MANUFACTURER,
            model = Build.MODEL,
            devicetype = 4,
            w = getScreenWidth(context),
            h = getScreenHeight(context),
            ip = getIpAddress(),
            ua = System.getProperty("http.agent") ?: "",
            language = Locale.getDefault().language,
            dnt = 0,
            lmt = 0
        )
    }
    
    private fun getScreenWidth(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.widthPixels
    }
    
    private fun getScreenHeight(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.heightPixels
    }
    
    private fun getIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address.hostAddress.indexOf(':') < 0) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
