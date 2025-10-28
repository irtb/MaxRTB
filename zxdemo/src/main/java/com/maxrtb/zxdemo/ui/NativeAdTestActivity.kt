package com.maxrtb.zxdemo.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maxrtb.zxdemo.R
import com.maxrtb.zhixuan.native.ZhixuanNativeAdapter
import com.maxrtb.zhixuan.view.NativeAdCardView

class NativeAdTestActivity : AppCompatActivity() {

    private val TAG = "NativeAdTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_test)

        val btnLoad = findViewById<Button>(R.id.btn_load)
        val container = findViewById<LinearLayout>(R.id.ad_container)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        title = "🎯 原生广告"

        btnLoad.text = "📥 加载原生广告"
        btnLoad.setOnClickListener {
            loadNative(container, tvStatus)
        }
    }

    private fun loadNative(container: LinearLayout, tvStatus: TextView) {
        Log.d(TAG, "开始加载原生广告...")
        tvStatus.text = "⏳ 加载中..."
        Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show()

        val adapter = ZhixuanNativeAdapter(this, "native_test")

        adapter.loadAds(object : ZhixuanNativeAdapter.NativeAdListener {
            override fun onAdsLoaded(ads: List<Any>) {
                Log.i(TAG, "✅ 原生广告加载成功: ${ads.size} 个")
                tvStatus.text = "✅ 加载成功: ${ads.size} 个广告"
                Toast.makeText(this@NativeAdTestActivity, "加载成功", Toast.LENGTH_SHORT).show()

                container.removeAllViews()
                ads.forEach { ad ->
                    if (ad is NativeAdCardView) {
                        container.addView(ad)
                    }
                }
            }

            override fun onAdFailed(msg: String) {
                Log.e(TAG, "❌ 原生广告失败: $msg")
                tvStatus.text = "❌ 加载失败: $msg"
                Toast.makeText(this@NativeAdTestActivity, "失败: $msg", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
