package com.example.project_rxjava_api_calling

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project_rxjava_api_calling.model.ImageModelItem
import com.example.project_rxjava_api_calling.adapter.ViewPageAdapter
import com.example.project_rxjava_api_calling.databinding.ActivityViewPagerBinding
import com.google.gson.Gson

@Suppress("DEPRECATION")
class PagerActivity : AppCompatActivity() {
	private val itemList: MutableList<ImageModelItem> = ArrayList()
	private lateinit var binding: ActivityViewPagerBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
  
		binding = ActivityViewPagerBinding.inflate(layoutInflater)
		setContentView(binding.root)
  
		val gson = Gson()
		val myData = gson.fromJson(intent.getStringExtra("item"), ImageModelItem::class.java)
		val parcelableList: List<ImageModelItem>? = intent
            .getParcelableArrayListExtra("IMAGE_LIST")
		
		if (!parcelableList.isNullOrEmpty()) itemList.addAll(parcelableList)
		

		val viewPager = binding.viewPagerItemHolder

		val mViewPagerAdapter = ViewPageAdapter(this, itemList)
		
        viewPager.adapter = mViewPagerAdapter
		mViewPagerAdapter.notifyDataSetChanged()
  
		val currPage = myData.id?.toInt() ?: 0
		viewPager.setCurrentItem(currPage, false)
	}
}