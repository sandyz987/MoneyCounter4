package com.example.moneycounter4.network

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 *@author zhangzhe
 *@date 2021/3/31
 *@description
 */

object PicUploadUtils {

    fun uploadPicture(
        imgPath: List<String>,
        onSuccess: (picUrls: List<String>) -> Unit,
        onFailed: () -> Unit
    ) {
        val fileList = imgPath.map { File(it) }
        val builder = MultipartBody.Builder()

        for (i in fileList.indices) {
            val file = fileList[i]
            val requestFile: RequestBody =
                RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            builder.addFormDataPart("file", file.getName(), requestFile)
        }

        ApiGenerator.getApiService(Api::class.java).uploadPicture(builder.build())
            .checkApiError()
            .setSchedulers()
            .doOnError {
                onFailed.invoke()
            }
            .safeSubscribeBy {
                onSuccess.invoke(it.picUrls)
            }.dispose()

    }


}