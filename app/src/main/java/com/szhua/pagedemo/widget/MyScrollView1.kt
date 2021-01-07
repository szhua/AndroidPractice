package com.szhua.pagedemo.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.LogUtils

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MyScrollView1(context: Context?, attrs: AttributeSet?) : ScrollView(context, attrs) {

    init {
        isNestedScrollingEnabled =true
    }



    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val intercept =  super.onInterceptTouchEvent(ev)
        LogUtils.d(intercept, "$ onTouchEvent ${MotionEvent.actionToString(ev!!.action)}")
        return   intercept
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val handled = super.onTouchEvent(ev)
//        if(scrollY==0){
//            LogUtils.d("scaley=0")
//            return  false
//        }
        return  handled
    }

}