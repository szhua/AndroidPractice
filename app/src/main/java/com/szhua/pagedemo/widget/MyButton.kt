package com.szhua.pagedemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import com.blankj.utilcode.util.LogUtils


class MyButton :AppCompatButton {

    constructor(context: Context):this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(
        context,
        attrs,
        defStyleAttr
    ){

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
         gestureDetector.onTouchEvent(event)
         return   super.onTouchEvent(event)

    }

    private val gestureDetector = GestureDetector(context,object : GestureDetector.OnGestureListener{
        override fun onDown(e: MotionEvent?): Boolean {
            LogUtils.d("=======onDown")
            return  true
        }
        override fun onShowPress(e: MotionEvent?) {
            LogUtils.d("=======onShowPress")
        }
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            LogUtils.d("=======onSingleTapUp")
            return  true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            LogUtils.d("=======scroll")
            LogUtils.d("${e1?.x}${e1?.y}")
            LogUtils.d("${e2?.x}${e2?.y}")
            LogUtils.d("=======scroll")
            return  true
        }

        override fun onLongPress(e: MotionEvent?) {
            LogUtils.d("=======longPress")
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            LogUtils.d("=======onFling")
            return true
        }
    })


}