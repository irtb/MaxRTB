package com.maxrtb.zx.utils

object MacroReplacer {
    enum class Macro(val value: String) {
        TIMESTAMP("{TIMESTAMP}"),
        REQUEST_ID("{REQUEST_ID}"),
        CLICK_ID("{CLICK_ID}"),
        DEVICE_ID("{DEVICE_ID}"),
        AD_ID("{AD_ID}"),
        RANDOM("{RANDOM}"),
    }
    
    fun replaceMacro(
        url: String,
        requestId: String = "",
        clickId: String = "",
        deviceId: String = "",
        adId: String = "",
    ): String {
        var result = url
        result = result.replace(Macro.TIMESTAMP.value, System.currentTimeMillis().toString())
        if (requestId.isNotEmpty()) {
            result = result.replace(Macro.REQUEST_ID.value, requestId)
        }
        if (clickId.isNotEmpty()) {
            result = result.replace(Macro.CLICK_ID.value, clickId)
        }
        if (deviceId.isNotEmpty()) {
            result = result.replace(Macro.DEVICE_ID.value, deviceId)
        }
        if (adId.isNotEmpty()) {
            result = result.replace(Macro.AD_ID.value, adId)
        }
        result = result.replace(Macro.RANDOM.value, (Math.random() * 10000).toInt().toString())
        return result
    }
}
