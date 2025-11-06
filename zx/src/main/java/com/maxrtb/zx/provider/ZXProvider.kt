// zx/src/main/java/com/maxrtb/zx/provider/ZXProvider.kt
package com.maxrtb.zx.provider

/**
 * ZX Provider 管理器
 */
object ZXProvider {

    private val bannerProvider = ZXProviderBanner()
    private val rewardProvider = ZXProviderReward()
    private val interstitialProvider = ZXProviderInterstitial()
    private val nativeProvider = ZXProviderNative()
    private val splashProvider = ZXProviderSplash()

    /**
     * 获取 Banner Provider
     */
    fun getBannerProvider(): IZXProvider = bannerProvider

    /**
     * 获取激励视频 Provider
     */
    fun getRewardProvider(): IZXProvider = rewardProvider

    /**
     * 获取插屏 Provider
     */
    fun getInterstitialProvider(): IZXProvider = interstitialProvider

    /**
     * 获取原生 Provider
     */
    fun getNativeProvider(): IZXProvider = nativeProvider

    /**
     * 获取启屏 Provider
     */
    fun getSplashProvider(): IZXProvider = splashProvider

    /**
     * 根据类型获取 Provider
     */
    fun getProvider(adType: String): IZXProvider? {
        return when (adType) {
            "banner" -> bannerProvider
            "reward" -> rewardProvider
            "interstitial" -> interstitialProvider
            "native" -> nativeProvider
            "splash" -> splashProvider
            else -> null
        }
    }
}
