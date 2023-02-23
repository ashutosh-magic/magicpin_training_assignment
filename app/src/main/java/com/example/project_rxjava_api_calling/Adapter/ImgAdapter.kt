package com.example.project_rxjava_api_calling.Adapter

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
import com.example.project_rxjava_api_calling.R


class ImgAdapter(private val context: Context, private var ImgList:MutableList<ImageModelItem>, private val onClick:(ImageModelItem)->Unit) :RecyclerView.Adapter<ImgAdapter.ImgViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
        return ImgViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.each_row,parent,false))
    }

    override fun getItemCount(): Int {
        Log.d("er", ImgList.size.toString())
        return ImgList.size
    }

    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        val img=ImgList[position]
        holder.imgId.text=img.id
        holder.name.text=img.author
        Glide.with(context)
            .load(img.download_url)
            .into(holder.image)
        holder.image.setOnClickListener{onClick(img)}

    }

    inner  class ImgViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val name:TextView=itemView.findViewById(R.id.name)
        val imgId: TextView =itemView.findViewById(R.id.imgId)
        val image: ImageView =itemView.findViewById(R.id.image)
    }

    companion object {

            fun setData(imgAdapter: ImgAdapter, imglist: List<ImageModelItem>)
            {
                imgAdapter.ImgList.clear()
                imgAdapter.ImgList.addAll(imglist)
                imgAdapter.notifyItemChanged(0, imgAdapter.ImgList.size)
            }
    }


}