package com.maxrtb.zhixuan.helper

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebSettings
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.NetworkInterface
import java.util.*

class DeviceHelper(private val activity: Activity) {

    fun getScreenWidth(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.systemBars()
            )
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            @Suppress("DEPRECATION")
            val displayMetrics = activity.resources.displayMetrics
            displayMetrics.widthPixels
        }
    }

    fun getScreenHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.systemBars()
            )
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            @Suppress("DEPRECATION")
            val displayMetrics = activity.resources.displayMetrics
            displayMetrics.heightPixels
        }
    }

    fun getUserAgent(): String {
        return try {
            WebSettings.getDefaultUserAgent(activity)
        } catch (e: Exception) {
            "Mozilla/5.0 (Linux; Android ${Build.VERSION.RELEASE})"
        }
    }

    fun getPackageName(): String = activity.packageName

    fun getAppName(): String {
        return try {
            val pm = activity.packageManager
            val info = pm.getApplicationInfo(activity.packageName, 0)
            pm.getApplicationLabel(info).toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun getAppVersion(): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                activity.packageManager.getPackageInfo(
                    activity.packageName,
                    android.content.pm.PackageManager.PackageInfoFlags.of(0)
                ).versionName
            } else {
                @Suppress("DEPRECATION")
                activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
            }
        } catch (e: Exception) {
            "1.0"
        }
    }

    fun getManufacturer(): String = Build.MANUFACTURER
    fun getModel(): String = Build.MODEL
    fun getOsVersion(): String = Build.VERSION.RELEASE
    fun getLanguage(): String = Locale.getDefault().language

    fun getGaid(): String? {
        return try {
            runBlocking {
                withContext(Dispatchers.IO) {
                    AdvertisingIdClient.getAdvertisingIdInfo(activity).id
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val iface = interfaces.nextElement()
                val addresses = iface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val addr = addresses.nextElement()
                    if (!addr.isLoopbackAddress && addr.address.size == 4) {
                        return addr.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
        }
        return null
    }
}
