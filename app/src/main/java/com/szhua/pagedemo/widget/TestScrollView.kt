package com.szhua.pagedemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.OverScroller
import android.widget.ScrollView
import android.widget.Scroller
import com.blankj.utilcode.util.LogUtils
import kotlin.math.max

class TestScrollView :ScrollView {

    constructor(context: Context):this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(
        context,
        attrs,
        defStyleAttr
    ){
        val scroller =OverScroller(context)
        scroller.isFinished
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)


    }


    override fun computeScroll() {
        super.computeScroll()
    }


    private fun cal(){
        //7000
        //7000
        val contentHeight: Int = height - paddingBottom - paddingTop
        var scrollRange = getChildAt(0).bottom
        val scrollY: Int = scrollY
        val overScrollBottom = max(0, scrollRange - contentHeight)
        if (scrollY < 0) {
            scrollRange -= scrollY
        } else if (scrollY > overScrollBottom) {
            scrollRange += scrollY - overScrollBottom
        }
        LogUtils.d(contentHeight,scrollY,getChildAt(0).bottom,scrollRange)

    }



    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        cal()
         val contentHeight: Int = height - paddingBottom - paddingTop
         LogUtils.d(contentHeight)
         LogUtils.d(computeVerticalScrollRange())
         LogUtils.d("onTouchEvent")
         super.onTouchEvent(ev)
         return  false
    }

    override fun executeKeyEvent(event: KeyEvent?): Boolean {
        LogUtils.d("executeKeyEvent")
        return super.executeKeyEvent(event)
    }


}