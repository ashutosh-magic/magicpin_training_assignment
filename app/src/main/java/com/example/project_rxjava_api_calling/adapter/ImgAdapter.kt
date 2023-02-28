package com.example.project_rxjava_api_calling.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_rxjava_api_calling.Model.ImageModelItem
import com.example.project_rxjava_api_calling.Model.WrapImageModelItem
import com.example.project_rxjava_api_calling.R

private const val VIEW_TYPE_ONE = 1
//private const val VIEW_TYPE_TWO = 2

class ImgAdapter(
    private val context: Context,
    private val onClick: (ImageModelItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val imgList: MutableList<WrapImageModelItem> = mutableListOf()

    fun setData(imgAdapter: ImgAdapter, imglist: List<ImageModelItem>) {
        imgAdapter.imgList.clear()

        for (idx: Int in imglist.indices) {
            val item = WrapImageModelItem(1, imglist[idx])
            imgAdapter.imgList.add(item)
            if (idx % 4 == 0) {
                imgAdapter.imgList[idx].viewType = 2
            }
        }
        imgAdapter.notifyItemChanged(0, imgAdapter.imgList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_ONE) {
            return ImgView1Holder(
                LayoutInflater.from(parent.context).inflate(R.layout.type_one_row, parent, false)
            )
        }
        return ImgView2Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.type_two_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        Log.d("er", imgList.size.toString())
        return imgList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val img = imgList[position]

        if (img.viewType == VIEW_TYPE_ONE && holder is ImgView1Holder) {
            Glide.with(context).load(img.apiData.download_url).into(holder.image)
            holder.image.setOnClickListener { onClick(img.apiData) }
        }
        else if (holder is ImgView2Holder) {
            holder.imgId.text = position.toString()
            Glide.with(context).load(img.apiData.download_url).into(holder.image)
            holder.image.setOnClickListener { onClick(img.apiData) }
        }
    }

    inner class ImgView1Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.image1)
    }

    inner class ImgView2Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgId: TextView = itemView.findViewById(R.id.IndexText)
        val image: ImageView = itemView.findViewById(R.id.image2)
    }

    override fun getItemViewType(position: Int): Int {
        return imgList[position].viewType
    }


}