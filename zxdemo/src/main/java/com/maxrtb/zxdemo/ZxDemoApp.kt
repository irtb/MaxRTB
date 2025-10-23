package com.maxrtb.zxdemo

import android.app.Application
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.maxrtb.zhixuan.provider.ZhixuanProvider

class ZxDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化TogetherAd
        initTogetherAd()
    }

    private fun initTogetherAd() {
        // 注册智选广告提供商
        val zhixuanProvider = AdProviderEntity(
            providerType = "zhixuan",
            classPath = ZhixuanProvider::class.java.name,
            desc = "智选广告SDK"
        )
        TogetherAd.addProvider(zhixuanProvider)

        // 设置全局配置
        TogetherAd.printLogEnable = true // 打开日志
        TogetherAd.failedSwitchEnable = false // 关闭失败切换（单一广告源）

        // 设置权重比例
        val ratioMap = linkedMapOf<String, Int>()
        ratioMap["zhixuan"] = 1
        TogetherAd.setPublicProviderRatio(ratioMap)
    }
}
