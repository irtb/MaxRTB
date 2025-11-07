package com.maxrtb.zxdm

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.listener.BannerListener
import com.maxrtb.zx.ZX

class BannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_test)

        val btnLoad = findViewById<Button>(R.id.btn_load)
        val container = findViewById<FrameLayout>(R.id.ad_container)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        btnLoad.setOnClickListener {
            loadBanner(container, tvStatus)
        }
    }

    private fun loadBanner(container: FrameLayout, tvStatus: TextView) {
        tvStatus.text = "⏳ 加载中..."
        Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show()

        val provider = ZX.getProvider()
        provider.showBannerAd(
            this,
            "zx",
            "banner_test",
            container,
            object : BannerListener {
                override fun onAdStartRequest(providerType: String) {
                    tvStatus.text = "开始请求..."
                }

                override fun onAdLoaded(providerType: String) {
                    tvStatus.text = "✅ 加载成功"
                    Toast.makeText(this@BannerActivity, "加载成功", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailed(providerType: String, failedMsg: String?) {
                    tvStatus.text = "❌ 加载失败: $failedMsg"
                    Toast.makeText(this@BannerActivity, "失败: $failedMsg", Toast.LENGTH_SHORT).show()
                }

                override fun onAdClicked(providerType: String) {
                    tvStatus.text = "👆 Banner 被点击"
                }

                override fun onAdExpose(providerType: String) {
                    tvStatus.text = "👁️ Banner 已曝光"
                }

                override fun onAdClose(providerType: String) {
                    tvStatus.text = "❌ Banner 已关闭"
                }
            }
        )
    }
}
