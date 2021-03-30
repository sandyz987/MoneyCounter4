package com.example.moneycounter4.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.moneycounter4.beannew.CounterDataItem

@Dao
interface CounterDao {
    //查询CounterDataItem表中所有数据
    @get:Query("SELECT * FROM data_list")
    val all: List<CounterDataItem?>?

    @Query("SELECT * FROM data_list WHERE 'id' IN (:c)")
    fun loadAllByIds(c: IntArray?): List<CounterDataItem?>?

    //    @Query("SELECT * FROM CounterDataItem   LIMIT 1")
    //    void findCounterDataItem(CounterDataItem CounterDataItem);

    @Query("SELECT * FROM data_list WHERE 'id'=:id LIMIT 1")
    fun findCounterDataItem(id: Int): CounterDataItem?

    @Insert
    fun insertAll(c: List<CounterDataItem>?)

    @Delete
    fun delete(vararg c: CounterDataItem?)

    @Query("DELETE FROM data_list")
    fun deleteAllCounterDataItem()

    @Query("DELETE FROM data_list WHERE 'id'=:id")
    fun delete(id: Int)

    // 当type为true查询收入
    @Query("SELECT * FROM data_list WHERE :type AND 'money'>=0 LIMIT :limit")
    fun getType(type: Boolean, limit: Int): List<CounterDataItem?>?

    // 当type为true查询收入
    @Query("SELECT * FROM data_list WHERE :type AND 'money'>=0")
    fun getType(type: Boolean): List<CounterDataItem?>?

    @Query("SELECT * FROM data_list LIMIT :limit")
    fun getAll(limit: Int): List<CounterDataItem?>?

    @Query("SELECT * FROM data_list WHERE 'time'>=:time")
    fun getByTime(time: Long): List<CounterDataItem?>?
}