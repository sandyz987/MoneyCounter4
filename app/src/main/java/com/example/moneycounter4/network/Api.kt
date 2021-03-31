package com.example.moneycounter4.network

import com.example.moneycounter4.beannew.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 *@author zhangzhe
 *@date 2021/3/3
 *@description
 */

interface Api {

    @FormUrlEncoded
    @POST("Counter4Sql?action=register")
    fun register(
        @Field("user_id") userId: String,
        @Field("password") password: String
    ): Observable<InfoWrapper>


    @FormUrlEncoded
    @POST("Counter4Sql?action=getAllDynamic")
    fun getAllDynamic(
        @Field("pos") pos: Int,
        @Field("size") size: Int,
        @Field("topic") topic: String
    ): Observable<ApiWrapper<List<DynamicItem>>>

    @FormUrlEncoded
    @POST("Counter4Sql?action=releaseDynamic")
    fun releaseDynamic(
        @Field("text") text: String,
        @Field("topic") topic: String
    ): Observable<InfoWrapper>

    // 发布有图片的帖子，图片url用逗号分割
    @FormUrlEncoded
    @POST("Counter4Sql?action=releaseDynamic")
    fun releaseDynamicPic(
        @Field("text") text: String,
        @Field("topic") topic: String,
        @Field("pic_url") picUrl: String
    ): Observable<InfoWrapper>


    @FormUrlEncoded
    @POST("Counter4Sql?action=reply")
    fun reply(
        @Field("text") text: String,
        @Field("reply_id") replyId: Int,
        @Field("which") which: Int
    ): Observable<InfoWrapper>


    @FormUrlEncoded
    @POST("Counter4Sql?action=getUserInfo")
    fun getUserInfo(
        @Field("user_id") userId: String
    ): Observable<ApiWrapper<User>>

    @FormUrlEncoded
    @POST("Counter4Sql?action=reversePraise")
    fun reversePraise(
        @Field("id") id: Int,
        @Field("which") which: Int
    ): Observable<InfoWrapper>

    @FormUrlEncoded
    @POST("Counter4Sql?action=deleteComment")
    fun deleteComment(
        @Field("id") id: Int,
        @Field("which") which: Int
    ): Observable<InfoWrapper>

    @FormUrlEncoded
    @POST("Counter4Sql?action=deleteDynamic")
    fun deleteDynamic(
        @Field("dynamic_id") dynamicId: Int
    ): Observable<InfoWrapper>

    @Multipart
    @POST("PicUpload?action=uploadPic")
    fun uploadPicture(
        @Body photo: MultipartBody
    ): Observable<ApiWrapper<UploadPicInfo>>

    @FormUrlEncoded
    @POST("Counter4Sql?action=changeUserInfo")
    fun changeUserInfo(
        @Field("user_info") userInfo: String
    ): Observable<InfoWrapper>


}