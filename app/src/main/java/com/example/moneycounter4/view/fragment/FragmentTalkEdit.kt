package com.example.moneycounter4.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.network.FileUploadUtil
import com.example.moneycounter4.viewmodel.CommunityViewModel
import com.example.moneycounter4.widgets.KeyboardController
import com.example.moneycounter4.widgets.ProgressDialogW
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_talk_edit.*

class FragmentTalkEdit : BaseFragment() {

    companion object {
        var viewModel: CommunityViewModel? = null
    }

    private var imgPath: String? = null
    private var upLoading = false
    private var imgUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_talk_edit, container, false)
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewModel?.replyInfo?.value?.let {
            if (it.replyId != -1) {
                textViewReplyText.text = "回复：@${it.nickname}"
            }
        }


        progressBar.indeterminateDrawable.setColorFilter(
            resources.getColor(
                R.color.colorLightRed,
                null
            ), PorterDuff.Mode.SRC_IN
        )

        imageViewPic.setOnClickListener {

            RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
                if (it) {
                    selectPic()
                }
            }

        }



        textViewSend.setOnClickListener {
            KeyboardController.hideInputKeyboard(requireContext(), it)

            if (upLoading) {
                Toast.makeText(requireContext(), "图片还没上传好鸭~", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            activity?.let { it1 -> ProgressDialogW.show(it1, "请稍后", "正在上传~", false) }
            if ((viewModel?.replyInfo?.value?.replyId ?: -1) == -1) {
                // 发帖
                if (imgUrl.isNullOrBlank()) {
                    viewModel?.releaseDynamic(editTextTalk.text.toString(), "广场")
                } else {
                    viewModel?.releaseDynamic(editTextTalk.text.toString(), "广场", listOf(imgUrl!!))
                }
            } else {
                // 回复
                viewModel?.reply(editTextTalk.text.toString())
            }
        }
        viewModel?.replyStatus?.observe {
            ProgressDialogW.hide()
            if (it) {
                viewModel?.refreshDynamic()
                findNavController().popBackStack()
            }
        }
        viewModel?.releaseDynamicStatus?.observe {
            ProgressDialogW.hide()
            if (it) {
                val navController = findNavController()
                while (navController.backStack.size >= 1) {
                    navController.popBackStack()
                }
                navController.navigate(R.id.action_global_fragmentCommunity)
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


            //imgPath?.let { LogW.d(it) }//===================================================
            progressBar.visibility = View.VISIBLE
            imageViewPic.visibility = View.GONE
            imageViewPic.setImageBitmap(null)

            upLoading = true

            // TODO imgPath
            if (imgPath.isNullOrEmpty()) {
                return
            }
            FileUploadUtil.uploadMultiFile(listOf(imgPath!!)) {
                activity?.runOnUiThread {
                    if (it.picUrls.isNotEmpty()) {
                        imgUrl = it.picUrls[0]
                        progressBar.visibility = View.GONE
                        imageViewPic.visibility = View.VISIBLE
                        Glide.with(this).load(it.picUrls[0]).into(imageViewPic)
                        upLoading = false
                    }
                }
            }


        }
    }


}