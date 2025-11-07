package com.maxrtb.zxdm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.listener.InterListener
import com.maxrtb.zx.ZX

class InterstitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interstitial_test)

        val btnLoad = findViewById<Button>(R.id.btn_load_inter)
        val btnShow = findViewById<Button>(R.id.btn_show_inter)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        val provider = ZX.getProvider()

        btnLoad.setOnClickListener {
            tvStatus.text = "⏳ 加载中..."
            provider.requestInterAd(
                this,
                "zx",
                "inter_test",
                object : InterListener {
                    override fun onAdStartRequest(providerType: String) {
                        tvStatus.text = "开始请求..."
                    }

                    override fun onAdLoaded(providerType: String) {
                        tvStatus.text = "✅ 加载成功"
                        Toast.makeText(this@InterstitialActivity, "加载成功", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAdFailed(providerType: String, failedMsg: String?) {
                        tvStatus.text = "❌ 加载失败: $failedMsg"
                    }

                    override fun onAdClicked(providerType: String) {}
                    override fun onAdExpose(providerType: String) {}
                    override fun onAdClose(providerType: String) {}
                }
            )
        }

        btnShow.setOnClickListener {
            provider.showInterAd(this)
            tvStatus.text = "🎬 正在展示..."
        }
    }
}
