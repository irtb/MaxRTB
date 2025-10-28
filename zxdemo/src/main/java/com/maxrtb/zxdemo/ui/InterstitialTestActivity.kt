package com.maxrtb.zxdemo.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maxrtb.zxdemo.R
import com.maxrtb.zhixuan.interstitial.ZhixuanInterstitialAdapter

class InterstitialTestActivity : AppCompatActivity() {

    private val TAG = "InterstitialTest"
    private var adapter: ZhixuanInterstitialAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_simple)

        val btnLoad = findViewById<Button>(R.id.btn_load)
        val btnShow = findViewById<Button>(R.id.btn_show)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        title = "📱 插屏广告"

        btnLoad.text = "📥 加载插屏广告"
        btnLoad.setOnClickListener {
            loadInterstitial(tvStatus)
        }

        btnShow.text = "🎬 展示插屏广告"
        btnShow.setOnClickListener {
            showInterstitial(tvStatus)
        }
    }

    private fun loadInterstitial(tvStatus: TextView) {
        Log.d(TAG, "开始加载插屏广告...")
        tvStatus.text = "⏳ 加载中..."
        Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show()

        adapter = ZhixuanInterstitialAdapter(this, "inter_test")

        adapter?.loadAd(object : ZhixuanInterstitialAdapter.InterstitialAdListener {
            override fun onAdLoaded() {
                Log.i(TAG, "✅ 插屏加载成功")
                tvStatus.text = "✅ 加载成功，点击展示按钮"
                Toast.makeText(this@InterstitialTestActivity, "加载成功", Toast.LENGTH_SHORT).show()
            }

            override fun onAdShown() {
                Log.i(TAG, "👁️ 插屏展示")
            }

            override fun onAdClicked() {
                Log.i(TAG, "👆 插屏点击")
            }

            override fun onAdClosed() {
                Log.i(TAG, "❌ 插屏关闭")
                tvStatus.text = "❌ 插屏已关闭"
                adapter = null
            }

            override fun onAdFailed(msg: String) {
                Log.e(TAG, "❌ 插屏失败: $msg")
                tvStatus.text = "❌ 加载失败: $msg"
                Toast.makeText(this@InterstitialTestActivity, "失败: $msg", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showInterstitial(tvStatus: TextView) {
        if (adapter == null) {
            Toast.makeText(this, "请先加载广告", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "展示插屏广告...")
        adapter?.show(object : ZhixuanInterstitialAdapter.InterstitialAdListener {
            override fun onAdLoaded() {}
            override fun onAdShown() {}
            override fun onAdClicked() {}
            override fun onAdClosed() {
                adapter = null
            }
            override fun onAdFailed(msg: String) {
                Toast.makeText(this@InterstitialTestActivity, "展示失败: $msg", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.destroy()
    }
}
