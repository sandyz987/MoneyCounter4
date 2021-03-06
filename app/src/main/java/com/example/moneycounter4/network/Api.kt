package com.example.moneycounter4.network

import com.example.moneycounter4.beannew.ApiWrapper
import com.example.moneycounter4.beannew.DynamicItem
import com.example.moneycounter4.beannew.InfoWrapper
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
        @Field("text") pos: String,
        @Field("topic") topic: String
    ): Observable<InfoWrapper>
}