package com.maxrtb.zxdm

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.listener.NativeListener
import com.maxrtb.zx.ZX

class NativeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad_test)

        val btnLoad = findViewById<Button>(R.id.btn_load_native)
        val container = findViewById<LinearLayout>(R.id.native_ad_container)
        val tvStatus = findViewById<TextView>(R.id.tv_native_status)

        val provider = ZX.getProvider()

        btnLoad.setOnClickListener {
            tvStatus.text = "⏳ 加载中..."
            provider.getNativeAdList(
                this,
                "zx",
                "native_test",
                3,
                object : NativeListener {
                    override fun onAdStartRequest(providerType: String) {
                        tvStatus.text = "开始请求..."
                    }

                    override fun onAdLoaded(providerType: String, adList: List<Any>) {
                        tvStatus.text = "✅ 加载成功，获得 ${adList.size} 个广告"
                        Toast.makeText(this@NativeActivity, "加载成功", Toast.LENGTH_SHORT).show()
                        
                        container.removeAllViews()
                        adList.forEachIndexed { index, ad ->
                            val view = TextView(this@NativeActivity).apply {
                                text = "广告 ${index + 1}: $ad"
                                textSize = 14f
                                setPadding(16, 16, 16, 16)
                            }
                            container.addView(view)
                        }
                    }

                    override fun onAdFailed(providerType: String, failedMsg: String?) {
                        tvStatus.text = "❌ 加载失败: $failedMsg"
                    }
                }
            )
        }
    }
}
