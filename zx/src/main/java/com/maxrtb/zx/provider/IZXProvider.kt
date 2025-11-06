// zx/src/main/java/com/maxrtb/zx/provider/IZXProvider.kt
package com.maxrtb.zx.provider

import android.app.Activity
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData

/**
 * ZX Provider 接口
 */
interface IZXProvider {

    /**
     * 初始化
     */
    suspend fun init()

    /**
     * 请求广告
     */
    suspend fun requestAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ): Result<AdData>

    /**
     * 展示广告
     */
    suspend fun showAd(activity: Activity): Result<Unit>

    /**
     * 销毁广告
     */
    suspend fun destroyAd()

    /**
     * 是否已准备好
     */
    fun isReady(): Boolean

    /**
     * 获取广告数据
     */
    fun getAdData(): AdData?
}
