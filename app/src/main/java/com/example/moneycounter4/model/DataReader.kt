package com.example.moneycounter4.model

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableArrayList
import com.example.moneycounter4.bean.CounterData
import com.example.moneycounter4.bean.DataItem
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.widgets.LogW
import com.google.gson.Gson
import kotlin.collections.ArrayList

class DataReader {

    companion object{
        lateinit var counterData: CounterData
        const val OPTION_BY_YEAR = 0
        const val OPTION_BY_MONTH = 1
        const val OPTION_BY_DAY = 2
        const val OPTION_INCOME = 3
        const val OPTION_EXPEND = 4
        const val OPTION_LAST = 5
        const val OPTION_IN = 6
        const val OPTION_OUT = 7

    }

    init {
        var s = ""
        val sharedPreferences = MainApplication.app.getSharedPreferences("counterData", Context.MODE_PRIVATE)
        sharedPreferences.getString("counterData",null)?.let { s = it }
        counterData = if(s != ""){
            val gson = Gson()
            CounterData()
            gson.fromJson(s,CounterData::class.javaObjectType)
        }else{
            CounterData()
        }
        if(counterData.typeListIn == null){
            counterData.typeListIn = ArrayList()//============init============
        }
        if(counterData.typeListOut == null){
            counterData.typeListOut = ArrayList()//==========init==============
        }
        if(counterData.list == null){
            counterData.list = ArrayList()
        }
        //LogW.d(counterData.list.size.toString())
    }

    fun save(){
        val sharedPreferences = MainApplication.app.getSharedPreferences("counterData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString("counterData",gson.toJson(counterData))
        editor.apply()
    }


    fun addItem(dataItem: DataItem){
        counterData.list.add(dataItem)
        counterData.list.sortBy { -it.time }
        save()
    }

    fun findItem(id: Long):DataItem?{
        for(item:DataItem in counterData.list){
            if(item.time == id){
                return item
            }
        }
        return null
    }

    fun replaceItem(id:Long,dataItem: DataItem){
        val d = findItem(id)
        d?.let {
            d.money = dataItem.money
            d.time = dataItem.time
            d.tips = dataItem.tips
            d.type = dataItem.type
        }
    }

    fun delItem(id:Long){
        counterData.list.remove(findItem(id))
        save()
    }

    fun getType(option: Int):ArrayList<TypeItem>{
        return when (option){
            OPTION_IN->{
                counterData.typeListIn
            }
            OPTION_OUT->{
                counterData.typeListOut
            }
            else -> TypeIndex.getAllType()
        }
    }



    fun saveType(list: ObservableArrayList<TypeItem>,option: Int){
        val l = when(option){
            OPTION_IN->counterData.typeListIn
            OPTION_OUT->counterData.typeListOut
            else->counterData.typeListIn
        }
        l.clear()
        l.addAll(list)
        save()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getItems(year:Int, month:Int, day: Int, option : Int):
            ArrayList<DataItem>{

        val list = ArrayList<DataItem>()
        val calendar = Calendar.getInstance()

        for(item:DataItem in counterData.list){
            calendar.timeInMillis = item.time
            when(option){
                OPTION_BY_YEAR->{
                    if(calendar.get(Calendar.YEAR) == year){
                        list.add(item)
                    }
                }
                OPTION_BY_MONTH->{
                    if(calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH)+1 == month){
                        list.add(item)
                    }
                }
                OPTION_BY_DAY->{
                    if(calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.DATE) == day){
                        list.add(item)
                    }
                }

            }
        }
        return list
    }

    fun count(list : ArrayList<DataItem>,option: Int):Double{
        var ans = 0.0

        for(item : DataItem in list){
            when(option){
                OPTION_EXPEND->{
                    if(item.money < 0){
                        ans -= item.money
                    }
                }
                OPTION_LAST->{
                    ans += item.money
                }
                OPTION_INCOME->{
                    if(item.money > 0){
                        ans += item.money
                    }
                }
            }
        }

        return ans
    }


}