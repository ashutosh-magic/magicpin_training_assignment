package com.example.project_rxjava_api_calling

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_rxjava_api_calling.adapter.ImgAdapter
import com.example.project_rxjava_api_calling.databinding.ActivityMainBinding
import com.example.project_rxjava_api_calling.model.ExternalModelItem
import com.example.project_rxjava_api_calling.model.ImageModelItem
import com.example.project_rxjava_api_calling.network.Retrofit
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class MainActivity : AppCompatActivity() {
	private lateinit var recyclerView: RecyclerView
	private lateinit var imgAdapter: ImgAdapter
	private lateinit var apiData: List<ImageModelItem>
	private lateinit var extItemList: MutableList<ExternalModelItem>
	
	
	private lateinit var binding: ActivityMainBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityMainBinding.inflate(layoutInflater)
		
		setContentView(binding.root)
		
//		getExternalFilesDir(null)
//		val outFile = File(getExternalFilesDir(null)!!.parent,"myFolder")
//		outFile.mkdirs()
//
//		val storageDirectory = Environment.getExternalStorageDirectory().toString()
//
//		val folderName = "MagicPin Test"
//
//		val subFolderName1 = "Deal Trend"
//		val subFolderName2 = "Carousel"
//		val subFolderName3 = "Feed Top"
//
//		val folder = File("$storageDirectory/$folderName")
//
//		val subFolder1 = File("$storageDirectory/$folderName/$subFolderName1")
//		val subFolder2 = File("$storageDirectory/$folderName/$subFolderName2")
//		val subFolder3 = File("$storageDirectory/$folderName/$subFolderName3")
//
//		if (!folder.exists()) folder.mkdir()
//
//		if (!subFolder1.exists()) subFolder1.mkdir()
//		if (!subFolder2.exists()) subFolder2.mkdir()
//		if (!subFolder3.exists()) subFolder3.mkdir()
		getExternalFilesDir(null)
		val testDir = File(getExternalFilesDir(null)!!.parent, "MagicPin Test")
		if(!testDir.exists())testDir.mkdirs()
		
		val dealTrendDir = File(testDir, "Deal Trend")
		if(!dealTrendDir.exists())dealTrendDir.mkdirs()
		
		val carouselDir = File(testDir, "Carousel")
		if(!carouselDir.exists())carouselDir.mkdirs()
		
		val feedTop = File(testDir, "Feed Top")
		if(!feedTop.exists())feedTop.mkdirs()
		
		extItemList = mutableListOf()
//		val storage = Environment.getExternalStorageDirectory().toString()
//		val rootDir=getExternalFilesDir("myFolder")
		val files = dealTrendDir.listFiles()
//		val files = File(directory).listFiles()
		
		files?.forEach {
			if (it.isFile && (it.name.endsWith(".jpg")
						|| it.name.endsWith(".jpeg")
						|| it.name.endsWith(".png"))
			) {
				extItemList.add(ExternalModelItem(Uri.fromFile(it).toString(),1))
			}
			else if(it.name.endsWith(".gif"))
			{
				extItemList.add(ExternalModelItem(it.path,2))
			}
		}
		extItemList.sortBy { it.type }
		
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
		
		imgAdapter.setExternalData(extItemList)
		
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