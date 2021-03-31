package com.example.moneycounter4.beannew

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("nickname")
    var nickname: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("register_date")
    val registerDate: Long = 0L,
    @SerializedName("sex")
    var sex: String = "",
    @SerializedName("text")
    var text: String = "",
    @SerializedName("avatar_url")
    var avatarUrl: String = ""
)
