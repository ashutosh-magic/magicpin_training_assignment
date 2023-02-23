package com.example.project_rxjava_api_calling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.viewpager.widget.ViewPager
import com.example.project_rxjava_api_calling.Adapter.ViewPageAdapter

import com.example.project_rxjava_api_calling.Model.ImageModelItem
import com.google.gson.Gson

@Suppress("DEPRECATION")
class PagerActivity : AppCompatActivity() {
    private val itemList: MutableList<ImageModelItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        val gson = Gson()

        val myData = gson.fromJson(intent.getStringExtra("item"), ImageModelItem::class.java)
        val parcelableList: List<ImageModelItem>? = intent.getParcelableArrayListExtra("IMAGE_LIST")
//        val imageList: List<ImageModelItem> = parcelableList.map { it as ImageModelItem }

        if(!parcelableList.isNullOrEmpty()) {
            itemList.addAll(parcelableList)
        }

        val viewPager = findViewById<ViewPager>(R.id.viewPagerItemHolder)


//        itemList = parcelableList
        val mViewPagerAdapter = ViewPageAdapter(this, itemList)
        viewPager.adapter = mViewPagerAdapter
        mViewPagerAdapter.notifyDataSetChanged()
//        Log.d("mydata",myData.toString())
        val currPage = myData.id?.toInt()?:0
//        while (currPage != null && currPage > (viewPager.adapter?.count ?: 0)) {
//            // Sleep for some time to avoid excessive CPU usage
//            Thread.sleep(100)
//        }
//        viewPager.post {
//            if (currPage != null) {
//                viewPager.setCurrentItem(currPage, false)
//            }
//        }
        viewPager.setCurrentItem(currPage, false)


    }
}