package com.szhua.pagedemo.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.LogUtils

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MyScrollView(context: Context?, attrs: AttributeSet?) : ScrollView(context, attrs) {


    init {
        isNestedScrollingEnabled=true
        isSmoothScrollingEnabled=false
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }


    override fun scrollTo(x: Int, y: Int) {
        LogUtils.d("new SCrollTo")
        super.scrollTo(x, y)
    }








}