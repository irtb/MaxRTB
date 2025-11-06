#!/bin/bash

cd /Users/mac/data/android/MaxRTB/zx/src/main/java/com/maxrtb/zx

# ===== 创建 View 文件 =====
mkdir -p view

cat > view/BannerAdView.kt << 'EOF'
package com.maxrtb.zx.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.maxrtb.zx.base.BaseAdView
import com.maxrtb.zx.model.AdData

class BannerAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseAdView(context, attrs, defStyleAttr) {

    private var titleView: TextView? = null
    private var imageView: ImageView? = null

    override fun onViewInit() {
        super.onViewInit()
        val rootView = LayoutInflater.from(context).inflate(
            android.R.layout.simple_list_item_1, this, false
        )
        addView(rootView)
        titleView = rootView.findViewById(android.R.id.text1)
    }

    override fun onAdDataReady(data: AdData) {
        super.onAdDataReady(data)
        titleView?.text = data.title
    }
}
EOF

cat > view/SplashAdView.kt << 'EOF'
package com.maxrtb.zx.view

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.maxrtb.zx.base.BaseAdView
import com.maxrtb.zx.listener.ZXSplashListener
import com.maxrtb.zx.model.AdData

class SplashAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseAdView(context, attrs, defStyleAttr) {

    private var titleView: TextView? = null
    private var skipButton: Button? = null
    private var countdownTimer: CountDownTimer? = null
    private var remainTime = 5000L

    override fun onViewInit() {
        super.onViewInit()
        setBackgroundColor(android.graphics.Color.WHITE)
        titleView = TextView(context).apply { text = "启屏广告" }
        addView(titleView)
        skipButton = Button(context).apply {
            text = "跳过"
            setOnClickListener {
                (listener as? ZXSplashListener)?.onSkipClicked()
                onAdClosed()
            }
        }
        addView(skipButton)
    }

    override fun onAdDataReady(data: AdData) {
        super.onAdDataReady(data)
        titleView?.text = data.title
    }

    override fun showAd() {
        super.showAd()
        startCountdown()
    }

    private fun startCountdown() {
        countdownTimer = object : CountDownTimer(remainTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainTime = millisUntilFinished
                (listener as? ZXSplashListener)?.onCountdown(millisUntilFinished)
                skipButton?.text = "跳过(${millisUntilFinished / 1000})"
            }
            override fun onFinish() {
                skipButton?.text = "进入"
                onAdClosed()
            }
        }.start()
    }

    override fun onDestroy() {
        countdownTimer?.cancel()
        super.onDestroy()
    }
}
EOF

cat > view/InterstitialDialog.kt << 'EOF'
package com.maxrtb.zx.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData

class InterstitialDialog(
    context: Context,
    private val adData: AdData,
    private val listener: ZXBaseListener,
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titleView = TextView(context).apply { text = adData.title }
        val closeButton = Button(context).apply {
            text = "关闭"
            setOnClickListener {
                listener.onAdClosed()
                dismiss()
            }
        }
        val container = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(titleView)
            addView(closeButton)
        }
        setContentView(container)
    }
}
EOF

cat > view/RewardedVideoDialog.kt << 'EOF'
package com.maxrtb.zx.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.maxrtb.zx.listener.ZXRewardListener
import com.maxrtb.zx.model.AdData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RewardedVideoDialog(
    context: Context,
    private val adData: AdData,
    private val listener: ZXRewardListener,
) : Dialog(context) {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var videoDuration = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titleView = TextView(context).apply { text = adData.title }
        val progressBar = ProgressBar(context).apply { max = videoDuration }
        val timeView = TextView(context).apply { text = "0/$videoDuration" }
        val closeButton = Button(context).apply {
            text = "关闭"
            isEnabled = false
            setOnClickListener {
                listener.onAdClosed()
                dismiss()
            }
        }
        val container = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(titleView)
            addView(progressBar)
            addView(timeView)
            addView(closeButton)
        }
        setContentView(container)
        scope.launch {
            listener.onVideoStart()
            for (i in 0..videoDuration) {
                progressBar.progress = i
                timeView.text = "$i/$videoDuration"
                listener.onVideoProgress(i, videoDuration)
                delay(1000)
            }
            listener.onVideoComplete()
            listener.onRewarded()
            closeButton.isEnabled = true
            closeButton.text = "获得奖励"
        }
    }

    override fun dismiss() {
        scope.cancel()
        super.dismiss()
    }
}
EOF

cat > view/NativeAdView.kt << 'EOF'
package com.maxrtb.zx.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.maxrtb.zx.base.BaseAdView
import com.maxrtb.zx.model.AdData

class NativeAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseAdView(context, attrs, defStyleAttr) {

    private var titleView: TextView? = null
    private var descView: TextView? = null
    private var priceView: TextView? = null
    private var ratingBar: RatingBar? = null
    private var ctaButton: Button? = null

    override fun onViewInit() {
        super.onViewInit()
        orientation = LinearLayout.VERTICAL
        titleView = TextView(context).apply { textSize = 18f }
        addView(titleView)
        descView = TextView(context).apply { textSize = 14f }
        addView(descView)
        priceView = TextView(context).apply { textSize = 16f }
        addView(priceView)
        ratingBar = RatingBar(context)
        addView(ratingBar)
        ctaButton = Button(context).apply {
            text = "查看详情"
            setOnClickListener { onAdClicked() }
        }
        addView(ctaButton)
    }

    override fun onAdDataReady(data: AdData) {
        super.onAdDataReady(data)
        titleView?.text = data.title
        descView?.text = data.desc
        priceView?.text = data.price
        ctaButton?.text = data.ctaText
    }
}
EOF

cat > view/NativeAdCardView.kt << 'EOF'
package com.maxrtb.zx.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.maxrtb.zx.base.BaseAdView
import com.maxrtb.zx.model.AdData

class NativeAdCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseAdView(context, attrs, defStyleAttr) {

    private var iconView: ImageView? = null
    private var titleView: TextView? = null
    private var descView: TextView? = null
    private var ctaButton: Button? = null

    override fun onViewInit() {
        super.onViewInit()
        orientation = LinearLayout.VERTICAL
        iconView = ImageView(context).apply { scaleType = ImageView.ScaleType.CENTER_CROP }
        addView(iconView)
        titleView = TextView(context).apply { textSize = 16f }
        addView(titleView)
        descView = TextView(context).apply { textSize = 12f }
        addView(descView)
        ctaButton = Button(context).apply {
            text = "下载"
            setOnClickListener { onAdClicked() }
        }
        addView(ctaButton)
    }

    override fun onAdDataReady(data: AdData) {
        super.onAdDataReady(data)
        titleView?.text = data.title
        descView?.text = data.desc
        ctaButton?.text = data.ctaText
    }
}
EOF

# ===== 创建主入口 =====
cat > ZX.kt << 'EOF'
package com.maxrtb.zx

import android.app.Activity
import android.content.Context
import com.maxrtb.zx.config.ZXConfig
import com.maxrtb.zx.config.ZXConfigManager
import com.maxrtb.zx.helper.ZXHelper
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.provider.ZXProvider

object ZX {
    private const val TAG = "ZX_SDK"
    private var isInitialized = false

    fun init(context: Context, config: ZXConfig = ZXConfig()) {
        if (isInitialized) {
            ZXHelper.logW(TAG, "ZX SDK already initialized")
            return
        }
        ZXConfigManager.init(context, config)
        ZXHelper.setDebugMode(config.debugMode)
        ZXHelper.logI(TAG, "ZX SDK initialized")
        isInitialized = true
    }

    suspend fun requestBannerAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getBannerProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestRewardAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getRewardProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestInterstitialAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getInterstitialProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestNativeAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getNativeProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestSplashAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getSplashProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestAd(
        activity: Activity,
        adType: String,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getProvider(adType)
            ?: throw IllegalArgumentException("Unsupported ad type: $adType")
        provider.requestAd(activity, slotId, listener)
    }

    fun getConfig(): ZXConfig {
        checkInitialized()
        return ZXConfigManager.getConfig()
    }

    fun isInitialized(): Boolean = isInitialized

    private fun checkInitialized() {
        require(isInitialized) { "ZX SDK not initialized. Call ZX.init() first." }
    }
}
EOF

# ===== 修复 ZXProviderBanner =====
cd provider
rm -f ZXProviderBanner.kt

cat > ZXProviderBanner.kt << 'EOF'
package com.maxrtb.zx.provider

import android.app.Activity
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData

class ZXProviderBanner : BaseZXProvider() {
    override suspend fun requestAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ): Result<AdData> {
        this.listener = listener
        return try {
            adData = AdData(
                adId = "banner_${System.currentTimeMillis()}",
                adName = "Banner Ad",
                adType = "banner",
                title = "Banner 广告",
                desc = "这是一个 Banner 广告",
                imageUrl = "https://via.placeholder.com/320x50",
                landingPageUrl = "https://example.com",
            )
            onAdLoaded()
            Result.success(adData!!)
        } catch (e: Exception) {
            onAdLoadFailed(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    override suspend fun showAd(activity: Activity): Result<Unit> {
        return try {
            if (adData == null) throw IllegalStateException("Ad data is null")
            onAdShown()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun destroyAd() {
        adData = null
    }
}
EOF

cd ..

echo "✅ 所有 View 和主入口文件已创建"

# 编译测试
cd /Users/mac/data/android/MaxRTB
./gradlew clean
./gradlew build -x test

echo "✅ 编译完成！"
