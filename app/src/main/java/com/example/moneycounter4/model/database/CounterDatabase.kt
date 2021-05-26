package com.example.moneycounter4.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.dao.CounterDao

@Database(entities = [CounterDataItem::class], version = 1)
abstract class CounterDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao?
}