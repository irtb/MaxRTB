package com.maxrtb.zxdemo.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.maxrtb.zxdemo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_title).text = "智选广告SDK Demo"
        findViewById<TextView>(R.id.tv_info).text = """
            SDK版本: 1.0.0
            提供商: Zhixuan
            支持广告类型: 开屏广告
        """.trimIndent()

        findViewById<Button>(R.id.btn_restart).setOnClickListener {
            // 重启应用
            recreate()
        }
    }
}
