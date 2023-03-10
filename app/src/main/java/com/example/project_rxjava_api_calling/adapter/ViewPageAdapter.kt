package com.example.project_rxjava_api_calling.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.project_rxjava_api_calling.model.ImageModelItem
import com.example.project_rxjava_api_calling.databinding.ActivityPagerItemBinding

class ViewPageAdapter(private val mContext: Context, private val imglist: List<ImageModelItem>) :
	PagerAdapter() {
	
	override fun instantiateItem(container: ViewGroup, position: Int): Any {
		
		val layoutInflater = LayoutInflater.from(mContext)
		val binding = ActivityPagerItemBinding
			.inflate(layoutInflater, container, false)
		
		val item = imglist[position]
		
		with(binding) {
			textViewPager1.text = item.id
			textViewPager2.text = item.author
			Glide.with(mContext)
				.load(item.download_url)
				.into(imagePager)
		}
		container.addView(binding.root)
		return binding.root
	}
	
	override fun getCount(): Int {
		return imglist.size
	}
	
	override fun isViewFromObject(view: View, obj: Any): Boolean {
		return view === obj
	}
	
	override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
		val view = `object` as View
		container.removeView(view)
	}
}