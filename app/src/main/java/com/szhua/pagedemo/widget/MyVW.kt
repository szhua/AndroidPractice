package com.szhua.pagedemo.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.customview.widget.ViewDragHelper
import com.blankj.utilcode.util.LogUtils

class MyVW : LinearLayout {

    lateinit var  dragger : ViewDragHelper

    private var mDragState = 0

    val STATE_IDLE = 0

    /**
     * A view is currently being dragged. The position is currently changing as a result
     * of user input or simulated user input.
     */
    val STATE_DRAGGING = 1

    /**
     * A view is currently settling into place as a result of a fling or
     * predefined non-interactive motion.
     */
    val STATE_SETTLING = 2

    init {
        dragger = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                LogUtils.d(this@MyVW.dragger.capturedView)
                LogUtils.d("tryCaptureView")
                return true
            }

            override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
                LogUtils.d("onViewCaptured")
                super.onViewCaptured(capturedChild, activePointerId)
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                LogUtils.d("clampViewPositionHorizontal")
                return left
            }

            override fun getOrderedChildIndex(index: Int): Int {
                LogUtils.d("getOrderedChildIndex")
                return super.getOrderedChildIndex(index)
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                LogUtils.d("clampViewPositionVertical")
                return top
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                //return  (measuredWidth-child.measuredWidth)/4
                LogUtils.d("getViewHorizontalDragRange",measuredWidth-child.measuredWidth)
                return 0
            }

            override fun getViewVerticalDragRange(child: View): Int {
              //  return  measuredHeight-child.measuredHeight
                LogUtils.d("getViewVerticalDragRange")
                LogUtils.d(this@MyVW.dragger.touchSlop)
                return  measuredHeight-child.measuredHeight
            }
            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                super.onEdgeDragStarted(edgeFlags, pointerId)
                LogUtils.d("onEdgeDragStarted")
            }
        })
    }



    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {



    }






    private val gestureDetector = GestureDetector(context,
        object : GestureDetector.OnGestureListener {
            override fun onDown(e: MotionEvent?): Boolean {
                LogUtils.d("=======onDown")
                mDragState = STATE_IDLE
                return true
            }

            override fun onShowPress(e: MotionEvent?) {
                LogUtils.d("=======onShowPress")
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                LogUtils.d("=======onSingleTapUp")
                return true
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
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
//              LogUtils.d("=======longPress")
////                mDragState =STATE_DRAGGING
//                for (i in  0 until childCount){
//                    val view =  getChildAt(i)
//                    if(view is Button){
//                        e?.let {
//                            this@MyVW.dragger.captureChildView(view,it.getPointerId(e.pointerCount-1))
//                        }
//                    }
//                }
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




    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {

      // gestureDetector.onTouchEvent(event)
       event?.let {
           val intercept = dragger.shouldInterceptTouchEvent(it)
           return  intercept
       }
        val intercept = super.onInterceptHoverEvent(event)
        LogUtils.d(intercept)
        return  intercept
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogUtils.d("event",MotionEvent.actionToString(event!!.action))
        event?.let { dragger.processTouchEvent(it) }
        return true
    }


 }