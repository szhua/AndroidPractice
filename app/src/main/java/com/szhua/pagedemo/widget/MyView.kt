package com.szhua.pagedemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.LogUtils

class MyView : View {

    private lateinit var  mBgColorPaint: Paint
    private lateinit var  mTextPaint: Paint
    private lateinit var  mBgColorPaint1: Paint
    private lateinit var  mTextPaint1: Paint

    constructor(context: Context?) : this(context,null,0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs ,0 )
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
         init()
    }


    var mWidth = 0
    var mheight =0

    private fun init(){
        mBgColorPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mBgColorPaint.color=Color.BLUE

        mTextPaint =Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mTextPaint.color  =Color.BLACK
        mTextPaint.textSize=20f

        mBgColorPaint1 = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mBgColorPaint1.color=Color.YELLOW

        mTextPaint1 =Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mTextPaint1.color  =Color.WHITE
        mTextPaint1.textSize =20f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        LogUtils.i("onMeasure")


        mWidth = measuredWidth
        mheight =measuredHeight
        LogUtils.d(mWidth)
        LogUtils.d(mheight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        LogUtils.i("onSizeChanged")
        mWidth = w
        mheight =h
        LogUtils.d(mWidth)
        LogUtils.d(mheight)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0f,0f,800f,800f,mBgColorPaint)
        canvas?.drawText("sdfdsfdsfdsfdsfsdfdsfdsfdsfdsfdsfdsffdfsdfdsfsdfdsfsdfdsfsdfs",0f,400f,mTextPaint)

        canvas?.save()
        canvas?.clipRect(Rect(200,200,700,600))
        canvas?.drawRect(0f,0f,800f,800f,mBgColorPaint1)
        canvas?.drawText("sdfdsfdsfdsfdsfsdfdsfdsfdsfdsfdsfdsffdfsdfdsfsdfdsfsdfdsfsdfs",0f,400f,mTextPaint1)

        canvas?.restore()


    }
}