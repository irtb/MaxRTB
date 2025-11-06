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
