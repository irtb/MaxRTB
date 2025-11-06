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
