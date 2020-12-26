package com.example.moneycounter4.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.utils.HttpUtilCallback
import com.example.moneycounter4.utils.HttpUtils.HttpUtil
import com.example.moneycounter4.utils.MyImageUtils.ImageLoader
import com.example.moneycounter4.utils.UploadPic
import com.example.moneycounter4.viewmodel.MainApplication
import com.example.moneycounter4.viewmodel.MainViewModel
import com.example.moneycounter4.widgets.LogW
import com.example.moneycounter4.widgets.ProgressDialogW
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_talk_edit.*
import java.util.*

class FragmentTalkEdit : Fragment() {

    private var imgPath: String? = null
    var picUrl: String? =null
    var upLoading = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_edit, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel : MainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        val talkId = arguments?.getLong("talkId")
        val usrName = arguments?.getString("usrName")


        talkId?.let {
            textViewReplyText.text = "回复：@${usrName}"
        }


        progressBar.indeterminateDrawable.setColorFilter(resources.getColor(R.color.colorLightRed,null), PorterDuff.Mode.SRC_IN)

        imageViewPic.setOnClickListener {
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



        textViewSend.setOnClickListener {
            if(upLoading){
                Toast.makeText(requireContext(),"图片还没上传好鸭~",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            ProgressDialogW.show(requireContext(),"提示","正在上传数据中",false)
            if (talkId == null){
                HttpUtil.getInstance().httpGet((requireActivity().application as MainApplication).connectionUrlMain,object : HttpUtilCallback{
                    @SuppressLint("RestrictedApi")
                    override fun doSomething(respond: String?) {
                        requireActivity().runOnUiThread {
                            when(respond){
                                "1"->{
                                    Toast.makeText(requireContext(),"已发表~~~(￣▽￣)~*",Toast.LENGTH_SHORT).show()
                                    val navController = findNavController()
                                    while(navController.backStack.size >= 1){
                                        navController.popBackStack()
                                    }
                                    navController.navigate(R.id.action_global_fragmentCommunity)
                                }
                                else->{
                                    Toast.makeText(requireContext(),"上传失败了诶，未知错误，登录失效了？请重新登录试试嘛？",Toast.LENGTH_SHORT).show()
                                }
                            }
                            ProgressDialogW.hide()
                        }
                    }
                    override fun error() {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(),"上传失败了诶，是不是没网了？",Toast.LENGTH_SHORT).show()
                            ProgressDialogW.hide()
                        }
                    }
                },requireContext(),"page", "test", "post", "talk","text",editTextTalk.text.toString(),"token",viewModel.token.toString(),"picurl",picUrl?:"")
            }else{
                HttpUtil.getInstance().httpGet((requireActivity().application as MainApplication).connectionUrlMain,object : HttpUtilCallback{
                    override fun doSomething(respond: String?) {
                        requireActivity().runOnUiThread {
                            when(respond){
                                "1"->{
                                    Toast.makeText(requireContext(),"已回复~~~(￣▽￣)~*",Toast.LENGTH_SHORT).show()
                                    val navController = findNavController()
                                    val bundle = Bundle()
                                    bundle.putLong("talkId",talkId)
                                    navController.popBackStack()
                                    navController.popBackStack()
                                    navController.navigate(R.id.action_global_fragmentTalkDetails,bundle)
                                }
                                else->{
                                    Toast.makeText(requireContext(),"上传失败了诶，未知错误，登录失效了？请重新登录试试嘛？",Toast.LENGTH_SHORT).show()
                                }
                            }
                            ProgressDialogW.hide()
                        }
                    }
                    override fun error() {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(),"上传失败了诶，是不是没网了？",Toast.LENGTH_SHORT).show()
                            ProgressDialogW.hide()
                        }
                    }
                },requireContext(),"page", "test", "post", "reply","id",talkId.toString(),"text",editTextTalk.text.toString(),"token",viewModel.token.toString(),"picurl",picUrl?:"")
            }
        }


    }

    private fun selectPic() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 11)
    }


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



            //imgPath?.let { LogW.d(it) }//===================================================
            progressBar.visibility = View.VISIBLE
            imageViewPic.visibility = View.GONE
            imageViewPic.setImageBitmap(null)

            upLoading = true
            UploadPic.upload(imgPath,object : HttpUtilCallback{
                override fun doSomething(respond: String?) {
                    activity?.runOnUiThread {
                        progressBar.visibility = View.GONE
                        imageViewPic.visibility = View.VISIBLE
                        ImageLoader.with(requireContext()).load(respond).into(imageViewPic)
                    }
                    upLoading = false
                    picUrl = respond?:""
                }

                override fun error() {
                    activity?.runOnUiThread {
                        progressBar.visibility = View.GONE
                        imageViewPic.visibility = View.VISIBLE
                        imageViewPic.setImageResource(R.drawable.ic_add_pic)
                        Toast.makeText(requireContext(),"上传失败了~请重试",Toast.LENGTH_SHORT).show()
                    }
                    upLoading = false
                }
            })


        }
    }


}