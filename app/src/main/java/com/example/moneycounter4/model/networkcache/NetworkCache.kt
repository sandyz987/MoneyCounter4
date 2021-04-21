package com.example.moneycounter4.model.networkcache

import androidx.room.Room
import com.example.moneycounter4.viewmodel.MainApplication

object NetworkCache {
    private val db: NetworkDatabase by lazy {
        Room.databaseBuilder(
            MainApplication.context,
            NetworkDatabase::class.java, "url_cache"
        )
            .build()
    }

    fun addCache(url: String, content: String) {
        db.networkDao()?.addUrlCache(UrlContent(url, System.currentTimeMillis(), content))
        db.networkDao()?.deleteOverdue(System.currentTimeMillis())
    }

    fun getCache(url: String): String? {
        return db.networkDao()?.getContent(url)?.content
    }
}