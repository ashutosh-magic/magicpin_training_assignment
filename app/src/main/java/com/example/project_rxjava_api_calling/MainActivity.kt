package com.example.project_rxjava_api_calling

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
	
	private companion object {
		private const val STORAGE_PERMISSION_CODE = 100
		private const val TAG = "PERMISSION TAG"
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityMainBinding.inflate(layoutInflater)
		
		setContentView(binding.root)
		
		if (checkPermission()) {
			Log.d(TAG, "onCreate:Permission Already Granted,create folder")
			createFolder()
			
		} else {
			Log.d(TAG, "onCreate:Permission not Granted,request")
			requestPermission()
		}

//		val folderName = "MagicPin Test"
		val subFolderName1 = "Deal Trend"
//		val subFolderName2 = "Carousel"
//		val subFolderName3 = "Feed Top"
		
		val folder = File(
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
			"MagicPin Test"
		)
		
		val subFolder1 = File(folder, subFolderName1)
		
		extItemList = mutableListOf()
		
		val files = subFolder1.listFiles()
		
		files?.forEach {
			if (it.isFile && (it.name.endsWith(".jpg")
						|| it.name.endsWith(".jpeg")
						|| it.name.endsWith(".png"))
			) {
				extItemList.add(ExternalModelItem(Uri.fromFile(it).toString(), 1))
			} else if (it.name.endsWith(".gif")) {
				extItemList.add(ExternalModelItem(it.path, 2))
			}
		}
		
		extItemList.sortBy { it.type }
		Log.d("extItem", "$extItemList")
		
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
	
	private fun createFolder() {
		
		val folderName = "MagicPin Test"
		val subFolderName1 = "Deal Trend"
		val subFolderName2 = "Carousel"
		val subFolderName3 = "Feed Top"
		
		val folder = File(
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
			"MagicPin Test"
		)
		if (!folder.exists()) folder.mkdirs()
		Log.d("DIRECTORY", "$folderName created: ${folder.exists()}")
		
		val subFolder1 = File(folder, subFolderName1)
		if (!subFolder1.exists()) subFolder1.mkdirs()
		Log.d("DIRECTORY", "$subFolderName1 created: ${subFolder1.exists()}")
		
		val subFolder2 = File(folder, subFolderName2)
		if (!subFolder2.exists()) subFolder2.mkdirs()
		Log.d("DIRECTORY", "$subFolderName2 created: ${subFolder2.exists()}")
		
		val subFolder3 = File(folder, subFolderName3)
		if (!subFolder3.exists()) subFolder3.mkdirs()
		Log.d("DIRECTORY", "$subFolderName3 created: ${subFolder3.exists()}")
		
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
	
	private fun requestPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			try {
				Log.d(TAG, "requestPermission:TRY")
				val intent = Intent()
				intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
				val uri = Uri.fromParts("package", this.packageName, null)
				intent.data = uri
				storageActivityResultLauncher.launch(intent)
			} catch (e: Exception) {
				Log.e(TAG, "requestPermission: ", e)
				val intent = Intent()
				intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
				storageActivityResultLauncher.launch(intent)
			}
		} else {
			ActivityCompat.requestPermissions(
				this,
				arrayOf(
					android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android
						.Manifest.permission.READ_EXTERNAL_STORAGE
				),
				STORAGE_PERMISSION_CODE
			)
		}
	}
	
	private val storageActivityResultLauncher = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		Log.d(TAG, "storageActivityResultLauncher: ")
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			if (Environment.isExternalStorageManager()) {
				Log.d(
					TAG,
					"storageActivityResultLauncher: Manage External Storage Permission is accepted"
				)
				createFolder()
			} else {
				Log.d(
					TAG,
					"storageActivityResultLauncher: Manage External Storage Permission is denied...."
				)
				Toast.makeText(
					this,
					"Manage External Storage Permission is denied",
					Toast.LENGTH_SHORT
				).show()
			}
		}
	}
	
	private fun checkPermission(): Boolean {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			Environment.isExternalStorageManager()
		} else {
			val write = ContextCompat.checkSelfPermission(
				this,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE
			)
			val read = ContextCompat.checkSelfPermission(
				this,
				android.Manifest.permission.READ_EXTERNAL_STORAGE
			)
			write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
		}
	}
	
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == STORAGE_PERMISSION_CODE) {
			if (grantResults.isNotEmpty()) {
				val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
				val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
				if (write && read) {
					Log.d(
						TAG,
						"onRequestPermissionsResult:External storage permission is Granted"
					)
					createFolder()
				} else {
					Log.d(
						TAG,
						"onRequestPermissionsResult: External storage permission is denied"
					)
					Toast.makeText(
						this,
						"External storage permission is denied",
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}
	
	
	private fun onFailure(t: Throwable) {
		Log.d("main", "onFailure: " + t.message)
		t.printStackTrace()
	}
}