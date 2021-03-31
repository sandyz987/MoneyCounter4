package com.example.moneycounter4.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseViewModelFragment
import com.example.moneycounter4.beannew.User
import com.example.moneycounter4.model.Config
import com.example.moneycounter4.network.PicUploadUtils
import com.example.moneycounter4.view.activity.ActivityLogin
import com.example.moneycounter4.viewmodel.SettingViewModel
import com.example.moneycounter4.widgets.ProgressDialogW
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_setting.*

class FragmentSetting : BaseViewModelFragment<SettingViewModel>() {

    var picUrl: String = ""
    private var imgPath: String? = ""
    var upLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ProgressDialogW.show(requireContext(), "提示", "从服务器读取信息中，请稍后", false)
        viewModel.getUser(Config.userId)

        viewModel.user.observeNotNull { user ->
            textViewUsrName.text = user.nickname
            textViewUid.text = user.userId
            textViewText.text = user.text
            textViewSex.text = user.sex
            picUrl = user.avatarUrl
            context?.let { Glide.with(it).load(user.avatarUrl).into(imageViewUsrPic) }
            ProgressDialogW.hide()

        }

        textViewFinish.setOnClickListener {

            if (upLoading) {
                Toast.makeText(activity, "头像还没上传好哦，再等等吧", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (picUrl == "" || picUrl == "null") {
                Toast.makeText(activity, "头像url为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val user = User()
            user.nickname = textViewUsrName.text.toString()
            user.sex = textViewSex.text.toString()
            user.text = textViewText.text.toString()
            user.avatarUrl = picUrl

            viewModel.changeUserInfo(Gson().toJson(user))


        }



        constraintPic.setOnClickListener {
            context?.let {
                val rxPermissions = RxPermissions(this)
                rxPermissions.request(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).subscribe {
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
            builder.setPositiveButton(
                "确定"
            ) { _, _ ->
                if (inputServer.text.toString().length in 1..7) {
                    textViewUsrName.text = inputServer.text.toString()
                } else {
                    Toast.makeText(requireContext(), "昵称长度要在1至7", Toast.LENGTH_SHORT).show()
                }
            }.show()
        }


        constraintText1.setOnClickListener {
            val inputServer = EditText(requireContext())
            inputServer.setText(textViewText.text.toString())
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("请输入个性签名：").setView(inputServer)
                .setNegativeButton("取消", null)
            builder.setPositiveButton(
                "确定"
            ) { _, _ ->
                if (inputServer.text.toString().length in 1..40) {
                    textViewText.text = inputServer.text.toString()
                } else {
                    Toast.makeText(requireContext(), "个性签名长度要在1至40", Toast.LENGTH_SHORT).show()
                }
            }.show()
        }

        constraintDel.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("真的要退出登录嘛~")
                .setNegativeButton("手滑了", null)
            builder.setPositiveButton(
                "去意已决"
            ) { _, _ ->
                Config.password = ""
                val intent = Intent(requireContext(), ActivityLogin::class.java)
                startActivity(intent)
                requireActivity().finish()
            }.show()
        }


        constraintSex.setOnClickListener {
            val options1Items = listOf("男", "女", "保密")
            val pvOptions: OptionsPickerView<String> =
                OptionsPickerBuilder(
                    requireContext()
                ) { options1, _, _, _ -> //返回的分别是三个级别的选中位置
                    val tx: String = options1Items[options1]
                    textViewSex.text = tx
                }.build()
            pvOptions.setPicker(options1Items)
            pvOptions.setSelectOptions(
                when (textViewSex.text.toString()) {
                    "男" -> 0
                    "女" -> 1
                    "保密" -> 2
                    else -> 2
                }
            )
            pvOptions.show()
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
                selectedImage?.let {
                    requireContext().contentResolver.query(
                        it,
                        filePathColumn,
                        null,
                        null,
                        null
                    )
                }
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            imgPath = columnIndex?.let { cursor.getString(it) }
            cursor?.close()


            upLoading = true
            ProgressDialogW.show(requireContext(), "提示", "正在上传~", false)
            imgPath?.let {
                PicUploadUtils.uploadPicture(listOf(it), { v ->
                    Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show()
                    if (v.isNotEmpty()) {
                        Glide.with(this).load(v[0]).into(imageViewUsrPic)
                    }
                    ProgressDialogW.hide()
                    picUrl = v[0]
                    upLoading = false
                }, {
                    Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show()
                    ProgressDialogW.hide()
                    upLoading = false
                })
            }


        }
    }


}