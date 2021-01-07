package com.szhua.pagedemo.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.blankj.utilcode.util.LogUtils
import com.szhua.pagedemo.R
import com.szhua.pagedemo.adapter.FavoriteShoeAdapter2
import com.szhua.pagedemo.adapter.MyTagAdapter
import com.szhua.pagedemo.common.BaseConstant.SP_USER_ID
import com.szhua.pagedemo.utils.AppPrefsUtils
import com.szhua.pagedemo.viewmodel.CustomViewModelProvider
import com.szhua.pagedemo.viewmodel.FavoriteShoeModel
import com.szhua.pagedemo.widget.FLowLayoutManager
import com.szhua.pagedemo.widget.MyFrameLayout
import com.szhua.pagedemo.widget.MyView
import kotlinx.android.synthetic.main.activity_constraint_layout_u_i.*


class ConstraintLayoutUI : AppCompatActivity() {

    private val  favoriteShoeModel:FavoriteShoeModel  by  viewModels{
         CustomViewModelProvider.providerFavoriteModel(this, AppPrefsUtils.getLong(SP_USER_ID))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout_u_i)

        changeLines.setOnClickListener {
            changeLines.scrollBy(50,50)
            changeLines.animate().scaleX(0.8f)
        }

        info.setOnClickListener {
            LogUtils.d(it)
        }

//
//        view_pager.orientation = ORIENTATION_HORIZONTAL
//        view_pager.adapter = FavoriteShoeAdapter2(this)
//
//        favoriteShoeModel.shoes.observe(this){
//            LogUtils.d(it.size)
//            (view_pager.adapter as FavoriteShoeAdapter2).shoes = it
//        }
//
//
//        mylayout.setOnTouchListener { _, event ->
//            LogUtils.d(event)
//            false
//        }
//
//
//        val data = mutableListOf(
//            "111",
//            "2222",
//            "sdfdsfdsfdsfdsf",
//            "fdsfdsfdsfdsf",
//            "fdsfdsf",
//            "11",
//            "fdsfdsfdsfdsff",
//            "111",
//            "2222",
//            "sdfdsfdsfdsfdsf",
//            "fdsfdsfdsfdsf",
//            "fdsfdsf",
//            "11",
//            "fdsfdsfdsfdsff",
//            "111",
//            "2222",
//            "sdfdsfdsfdsfdsf",
//            "fdsfdsfdsfdsf",
//            "fdsfdsf",
//            "11",
//            "fdsfdsfdsfdsff",
//            "111",
//            "2222",
//            "fdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsf",
//            "fdsfdsfdsfdsf",
//            "fdsfdsf",
//            "11",
//            "fdsfdsfdsfdsff",
//            "111",
//            "2222",
//            "sdfdsfdsfdsfdsf",
//            "fdsfdsfdsfdsf",
//            "fdsfdsf",
//            "11",
//            "fdsfdsfdsfdsff",
//            "111",
//            "2222",
//            "sdfdsfdsfdsfdsf",
//            "fdsfdsfdsfdsf",
//            "fdsfdsf",
//            "11",
//            "fdsfdsfdsfdsff",
//            "111",
//            "2222",
//            "sdfdsfdsfdsfdsf",
//            "fdsfdsfdsfdsf",
//            "fdsfdsf",
//            "11",
//            "fdsfdsfdsfdsff"
//        )
//
//        (recycler_view.layoutManager as FLowLayoutManager).lines =3
//        recycler_view.adapter = MyTagAdapter(data)
//
//        changeLines.setOnClickListener {
//          val manager =  recycler_view.layoutManager as FLowLayoutManager
//          if(manager.lines>3){
//              manager.setMaxRows(3)
//              changeLines.text ="展开"
//          }else {
//              manager.setMaxRows(Int.MAX_VALUE)
//              changeLines.text ="收起"
//          }
//        }

      //flow_widget.referencedIds = intArrayOf(R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4)

//        val view1 = MyView(this)
//        val view2 = MyView(this)
//        val view3 = MyView(this)
//        val vp2 = MyFrameLayout(this)
//        vp2.addView(view1)
//        vp2.addView(view2)
//        vp2.addView(view3)
//
//
//        val view4 = MyView(this)
//        val view5 = MyView(this)
//        val view6 = MyView(this)
//
//        val vp3 = MyFrameLayout(this)
//        vp3.addView(view4)
//        vp3.addView(view5)
//        vp3.addView(view6)
//
//        val view7 = MyView(this)
//        val view8 = MyView(this)
//        val view9 = MyView(this)
//
//        val vp4 = MyFrameLayout(this)
//        vp4.addView(view7)
//        vp4.addView(view8)
//        vp4.addView(view9)
//
//        val vp1 = MyFrameLayout(this)
//        vp1.addView(vp2)
//        vp1.addView(vp3)
//        vp1.addView(vp4)
//
//        setContentView(vp1)
//        view1.name = "view1"
//        view2.name = "view2"
//        view3.name = "view3"
//        view4.name = "view4"
//        view5.name = "view5"
//        view6.name = "view6"
//        view7.name = "view7"
//        view8.name = "view8"
//        view9.name = "view9"
//        vp1.name ="vp1"
//        vp2.name ="vp2"
//        vp3.name ="vp3"
//        vp4.name ="vp4"


    }


    var count =0

//    override fun onResume() {
//        super.onResume()
//        count++
//        if(changeLines.width==0){
//            return
//        }
//        val bitmap = getBitmap(recycler_view)
//        bitmap?.let {
//            imageview.setImageBitmap(it)
//        }
//    }
//
//    private fun getBitmap(view: View): Bitmap? {
//        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        view.draw(canvas)
//        canvas.setBitmap(null)
//        return bitmap
//    }
//
//    private fun generateViewCacheBitmap(view: TextView): Bitmap? {
//        LogUtils.d("left${view.left}")
//        view.destroyDrawingCache()
//        val widthMeasureSpec: Int =
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        val heightMeasureSpec: Int =
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        view.measure(widthMeasureSpec, heightMeasureSpec)
//        val width: Int = view.measuredWidth
//        val height: Int = view.measuredHeight
//        LogUtils.d("width${view.left}")
//        if(view.left==0){
//            return  null
//        }
//       // view.layout(0, 0, width, height)
//        view.isDrawingCacheEnabled = true
//        view.buildDrawingCache()
//        // 请注意，必须要生成新的Bitmap
//        // ImageView内部有对DrawingCache回收的机制
//        return Bitmap.createBitmap(view.drawingCache)
//    }

}