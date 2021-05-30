package com.example.moneycounter4.beannew

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "data_list")
data class CounterDataItem(
    @ColumnInfo(name = "money")
    var money: Double? = 0.0, // 正数：收入 负数：支出

    @ColumnInfo(name = "time")
    var time: Long? = 0L, // 时间戳

    @ColumnInfo(name = "tips")
    var tips: String? = "", // 用户输入备注

    @ColumnInfo(name = "type")
    var type: String? = "", // 种类

    @ColumnInfo(name = "image_path")
    var image_path: String? = "", // 图片的路径

    @ColumnInfo(name = "wallet")
    var wallet: String? = "", // 账户

    @ColumnInfo(name = "account_book")
    var accountBook: String? = "" // 账本
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
