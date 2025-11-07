package com.maxrtb.zxdm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.listener.RewardListener
import com.maxrtb.zx.ZX

class RewardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward_video_test)

        val btnLoad = findViewById<Button>(R.id.btn_load_reward)
        val btnShow = findViewById<Button>(R.id.btn_show_reward)
        val tvStatus = findViewById<TextView>(R.id.tv_reward_status)

        val provider = ZX.getProvider()

        btnLoad.setOnClickListener {
            tvStatus.text = "⏳ 加载中..."
            provider.requestRewardAd(
                this,
                "zx",
                "reward_test",
                object : RewardListener {
                    override fun onAdStartRequest(providerType: String) {
                        tvStatus.text = "开始请求..."
                    }

                    override fun onAdLoaded(providerType: String) {
                        tvStatus.text = "✅ 加载成功"
                        Toast.makeText(this@RewardActivity, "加载成功", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAdFailed(providerType: String, failedMsg: String?) {
                        tvStatus.text = "❌ 加载失败: $failedMsg"
                    }

                    override fun onAdClicked(providerType: String) {}
                    override fun onAdShow(providerType: String) {}
                    override fun onAdExpose(providerType: String) {}
                    override fun onAdVideoComplete(providerType: String) {}
                    override fun onAdVideoCached(providerType: String) {}
                    override fun onAdRewardVerify(providerType: String) {}
                    override fun onAdClose(providerType: String) {}
                }
            )
        }

        btnShow.setOnClickListener {
            provider.showRewardAd(this)
            tvStatus.text = "▶️ 正在播放..."
        }
    }
}
