package com.szhua.pagedemo.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.LogUtils

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MyScrollView(context: Context?, attrs: AttributeSet?) : ScrollView(context, attrs) {


    init {
        isNestedScrollingEnabled=true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
       return   super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val handled = super.onTouchEvent(ev)
        LogUtils.d("MyScrollView", "$ onTouchEvent ${MotionEvent.actionToString(ev!!.action)}")
        return  handled
    }








    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        super.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
       // LogUtils.d("onScrollChange",t)
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
       // LogUtils.d("onOVerscroll",scrollY)

        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    override fun onNestedScroll(
        target: View?,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }




}