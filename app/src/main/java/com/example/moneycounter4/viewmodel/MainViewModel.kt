package com.example.moneycounter4.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneycounter4.bean.CounterData
import com.example.moneycounter4.bean.DataItem
import com.example.moneycounter4.bean.TypeItem
import com.example.moneycounter4.model.DataReader
import com.example.moneycounter4.model.TypeIndex
import com.example.moneycounter4.widgets.LogW
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel : ViewModel() {
    var accountNum = ObservableField<String>()
    var password = ObservableField<String>()
    var token = -1


    var year = ObservableField<Int>()
    var month = ObservableField<Int>()
    var income = ObservableField<Double>()
    var expend = ObservableField<Double>()

    var selectedYear = 0

    var handlerAddType : Handler? = null

    var list = ObservableArrayList<DataItem>()
    var typeListOut = ObservableArrayList<TypeItem>()
    var typeListIn = ObservableArrayList<TypeItem>()



    init {
        //从sp中读取账号密码
        val sharedPreferences = MainApplication.app.getSharedPreferences("usrInfo",Context.MODE_PRIVATE)
        sharedPreferences.getString("accountNum",null).let { accountNum.set(it) }
        sharedPreferences.getString("password",null).let { password.set(it) }
        sharedPreferences.getInt("token",-1).let { token = it }
        LogW.d(this.toString())
        //初始化年月显示
        month.set(Calendar.getInstance().get(Calendar.MONTH)+1)
        year.set(Calendar.getInstance().get(Calendar.YEAR))
        val calendar = java.util.Calendar.getInstance()
        selectedYear = calendar.get(java.util.Calendar.YEAR)
        //初始化记账记录列表
        val tmpList = DataReader().getItems(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.DATE),DataReader.OPTION_BY_MONTH)
        tmpList.forEach { list.add(it) }
        //初始化收入图标种类列表
        val tmpList2 = DataReader().getType(DataReader.OPTION_IN)
        if(tmpList2.size == 0){
            TypeIndex.getInTypeInit().forEach { typeListIn.add(it) }
        }else {
            tmpList2.forEach { typeListIn.add(it) }
        }
        //初始化支出图标种类列表
        val tmpList3 = DataReader().getType(DataReader.OPTION_OUT)
        if(tmpList3.size == 0){
            TypeIndex.getOutTypeInit().forEach { typeListOut.add(it) }
        }else {
            tmpList3.forEach { typeListOut.add(it) }
        }
        refreshList()
    }

    fun refreshList(){
        list.clear()
        list.addAll(DataReader().getItems(year.get()!!,month.get()!!,0,DataReader.OPTION_BY_MONTH))
        income.set(DataReader().count(list,DataReader.OPTION_INCOME))
        expend.set(DataReader().count(list,DataReader.OPTION_EXPEND))
        list.sortBy { -it.time }
        income.set(DataReader().count(list,DataReader.OPTION_INCOME))
        expend.set(DataReader().count(list,DataReader.OPTION_EXPEND))
    }

    fun addItem(d : DataItem){
        DataReader().addItem(d)
        refreshList()
    }

//    fun delItem(d : DataItem){
//        DataReader().delItem(d.time)
//        refreshList()
//    }

    fun delItemByTime(t:Long){
        DataReader().delItem(t)

        refreshList()
    }


    fun addType(typeItem: TypeItem,option:Int){
        when(option){
            DataReader.OPTION_IN ->{
                typeListIn.add(typeItem)
                DataReader().saveType(typeListIn,option)
            }
            DataReader.OPTION_OUT ->{
                typeListOut.add(typeItem)
                DataReader().saveType(typeListOut,option)
            }
        }

    }

    fun delType(typeItem: TypeItem){
        typeListOut.remove(typeItem)
        typeListIn.remove(typeItem)
        DataReader().saveType(typeListOut,DataReader.OPTION_OUT)
        DataReader().saveType(typeListIn,DataReader.OPTION_IN)
    }



    fun saveUsrInfo(accountNum:String?, password:String?,token:Int?){
        accountNum?.let { this.accountNum.set(it) }
        password?.let { this.password.set(it) }
        this.token = token?:-1

        //账号密码存入sp
        val sharedPreferences = MainApplication.app.getSharedPreferences("usrInfo",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accountNum",this.accountNum.get())
        editor.putString("password",this.password.get())
        editor.putInt("token",this.token)
        editor.apply()
    }


}