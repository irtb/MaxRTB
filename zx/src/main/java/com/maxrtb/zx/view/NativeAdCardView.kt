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
        
        // 创建 LinearLayout 容器
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        
        iconView = ImageView(context).apply { 
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        container.addView(iconView)
        
        titleView = TextView(context).apply { textSize = 16f }
        container.addView(titleView)
        
        descView = TextView(context).apply { textSize = 12f }
        container.addView(descView)
        
        ctaButton = Button(context).apply {
            text = "下载"
            setOnClickListener { onAdClicked() }
        }
        container.addView(ctaButton)
        
        addView(container)
    }

    override fun onAdDataReady(data: AdData) {
        super.onAdDataReady(data)
        titleView?.text = data.title
        descView?.text = data.desc
        ctaButton?.text = data.ctaText
    }
}
