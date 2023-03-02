package com.example.project_rxjava_api_calling

//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_rxjava_api_calling.Model.ImageModelItem
import com.example.project_rxjava_api_calling.Network.Retrofit
import com.example.project_rxjava_api_calling.adapter.ImgAdapter
import com.example.project_rxjava_api_calling.databinding.ActivityMainBinding
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

//ashutosh dixit

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imgAdapter: ImgAdapter
    private lateinit var apiData: List<ImageModelItem>
    private lateinit var _binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)

        imgAdapter = ImgAdapter(this) { item ->
            showDetailsScreen(item, apiData)
        }
//        recyclerView = findViewById(R.id.recyclerView)
        recyclerView=_binding.recyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL ,false)
            adapter = imgAdapter
        }

        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(getObservable().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response -> getObserver(response) },
                { t -> onFailure(t) }
            ))
    }

    private fun showDetailsScreen(item: ImageModelItem, apiData: List<ImageModelItem>) {
        val intent = Intent(this@MainActivity, PagerActivity::class.java)

        val gson = Gson()
        val myData: String = gson.toJson(item)



        Log.d("showDetailScreen", "showDetailsScreen: ${item.id}")
        intent.apply { putExtra("item", myData) }
        val parcelableList: ArrayList<Parcelable> = ArrayList(apiData)
        intent.putParcelableArrayListExtra("IMAGE_LIST", parcelableList)


        startActivity(intent)
    }


    private fun getObservable(): Observable<List<ImageModelItem>> {
        val temp = Retrofit.api.getData()
        Log.d("temp", temp.toString())
        return temp
    }

    private fun getObserver(imgList: List<ImageModelItem>?) {
        if (imgList.isNullOrEmpty()) {
            Log.d("temp", "img list empty or null")
            return
        }
        Log.d("tmp", imgList.size.toString())
        if (imgList.isNotEmpty()) {
            apiData = imgList.toList()
            imgAdapter.setData(imgAdapter,imgList)

//            ImgAdapter.setData(imgAdapter, imgList)
        }
    }

    private fun onFailure(t: Throwable) {
        Log.d("main", "onFailure: " + t.message)
        t.printStackTrace()
    }
}