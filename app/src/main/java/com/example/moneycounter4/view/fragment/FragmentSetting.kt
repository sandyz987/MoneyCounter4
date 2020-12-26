package com.example.moneycounter4.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.bean.ItemAccount
import com.example.moneycounter4.utils.HttpUtilCallback
import com.example.moneycounter4.utils.HttpUtils.HttpUtil
import com.example.moneycounter4.utils.JSonEvalUtils.JSonEval
import com.example.moneycounter4.utils.MyImageUtils.ImageLoader
import com.example.moneycounter4.utils.UploadPic
import com.example.moneycounter4.view.activity.ActivityLogin
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel
import com.example.moneycounter4.widgets.ProgressDialogW
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_setting.*

class FragmentSetting : Fragment() {

    var picUrl : String = ""
    private var imgPath : String? = ""
    var upLoading = false

    lateinit var viewModel : MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ProgressDialogW.show(requireContext(),"提示","从服务器读取信息中，请稍后",false)
        HttpUtil.getInstance().httpGet((activity?.application as MainApplication).connectionUrlMain,object :
            HttpUtilCallback {
            override fun doSomething(respond: String?) {
                var account: ItemAccount?=null
                try {
                    account = JSonEval.getInstance().fromJson(respond, ItemAccount::class.java)
                }catch (e: Exception){
                    e.printStackTrace()
                }
                activity?.runOnUiThread {
                    account?.let {
                        textViewMoney?.text = it.accountNum
                        textViewTips?.text = it.text
                        textViewUsrName?.text = it.usrName
                        textViewTime?.text = it.sex
                        picUrl = it.picUrl?:"null"
                        imageViewUsrPic?.let { it2 -> Glide.with(requireActivity()).load(it.picUrl).into(it2) }
                        ProgressDialogW.hide()
                    }
                }

            }
            override fun error() {
                activity?.runOnUiThread {
                    Toast.makeText(activity,"加载失败，请检查网络连接呀", Toast.LENGTH_SHORT).show()
                    ProgressDialogW.hide()
                    findNavController().navigate(R.id.action_fragmentSetting_pop)
                }

            }
        },activity,"page","test","post","getaccountinfo","accountnum",viewModel.accountNum.get())



        textViewFinish.setOnClickListener {

            if(upLoading){
                Toast.makeText(activity,"头像还没上传好哦，再等等吧", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(picUrl == "" || picUrl == "null"){
                Toast.makeText(activity,"头像url为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ProgressDialogW.show(requireContext(),"提示","正在上传~请稍后",false)

            val account = ItemAccount()
            account.usrName = textViewUsrName.text.toString()
            account.sex = textViewTime.text.toString()
            account.text = textViewTips.text.toString()
            account.picUrl = picUrl

            val gson = Gson()
            val info = gson.toJson(account)

            HttpUtil.getInstance().httpPost((activity?.application as MainApplication).connectionUrlMain,object :
                HttpUtilCallback {
                @SuppressLint("RestrictedApi")
                override fun doSomething(respond: String?) {
                    activity?.runOnUiThread {
                        when(respond){
                            "1"->{
                                val navController = findNavController()
                                while(navController.backStack.size >= 1){//当底部栏点击，将所有
                                    navController.popBackStack()
                                }
                                val bundle = Bundle()
                                bundle.putBoolean("isMine",true)
                                bundle.putString("accountNum",viewModel.accountNum.get())
                                navController.navigate(R.id.action_global_fragmentMine,bundle)
                                Toast.makeText(activity,"修改成功！", Toast.LENGTH_SHORT).show()
                            }
                            else->{
                                Toast.makeText(activity,"修改失败，登录失效了？", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_fragmentSetting_pop)
                            }
                        }
                        ProgressDialogW.hide()
                    }

                }
                override fun error() {
                    activity?.runOnUiThread {
                        Toast.makeText(activity,"修改失败，请检查网络连接呀", Toast.LENGTH_SHORT).show()
                        ProgressDialogW.hide()
                        findNavController().navigate(R.id.action_fragmentSetting_pop)
                    }

                }
            },activity,"page","test","post","setusrinfo","token",viewModel.token.toString(),"info",info)
        }



        constraintPic.setOnClickListener {
            context?.let {
                if (ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1
                    )
                } else {
                    selectPic()
                }
            }
        }



        constraintName.setOnClickListener {
            val inputServer = EditText(requireContext())
            inputServer.setText(textViewUsrName.text.toString())
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("请输入昵称：").setView(inputServer)
                .setNegativeButton("取消", null)
            builder.setPositiveButton("确定"
            ) { _, _ ->
                if(inputServer.text.toString().length in 1..7){
                    textViewUsrName.text = inputServer.text.toString()
                }else{
                    Toast.makeText(requireContext(), "昵称长度要在1至7", Toast.LENGTH_SHORT).show()
                }
            }.show()
        }


        constraintText.setOnClickListener {
            val inputServer = EditText(requireContext())
            inputServer.setText(textViewTips.text.toString())
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("请输入个性签名：").setView(inputServer)
                .setNegativeButton("取消", null)
            builder.setPositiveButton("确定"
            ) { _, _ ->
                if(inputServer.text.toString().length in 1..40){
                    textViewTips.text = inputServer.text.toString()
                }else{
                    Toast.makeText(requireContext(), "个性签名长度要在1至40", Toast.LENGTH_SHORT).show()
                }
            }.show()
        }

        constraintDel.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("真的要退出嘛~")
                .setNegativeButton("手滑了", null)
            builder.setPositiveButton("去意已决"
            ) { _, _ ->
                val intent = Intent(requireContext(),ActivityLogin::class.java)
                startActivity(intent)
                requireActivity().finish()
            }.show()
        }


        constraintSex.setOnClickListener {
            val options1Items = listOf("男","女","保密")
            val pvOptions: OptionsPickerView<String> =
                OptionsPickerBuilder(requireContext(), object : OnOptionsSelectListener {
                    override fun onOptionsSelect(
                        options1: Int,
                        option2: Int,
                        options3: Int,
                        v: View?
                    ) {
                        //返回的分别是三个级别的选中位置
                        val tx: String = options1Items[options1]
                        textViewTime.text = tx
                    }
                }).build()
            pvOptions.setPicker(options1Items)
            pvOptions.setSelectOptions(when(textViewTime.text.toString()){
                "男"->0
                "女"->1
                "保密"->2
                else->2
            })
            pvOptions.show()
        }



    }



    //权限=========
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
//                    ActivityCompat.requestPermissions(
//                        requireActivity(),
//                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                        2
//                    )
            }

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPic()
            } else {
                Toast.makeText(requireContext(), "读取图库需要权限哦~请在设置中给一下吧~", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == 2) {
            selectPic()
        }
    }

    //====选择照片

    private fun selectPic() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 11)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                selectedImage?.let { requireContext().contentResolver.query(it, filePathColumn, null, null, null) }
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            imgPath = columnIndex?.let { cursor.getString(it) }
            cursor?.close()


            upLoading = true
            ProgressDialogW.show(requireContext(),"提示","正在上传~",false)
            UploadPic.upload(imgPath,object : HttpUtilCallback{
                override fun doSomething(respond: String?) {
                    activity?.runOnUiThread {
                        ImageLoader.with(requireContext()).load(respond).into(imageViewUsrPic)
                    }
                    upLoading = false
                    picUrl = respond?:""
                    ProgressDialogW.hide()
                }

                override fun error() {
                    activity?.runOnUiThread {
                        picUrl = ""
                        Toast.makeText(requireContext(),"上传失败了~请重试",Toast.LENGTH_SHORT).show()
                    }
                    upLoading = false
                    ProgressDialogW.hide()
                }
            })


        }
    }


}