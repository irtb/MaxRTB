package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("interests")
    val interests: List<String> = emptyList(),
    @SerializedName("keywords")
    val keywords: List<String> = emptyList(),
)
