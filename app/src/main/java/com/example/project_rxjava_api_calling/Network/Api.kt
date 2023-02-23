//@file:Suppress("PackageName")

package com.example.project_rxjava_api_calling.Network

import com.example.project_rxjava_api_calling.model.ImageModelItem
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface Api  {
    @GET("v2/list")
     fun getData(): Observable<List<ImageModelItem>>

}