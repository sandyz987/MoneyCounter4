package com.example.moneycounter4.beannew

import com.google.gson.annotations.SerializedName

data class CommentItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("reply_id")
    val replyId: Int = 0,
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("submit_time")
    val submitTime: Long = 0L,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("which")
    val which: Int = 0,
    @SerializedName("reply_user_nickname")
    val replyUserNickname: String = "",

    @SerializedName("nickname")
    val nickname: String = "",
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("reply_list") // 只有一级评论有，二级评论没有这个，默认空
    val replyList: List<CommentItem> = listOf(),

    @SerializedName("praise")
    val praise: List<User> = listOf()
)

fun <T> List<T>.findEquals(t: (T) -> Boolean): Boolean {
    var b = false
    this.forEach {
        if (t.invoke(it)) {
            b = true
        }
    }
    return b
}
