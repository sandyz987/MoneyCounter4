package com.example.moneycounter4.beannew

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token")
    val token: Int = 0
)