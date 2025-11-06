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
        
        // 创建 LinearLayout 容器
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        
        titleView = TextView(context).apply { textSize = 18f }
        container.addView(titleView)
        
        descView = TextView(context).apply { textSize = 14f }
        container.addView(descView)
        
        priceView = TextView(context).apply { textSize = 16f }
        container.addView(priceView)
        
        ratingBar = RatingBar(context)
        container.addView(ratingBar)
        
        ctaButton = Button(context).apply {
            text = "查看详情"
            setOnClickListener { onAdClicked() }
        }
        container.addView(ctaButton)
        
        addView(container)
    }

    override fun onAdDataReady(data: AdData) {
        super.onAdDataReady(data)
        titleView?.text = data.title
        descView?.text = data.desc
        priceView?.text = data.price
        ctaButton?.text = data.ctaText
    }
}
