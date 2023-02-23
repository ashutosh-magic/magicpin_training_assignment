package com.example.project_rxjava_api_calling.Network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {
    companion object{
        private val retrofit=retrofit2.Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).client(OkHttpClient())
            .build()
        val api:Api= retrofit.create(Api::class.java)
    }
}