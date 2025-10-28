package com.maxrtb.zxdemo.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maxrtb.zxdemo.R
import com.maxrtb.zhixuan.bannerad.ZhixuanBannerAdapter

class BannerTestActivity : AppCompatActivity() {

    private val TAG = "BannerTest"
    private var adapter: ZhixuanBannerAdapter? = null

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
        Log.d(TAG, "开始加载 Banner 广告...")
        tvStatus.text = "⏳ 加载中..."
        Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show()

        adapter = ZhixuanBannerAdapter(this, "banner_test")

        adapter?.loadAd(320, 50, object : ZhixuanBannerAdapter.BannerAdListener {
            override fun onAdLoaded() {
                Log.i(TAG, "✅ Banner 加载成功")
                tvStatus.text = "✅ 加载成功"
                Toast.makeText(this@BannerTestActivity, "加载成功", Toast.LENGTH_SHORT).show()
                
                adapter?.getView()?.let {
                    container.removeAllViews()
                    container.addView(it)
                }
            }

            override fun onAdShown() {
                Log.i(TAG, "👁️ Banner 展示")
                tvStatus.text = "👁️ Banner 已展示"
            }

            override fun onAdClicked() {
                Log.i(TAG, "👆 Banner 点击")
            }

            override fun onAdClosed() {
                Log.i(TAG, "❌ Banner 关闭")
                tvStatus.text = "❌ Banner 已关闭"
            }

            override fun onAdFailed(msg: String) {
                Log.e(TAG, "❌ Banner 失败: $msg")
                tvStatus.text = "❌ 加载失败: $msg"
                Toast.makeText(this@BannerTestActivity, "失败: $msg", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.destroy()
    }
}
