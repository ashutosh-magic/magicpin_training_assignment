package com.example.project_rxjava_api_calling.adapter

import android.content.Context
import android.util.Log
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
import com.example.project_rxjava_api_calling.model.ExternalModelItem
import kotlin.math.log

private const val VIEW_TYPE_ONE = 1
//private const val VIEW_TYPE_TWO = 2

class ImgAdapter(
	private val context: Context,
	private val onClick: (ImageModelItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	private val imgList: MutableList<WrapImageModelItem> = mutableListOf()
	private val extItemList: MutableList<ExternalModelItem> = mutableListOf()
	fun setData(imgList: List<ImageModelItem>) {
		this.imgList.clear()
		
		imgList.forEachIndexed { index, img ->
			this.imgList
				.add(WrapImageModelItem(if (index % 4 == 0) 2 else 1, img))
		}
		this.notifyItemChanged(0, this.imgList.size)
	}
	
	fun setExternalData(extImgList: List<ExternalModelItem>) {
		this.extItemList.clear()
		
		extImgList.forEach { this.extItemList.add(it) }
		this.notifyItemChanged(0, this.extItemList.size)
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
//		return imgList.size
		return extItemList.size
	}
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//		Log.d("temp","$extItemList ")
//		val img = imgList[position]
		
//		if (img.viewType == VIEW_TYPE_ONE && holder is ImgView1Holder) {
//			Glide.with(context).load(img.apiData.download_url).into(holder.image)
//			holder.image.setOnClickListener { onClick(img.apiData) }
//		} else if (holder is ImgView2Holder) {
//			"$position external storage".also { holder.imgId.text = it }
//			Glide.with(context).load(extItemList[0].filePath).into(holder.image)
//			holder.image.setOnClickListener { onClick(img.apiData) }
//		}
		
		if(holder is ImgView2Holder)
		{
			val path = extItemList[position].filePath
			
			if (path?.endsWith(".gif") == true) {
				"gif external storage".also { holder.imgId.text = it }
				Glide.with(holder.itemView).asGif().load(path).into(holder.image)
			} else {
				"image external storage".also { holder.imgId.text = it }
				Glide.with(holder.itemView).load(path).into(holder.image)
			}
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
//	   return imgList[position].viewType
	return 2
	}
}