package com.example.project_rxjava_api_calling

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_rxjava_api_calling.model.ImageModelItem
import com.example.project_rxjava_api_calling.network.Retrofit
import com.example.project_rxjava_api_calling.adapter.ImgAdapter
import com.example.project_rxjava_api_calling.databinding.ActivityMainBinding
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
	private lateinit var recyclerView: RecyclerView
	private lateinit var imgAdapter: ImgAdapter
	private lateinit var apiData: List<ImageModelItem>
	private lateinit var binding: ActivityMainBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityMainBinding.inflate(layoutInflater)
		
		setContentView(binding.root)
		
		imgAdapter = ImgAdapter(this) { item ->
			showDetailsScreen(item, apiData)
		}
		
		recyclerView = binding.recyclerView
		recyclerView.apply {
			layoutManager = LinearLayoutManager(
				context,
				LinearLayoutManager.VERTICAL, false
			)
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
		intent.apply { putExtra("item", myData) }
		val parcelableList: ArrayList<Parcelable> = ArrayList(apiData)
		intent.putParcelableArrayListExtra("IMAGE_LIST", parcelableList)
		startActivity(intent)
	}
	private fun getObservable(): Observable<List<ImageModelItem>> {
		return Retrofit.api.getData()
	}
	
	private fun getObserver(imgList: List<ImageModelItem>?) {
		if (imgList.isNullOrEmpty()) return
		else {
			apiData = imgList.toList()
			imgAdapter.setData(imgList)
		}
	}
	
	private fun onFailure(t: Throwable) {
		Log.d("main", "onFailure: " + t.message)
		t.printStackTrace()
	}
}