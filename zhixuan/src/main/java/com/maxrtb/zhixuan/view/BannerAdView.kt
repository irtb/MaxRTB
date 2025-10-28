package com.maxrtb.zhixuan.view

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class BannerAdView(context: Context) : FrameLayout(context) {
    
    private var listener: BannerListener? = null
    private var currentBid: Bid? = null
    
    interface BannerListener {
        fun onShown()
        fun onClicked()
        fun onClosed()
        fun onFailed(msg: String)
    }
    
    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setBackgroundColor(Color.parseColor("#F5F5F5"))
    }
    
    fun loadAd(bid: Bid, expectedWidth: Int, expectedHeight: Int, listener: BannerListener) {
        this.currentBid = bid
        this.listener = listener
        
        try {
            val imageUrl = bid.iurl
            if (imageUrl.isNullOrEmpty()) {
                listener.onFailed("无图片URL")
                return
            }
            
            ZhixuanHelper.logI("开始加载Banner图片: $imageUrl")
            
            // 创建 ImageView
            val imageView = ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            
            // 使用 Glide 加载图片，并在加载完成时计算高度
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)  // 获取原始尺寸
                .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                    override fun onResourceReady(
                        resource: android.graphics.drawable.Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in android.graphics.drawable.Drawable>?
                    ) {
                        try {
                            val imgWidth = resource.intrinsicWidth
                            val imgHeight = resource.intrinsicHeight
                            
                            ZhixuanHelper.logI("Banner 图片加载成功，尺寸: ${imgWidth}x${imgHeight}")
                            
                            // ========== 关键：正确计算 Banner 高度 ==========
                            val screenWidth = context.resources.displayMetrics.widthPixels
                            
                            // 计算实际显示高度：
                            // 如果图片宽度 > 屏幕宽度，则按比例缩小
                            val actualHeight = if (imgWidth > screenWidth) {
                                (imgHeight * screenWidth / imgWidth).toInt()
                            } else {
                                imgHeight
                            }
                            
                            // 但不能小于期望高度的一半
                            val minHeight = (expectedHeight * context.resources.displayMetrics.density).toInt() / 2
                            val finalHeight = maxOf(actualHeight, minHeight)
                            
                            ZhixuanHelper.logI("广告位尺寸: w=$screenWidth, h=$finalHeight (期望: ${expectedWidth}x${expectedHeight})")
                            ZhixuanHelper.logI("调整 Banner 高度: 广告尺寸=${imgWidth}x${imgHeight}, 屏幕宽度=$screenWidth, 计算高度=$finalHeight")
                            
                            // 设置正确的高度
                            imageView.apply {
                                layoutParams = LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    finalHeight
                                )
                                setImageDrawable(resource)
                            }
                            
                            // 更新 BannerAdView 的高度
                            this@BannerAdView.layoutParams = LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                finalHeight
                            )
                            
                            // 移除旧的 view，添加新的 view
                            this@BannerAdView.removeAllViews()
                            this@BannerAdView.addView(imageView)
                            
                            ZhixuanHelper.logI("Banner广告展示成功")
                            listener.onShown()
                            // =============================================
                            
                        } catch (e: Exception) {
                            ZhixuanHelper.logE("图片加载回调异常", e)
                            listener.onFailed(e.message ?: "加载失败")
                        }
                    }
                    
                    override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
                    
                    override fun onLoadFailed(errorDrawable: android.graphics.drawable.Drawable?) {
                        ZhixuanHelper.logE("Banner 图片加载失败")
                        listener.onFailed("图片加载失败")
                    }
                })
            
            // 设置点击事件
            setOnClickListener {
                listener.onClicked()
                bid.ext?.ldp?.let { url ->
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                        intent.data = android.net.Uri.parse(url)
                        context.startActivity(intent)
                        ZhixuanHelper.logI("打开 Banner 落地页: $url")
                    } catch (e: Exception) {
                        ZhixuanHelper.logE("打开落地页失败", e)
                    }
                }
            }
            
        } catch (e: Exception) {
            ZhixuanHelper.logE("Banner 加载异常", e)
            listener.onFailed(e.message ?: "加载失败")
        }
    }
}
