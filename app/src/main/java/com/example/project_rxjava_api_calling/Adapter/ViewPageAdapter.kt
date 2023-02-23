package com.example.project_rxjava_api_calling.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.project_rxjava_api_calling.Model.ImageModelItem
import com.example.project_rxjava_api_calling.R

class ViewPageAdapter(private val mContext:Context,private val imglist: List<ImageModelItem>): PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater!!.inflate(R.layout.activity_pager_item, container, false)
        val item=imglist[position]
        val pagerImage:ImageView=view.findViewById(R.id.image_pager)
//        val textViewPager1:TextView=view.findViewById(R.id.textViewPager1)
//        val textViewPager2:TextView=view.findViewById(R.id.textViewPager2)
//
//        textViewPager1.text=item.id
//        textViewPager2.text=item.author
        Glide.with(mContext)
            .load(item.download_url)
            .into(pagerImage)

        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        Log.d("pagerItemCount", "getCount: ${imglist.size}")
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