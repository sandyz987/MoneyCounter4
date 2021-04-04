package com.example.moneycounter4.model.dao

import android.util.Log
import androidx.room.*
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.DataReader
import java.util.*

@Dao
interface CounterDao {
    //查询CounterDataItem表中所有数据
    @get:Query("SELECT * FROM data_list")
    val all: List<CounterDataItem>?

    @Query("SELECT * FROM data_list WHERE id IN (:c)")
    fun loadAllByIds(c: IntArray?): List<CounterDataItem?>?

    //    @Query("SELECT * FROM CounterDataItem   LIMIT 1")
    //    void findCounterDataItem(CounterDataItem CounterDataItem);

    @Query("SELECT * FROM data_list WHERE id =:id LIMIT 1")
    fun findCounterDataItem(id: Int): CounterDataItem?

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(c: List<CounterDataItem?>?)

    @Delete
    fun delete(vararg c: CounterDataItem?)

    @Query("DELETE FROM data_list WHERE time =:t")
    fun deleteByTime(t: Long)

    @Query("DELETE FROM data_list")
    fun deleteAllCounterDataItem()

    @Query("DELETE FROM data_list WHERE id =:id")
    fun delete(id: Int)

    // 当type为true查询收入
    @Query("SELECT * FROM data_list WHERE :type AND money >=0 LIMIT :limit")
    fun getType(type: Boolean, limit: Int): List<CounterDataItem?>?

    // 当type为true查询收入
    @Query("SELECT * FROM data_list WHERE :type AND money >=0")
    fun getType(type: Boolean): List<CounterDataItem?>?

    @Query("SELECT * FROM data_list LIMIT :limit")
    fun getAll(limit: Int): List<CounterDataItem?>?

    @Query("SELECT * FROM data_list WHERE time BETWEEN :low AND :high")
    fun getByTime(low: Long, high: Long): List<CounterDataItem?>?
//    @Query("SELECT * FROM data_list WHERE date_format('time','%Y-%m')=:y-:m")
//    fun getByTime(y: Int, m: Int): List<CounterDataItem?>?
//    @Query("SELECT * FROM data_list WHERE date_format('time','%Y')=:y")
//    fun getByTime(y: Int): List<CounterDataItem?>?


}

fun CounterDao.getByTime(y: Int, m: Int, d: Int): List<CounterDataItem> {
    val cal = Calendar.getInstance()
    cal.set(y, m - 1, d)
    val mutableList = mutableListOf<CounterDataItem>()
    getByTime(cal.timeInMillis, cal.timeInMillis + 86400000L)?.forEach {
        it?.let {
            mutableList.add(
                it
            )
        }
    }
    return mutableList
}

data class DateItem(
    val year: Int = 0, val month: Int = 0, val day: Int = 0
)

fun CounterDao.getByDuration(
    dateItem: DateItem,
    durationL: Long,
    durationR: Long,
    option: Int
): List<CounterDataItem> {
    val cal = Calendar.getInstance()
    cal.set(dateItem.year, dateItem.month - 1, dateItem.day)
    val mutableList = mutableListOf<CounterDataItem>()
    getByTime(
        cal.timeInMillis + durationL,
        cal.timeInMillis + durationR
    )?.forEach { it?.let { mutableList.add(it) } }
    return mutableList.filter {
        when (option) {
            DataReader.OPTION_EXPEND -> it.money!! < 0.0
            DataReader.OPTION_INCOME -> it.money!! > 0.0
            else -> true
        }
    }
}


fun CounterDao.getByTime(y: Int, m: Int): List<CounterDataItem> {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, y)
    cal.set(Calendar.MONTH, m - 1)
    cal.set(Calendar.DATE, 1)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)


    val mutableList = mutableListOf<CounterDataItem>()
    getByTime(
        cal.timeInMillis,
        cal.timeInMillis + 86400000L * cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    )?.forEach { it?.let { mutableList.add(it) } }
    return mutableList
}

fun CounterDao.getByTime(y: Int): List<CounterDataItem> {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, y)
    val mutableList = mutableListOf<CounterDataItem>()
    getByTime(
        cal.timeInMillis,
        cal.timeInMillis + 86400000L * cal.get(Calendar.DAY_OF_YEAR)
    )?.forEach { it?.let { mutableList.add(it) } }
    return mutableList
}