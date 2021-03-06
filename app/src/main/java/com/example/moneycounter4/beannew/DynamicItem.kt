package com.example.moneycounter4.beannew

import com.google.gson.annotations.SerializedName

data class DynamicItem(
    @SerializedName("dynamic_id")
    val dynamicId: Int = 0,
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("submit_time")
    val submitTime: Long = 0L,
    @SerializedName("text")
    val text: String = "",

    @SerializedName("nickname")
    val nickname: String = "",
    @SerializedName("avatar_url")
    val avatarUrl: String = "",

    @SerializedName("pic_url")
    val picUrl: List<String> = listOf(),

    @SerializedName("comment_list")
    val commentList: List<CommentItem> = listOf(),

    @SerializedName("praise")
    val praise: List<User> = listOf()

)
