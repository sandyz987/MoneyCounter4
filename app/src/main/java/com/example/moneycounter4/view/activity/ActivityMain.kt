package com.example.moneycounter4.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


//所有fragment都在这个activity中运行

class ActivityMain : AppCompatActivity() {
    lateinit var viewModel :MainViewModel

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //简单的navigation导航部分
        bottomViewList.setSelect(true)
        bottomViewList.setOnClickListener {
            clrBottomViewSelect()
            bottomViewList.setSelect(true)
            navTo(R.id.action_global_fragmentHome,null)
        }

        bottomViewMine.setOnClickListener {
            clrBottomViewSelect()
            bottomViewMine.setSelect(true)
            val bundle = Bundle()
            bundle.putBoolean("isMine",true)
            bundle.putString("accountNum",viewModel.accountNum.get())
            navTo(R.id.action_global_fragmentMine,bundle)

        }
        bottomViewCommunity.setOnClickListener {
            clrBottomViewSelect()
            bottomViewCommunity.setSelect(true)
            navTo(R.id.action_global_fragmentCommunity,null)
        }

        bottomViewGraph.setOnClickListener {
            clrBottomViewSelect()
            bottomViewGraph.setSelect(true)
            navTo(R.id.action_global_fragmentGraph,null)
        }

        floatingAdd.setOnClickListener {
            if(findNavController(R.id.fragment).backStack.size == 2){
                findNavController(R.id.fragment).navigate(R.id.action_global_fragmentCounterEdit)
            }
        }


        //添加导航完成的listener,如果返回栈大于2说明不在首页平行的页面，则隐藏底部栏
        findNavController(R.id.fragment).addOnDestinationChangedListener { navController: NavController, navDestination: NavDestination, bundle: Bundle? ->
            if(navController.backStack.size > 2){//因为这个callback是导航完成后才触发的，栈中已经添加了目标fragment了，所以不是1而是2
                if(!bottomView.isHide){
                    bottomView.hide()//隐藏的动态效果
                }
            }else{
                if(bottomView.isHide){
                    bottomView.show()//进入的动态效果
                }
            }
        }


    }

    @SuppressLint("RestrictedApi")
    private fun navTo(id : Int, bundle: Bundle?){
        val navController = findNavController(R.id.fragment)

        while(navController.backStack.size >= 1){//当底部栏点击，将所有
            navController.popBackStack()
        }
        if(bundle!= null){
            navController.navigate(id,bundle)
        }else{
            navController.navigate(id)
        }

    }

    private fun clrBottomViewSelect(){
        bottomViewMine.setSelect(false)
        bottomViewCommunity.setSelect(false)
        bottomViewGraph.setSelect(false)
        bottomViewList.setSelect(false)
    }

}