package com.example.project_rxjava_api_calling.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_rxjava_api_calling.model.ImageModelItem
import com.example.project_rxjava_api_calling.model.WrapImageModelItem
import com.example.project_rxjava_api_calling.databinding.TypeOneRowBinding
import com.example.project_rxjava_api_calling.databinding.TypeTwoRowBinding

private const val VIEW_TYPE_ONE = 1
//private const val VIEW_TYPE_TWO = 2

class ImgAdapter(
	private val context: Context,
	private val onClick: (ImageModelItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	private val imgList: MutableList<WrapImageModelItem> = mutableListOf()
	
	fun setData(imgList: List<ImageModelItem>) {
		this.imgList.clear()
		
		imgList.forEachIndexed { index, img ->
			this.imgList
				.add(WrapImageModelItem(if (index % 4 == 0) 2 else 1, img))
		}
		this.notifyItemChanged(0, this.imgList.size)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		
		val layoutInflater = LayoutInflater.from(parent.context)
		
		if (viewType == VIEW_TYPE_ONE) {
			val binding = TypeOneRowBinding
				.inflate(layoutInflater, parent, false)
			return ImgView1Holder(binding)
		}
		val binding = TypeTwoRowBinding
			.inflate(layoutInflater, parent, false)
		return ImgView2Holder(binding)
	}
	
	override fun getItemCount(): Int {
		return imgList.size
	}
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		
		val img = imgList[position]
		
		if (img.viewType == VIEW_TYPE_ONE && holder is ImgView1Holder) {
			Glide.with(context).load(img.apiData.download_url).into(holder.image)
			holder.image.setOnClickListener { onClick(img.apiData) }
		} else if (holder is ImgView2Holder) {
			holder.imgId.text = position.toString()
			Glide.with(context).load(img.apiData.download_url).into(holder.image)
			holder.image.setOnClickListener { onClick(img.apiData) }
		}
	}
	
	inner class ImgView1Holder(binding: TypeOneRowBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val image: ImageView = binding.image1
	}
	
	inner class ImgView2Holder(binding: TypeTwoRowBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val imgId: TextView = binding.IndexText
		val image: ImageView = binding.image2
	}
	
	override fun getItemViewType(position: Int): Int {
		return imgList[position].viewType
	}
}