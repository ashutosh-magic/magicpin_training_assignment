package com.example.project_rxjava_api_calling
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.viewpager.widget.ViewPager
import com.example.project_rxjava_api_calling.adapter.ViewPageAdapter

import com.example.project_rxjava_api_calling.model.ImageModelItem
import com.google.gson.Gson

@Suppress("DEPRECATION")
class PagerActivity : AppCompatActivity() {
    private lateinit var itemList:List<ImageModelItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        val gson = Gson()

        val myData=gson.fromJson(intent.getStringExtra("item"), ImageModelItem::class.java)
        val parcelableList: ArrayList<Parcelable> = intent.getParcelableArrayListExtra<Parcelable>("IMAGE_LIST") as ArrayList<Parcelable>
        val imageList: List<ImageModelItem> = parcelableList.map { it as ImageModelItem }

        val viewPager = findViewById<ViewPager>(R.id.viewPagerItemHolder)


        itemList=imageList
        val mViewPagerAdapter = ViewPageAdapter(this, itemList)
        viewPager.adapter = mViewPagerAdapter
        mViewPagerAdapter.notifyDataSetChanged()
//        Log.d("mydata",myData.toString())
        val currPage=myData.id?.toInt()
//        while (currPage != null && currPage > (viewPager.adapter?.count ?: 0)) {
//            // Sleep for some time to avoid excessive CPU usage
//            Thread.sleep(100)
//        }
        viewPager.post {
            if (currPage != null) {
                viewPager.setCurrentItem(currPage, false)
            }
        }


    }
}