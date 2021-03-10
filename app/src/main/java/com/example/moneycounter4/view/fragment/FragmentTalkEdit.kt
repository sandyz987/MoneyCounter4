package com.example.moneycounter4.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.moneycounter4.R
import com.example.moneycounter4.base.BaseFragment
import com.example.moneycounter4.viewmodel.CommunityViewModel
import com.example.moneycounter4.widgets.KeyboardController
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_talk_edit.*

class FragmentTalkEdit : BaseFragment() {

    companion object {
        var viewModel: CommunityViewModel? = null
    }

    private var imgPath: String? = null
    var upLoading = false


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
            KeyboardController.hideInputKeyboard(requireContext(), it)
            if (upLoading) {
                Toast.makeText(requireContext(), "图片还没上传好鸭~", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if ((viewModel?.replyInfo?.value?.replyId ?: -1) == -1) {
                // 发帖
                viewModel?.releaseDynamic(editTextTalk.text.toString(), "广场")
            } else {
                // 回复
                viewModel?.reply(editTextTalk.text.toString())
            }
        }
        viewModel?.replyStatus?.observe {
            if (it) {
                viewModel?.refreshDynamic()
                findNavController().popBackStack()
            }
        }
        viewModel?.releaseDynamicStatus?.observe {
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


        }
    }


}