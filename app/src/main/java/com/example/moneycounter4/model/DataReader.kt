package com.example.moneycounter4.model

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableArrayList
import androidx.room.Room
import com.example.moneycounter4.bean.TypeData
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.beannew.CounterDataItem
import com.example.moneycounter4.model.dao.getByTime
import com.example.moneycounter4.model.database.CounterDatabase
import com.example.moneycounter4.viewmodel.MainApplication
import com.google.gson.Gson
import java.util.*

object DataReader {
    var typeData: TypeData
    const val OPTION_BY_YEAR = 0
    const val OPTION_BY_MONTH = 1
    const val OPTION_BY_DAY = 2
    const val OPTION_INCOME = 3
    const val OPTION_EXPEND = 4
    const val OPTION_LAST = 5
    const val OPTION_IN = 6
    const val OPTION_OUT = 7
    var db: CounterDatabase? = null

    init {


        db = Room.databaseBuilder(
            MainApplication.context,
            CounterDatabase::class.java, "data_list"
        )
            .allowMainThreadQueries() //允许在主线程中查询
            .build()

        var s = ""
        val sharedPreferences =
            MainApplication.app.getSharedPreferences("counterData", Context.MODE_PRIVATE)
        sharedPreferences.getString("counterData", null)?.let { s = it }
        typeData = if (s != "") {
            val gson = Gson()
            TypeData()
            gson.fromJson(s, TypeData::class.javaObjectType)
        } else {
            TypeData()
        }


        if (typeData.typeListIn == null) {
            typeData.typeListIn = ArrayList()//============init============
        }
        if (typeData.typeListOut == null) {
            typeData.typeListOut = ArrayList()//==========init==============
        }

        //LogW.d(counterData.list.size.toString())
    }

    fun save() {
        val sharedPreferences =
            MainApplication.app.getSharedPreferences("counterData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString("counterData", gson.toJson(typeData))
        editor.apply()
//        db?.userDao()?.deleteAllCounterDataItem()
//        db?.userDao()?.insertAll(counterData.list)
    }


    fun addItem(dataItem: CounterDataItem) {
        db?.userDao()?.insertAll(listOf(dataItem))
        save()
    }

//    fun replaceItem(id: Long, dataItem: CounterDataItem) {
//        val d = findItem(id)
//        d?.let {
//            d.money = dataItem.money
//            d.time = dataItem.time
//            d.tips = dataItem.tips
//            d.type = dataItem.type
//        }
//    }

    fun delItem(id: Long) {
        db?.userDao()?.deleteByTime(id)
    }

    fun getType(option: Int): ArrayList<TypeItem> {
        return when (option) {
            OPTION_IN -> {
                typeData.typeListIn
            }
            OPTION_OUT -> {
                typeData.typeListOut
            }
            else -> TypeIndex.getAllType()
        }
    }


    fun saveType(list: ObservableArrayList<TypeItem>, option: Int) {
        val l = when (option) {
            OPTION_IN -> typeData.typeListIn
            OPTION_OUT -> typeData.typeListOut
            else -> typeData.typeListIn
        }
        l.clear()
        l.addAll(list)
        save()
    }

//    @Deprecated("效率低下")
//    @RequiresApi(Build.VERSION_CODES.N)
//    fun getItems(year: Int, month: Int, day: Int, option: Int):
//            ArrayList<CounterDataItem> {
//
//        val list = ArrayList<CounterDataItem>()
//        val calendar = Calendar.getInstance()
//
//        for (item: CounterDataItem in typeData.list) {
//            calendar.timeInMillis = item.time!!
//            when (option) {
//                OPTION_BY_YEAR -> {
//                    if (calendar.get(Calendar.YEAR) == year) {
//                        list.add(item)
//                    }
//                }
//                OPTION_BY_MONTH -> {
//                    if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month) {
//                        list.add(item)
//                    }
//                }
//                OPTION_BY_DAY -> {
//                    if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month && calendar.get(
//                            Calendar.DATE
//                        ) == day
//                    ) {
//                        list.add(item)
//                    }
//                }
//
//            }
//        }
//        return list
//    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getCounterItems(year: Int, month: Int, day: Int, option: Int):
            ArrayList<CounterDataItem> {

        val list = ArrayList<CounterDataItem>()

        when (option) {
            OPTION_BY_YEAR -> {
                db?.userDao()?.getByTime(year, -1)?.let {
                    list.addAll(it)
                }
            }
            OPTION_BY_MONTH -> {
                db?.userDao()?.getByTime(year, month, -1)?.let {
                    list.addAll(it)
                }
            }
            OPTION_BY_DAY -> {
                db?.userDao()?.getByTime(year, month, day, -1)?.let {
                    list.addAll(it)
                }
            }

        }
        return list
    }

    fun count(list: List<CounterDataItem>, option: Int): Double {
        var ans = 0.0

        for (item: CounterDataItem in list) {
            when (option) {
                OPTION_EXPEND -> {
                    if (item.money!! < 0) {
                        ans -= item.money!!
                    }
                }
                OPTION_LAST -> {
                    ans += item.money!!
                }
                OPTION_INCOME -> {
                    if (item.money!! > 0) {
                        ans += item.money!!
                    }
                }
            }
        }

        return ans
    }


}