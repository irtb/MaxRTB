package com.maxrtb.zxdemo.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maxrtb.zxdemo.R
import com.maxrtb.zhixuan.reward.ZhixuanRewardAdapter

class RewardVideoTestActivity : AppCompatActivity() {

    private val TAG = "RewardVideoTest"
    private var adapter: ZhixuanRewardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_simple)

        val btnLoad = findViewById<Button>(R.id.btn_load)
        val btnShow = findViewById<Button>(R.id.btn_show)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        title = "🎁 激励视频"

        btnLoad.text = "📥 加载激励视频"
        btnLoad.setOnClickListener {
            loadReward(tvStatus)
        }

        btnShow.text = "▶️ 播放视频"
        btnShow.setOnClickListener {
            showReward(tvStatus)
        }
    }

    private fun loadReward(tvStatus: TextView) {
        Log.d(TAG, "开始加载激励视频...")
        tvStatus.text = "⏳ 加载中..."
        Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show()

        adapter = ZhixuanRewardAdapter(this, "reward_test")

        adapter?.loadAd(object : ZhixuanRewardAdapter.RewardAdListener {
            override fun onAdLoaded() {
                Log.i(TAG, "✅ 激励视频加载成功")
                tvStatus.text = "✅ 加载成功，点击播放按钮"
                Toast.makeText(this@RewardVideoTestActivity, "加载成功", Toast.LENGTH_SHORT).show()
            }

            override fun onAdShown() {
                Log.i(TAG, "👁️ 激励视频播放")
            }

            override fun onAdClicked() {
                Log.i(TAG, "👆 激励视频点击")
            }

            override fun onAdClosed() {
                Log.i(TAG, "❌ 激励视频关闭")
                tvStatus.text = "❌ 视频已关闭"
                adapter = null
            }

            override fun onRewarded() {
                Log.i(TAG, "🎁 用户获得奖励")
                tvStatus.text = "🎁 恭喜！获得 100 金币"
                Toast.makeText(this@RewardVideoTestActivity, "🎉 获得 100 金币", Toast.LENGTH_LONG).show()
            }

            override fun onAdFailed(msg: String) {
                Log.e(TAG, "❌ 激励视频失败: $msg")
                tvStatus.text = "❌ 加载失败: $msg"
                Toast.makeText(this@RewardVideoTestActivity, "失败: $msg", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showReward(tvStatus: TextView) {
        if (adapter == null) {
            Toast.makeText(this, "请先加载广告", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "播放激励视频...")
        adapter?.show(object : ZhixuanRewardAdapter.RewardAdListener {
            override fun onAdLoaded() {}
            override fun onAdShown() {}
            override fun onAdClicked() {}
            override fun onAdClosed() {
                adapter = null
            }
            override fun onRewarded() {}
            override fun onAdFailed(msg: String) {
                Toast.makeText(this@RewardVideoTestActivity, "播放失败: $msg", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.destroy()
    }
}
