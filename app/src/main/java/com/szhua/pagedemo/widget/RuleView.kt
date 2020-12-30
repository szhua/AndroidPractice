package com.szhua.pagedemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.Scroller
import com.blankj.utilcode.util.LogUtils
import com.szhua.pagedemo.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class RuleView : View {

    /**
     * 滑动阈值
     */
    private var  TOUCH_SLOP = 0
    /**
     * 惯性滑动最小、最大速度
     */
    private var  MIN_FLING_VELOCITY = 0
    private var  MAX_FLING_VELOCITY = 0
    /**
     * 背景色
     */
    private var bgColor = 0

    /**
     * 刻度颜色
     */
    private var gradationColor = 0

    /**
     * 短刻度线宽度
     */
    private var shortLineWidth = 0f

    /**
     * 长刻度线宽度
     * 默认 = 2 * shortLineWidth
     */
    private var longLineWidth = 0f

    /**
     * 短刻度长度
     */
    private var shortGradationLen = 0f

    /**
     * 长刻度长度
     * 默认为短刻度的2倍
     */
    private var longGradationLen = 0f

    /**
     * 刻度字体颜色
     */
    private var textColor = 0

    /**
     * 刻度字体大小
     */
    private var textSize = 0f

    /**
     * 中间指针线颜色
     */
    private var indicatorLineColor = 0

    /**
     * 中间指针线宽度
     */
    private var indicatorLineWidth = 0f

    /**
     * 中间指针线长度
     */
    private var indicatorLineLen = 0f

    /**
     * 最小值
     */
    private var minValue = 0f

    /**
     * 最大值
     */
    private var maxValue = 0f

    /**
     * 当前值
     */
    private var currentValue = 0f

    /**
     * 刻度最小单位
     */
    private var gradationUnit = 0f

    /**
     * 需要绘制的数值
     */
    private var numberPerCount = 0

    /**
     * 刻度间距离
     */
    private var gradationGap = 0f

    /**
     * 刻度与文字的间距
     */
    private var gradationNumberGap = 0f

    /**
     * 最小数值，放大10倍：minValue * 10
     */
    private var mMinNumber = 0

    /**
     * 最大数值，放大10倍：maxValue * 10
     */
    private var mMaxNumber = 0

    /**
     * 当前数值
     */
    private var mCurrentNumber = 0

    /**
     * 最大数值与最小数值间的距离：(mMaxNumber - mMinNumber) / mNumberUnit * gradationGap
     */
    private var mNumberRangeDistance = 0f

    /**
     * 刻度数值最小单位：gradationUnit * 10
     */
    private var mNumberUnit = 0

    /**
     * 当前数值与最小值的距离：(mCurrentNumber - minValue) / mNumberUnit * gradationGap
     */
    private var mCurrentDistance = 0f

    /**
     * 控件宽度所占有的数值范围：mWidth / gradationGap * mNumberUnit
     */
    private var mWidthRangeNumber = 0

    /**
     * 普通画笔
     */
    private var mPaint: Paint

    /**
     * 文字画笔
     */
    private var  mTextPaint: TextPaint


    private  var mScroller:Scroller

    private val mRangeDistance = 0





    constructor(context: Context):this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(
        context,
        attrs,
        defStyleAttr
    ){


        val ta = context.obtainStyledAttributes(attrs, R.styleable.RuleView)
        bgColor = ta.getColor(R.styleable.RuleView_zjun_bgColor, Color.parseColor("#f5f8f5"))
        gradationColor = ta.getColor(R.styleable.RuleView_zjun_gradationColor, Color.LTGRAY)
        shortLineWidth = ta.getDimension(
            R.styleable.RuleView_gv_shortLineWidth,
            dp2px(1f).toFloat()
        )
        shortGradationLen = ta.getDimension(
            R.styleable.RuleView_gv_shortGradationLen,
            dp2px(16f).toFloat()
        )
        longGradationLen = ta.getDimension(
            R.styleable.RuleView_gv_longGradationLen,
            shortGradationLen * 2
        )
        longLineWidth = ta.getDimension(R.styleable.RuleView_gv_longLineWidth, shortLineWidth * 2)
        textColor = ta.getColor(R.styleable.RuleView_zjun_textColor, Color.BLACK)
        textSize = ta.getDimension(R.styleable.RuleView_zjun_textSize, sp2px(14f).toFloat())
        indicatorLineColor = ta.getColor(
            R.styleable.RuleView_zjun_indicatorLineColor, Color.parseColor(
                "#48b975"
            )
        )
        indicatorLineWidth = ta.getDimension(
            R.styleable.RuleView_zjun_indicatorLineWidth,
            dp2px(3f).toFloat()
        )
        indicatorLineLen = ta.getDimension(
            R.styleable.RuleView_gv_indicatorLineLen,
            dp2px(35f).toFloat()
        )
        minValue = ta.getFloat(R.styleable.RuleView_gv_minValue, 0f)
        maxValue = ta.getFloat(R.styleable.RuleView_gv_maxValue, 100f)
        currentValue = ta.getFloat(R.styleable.RuleView_gv_currentValue, 50f)
        gradationUnit = ta.getFloat(R.styleable.RuleView_gv_gradationUnit, .1f)
        numberPerCount = ta.getInt(R.styleable.RuleView_gv_numberPerCount, 10)
        gradationGap = ta.getDimension(R.styleable.RuleView_gv_gradationGap, dp2px(10f).toFloat())
        gradationNumberGap = ta.getDimension(
            R.styleable.RuleView_gv_gradationNumberGap,
            dp2px(8f).toFloat()
        )
        ta.recycle()


        // 初始化final常量，必须在构造中赋初值
        val viewConfiguration = ViewConfiguration.get(context)
        TOUCH_SLOP = viewConfiguration.scaledTouchSlop
        MIN_FLING_VELOCITY = viewConfiguration.scaledMinimumFlingVelocity
        MAX_FLING_VELOCITY = viewConfiguration.scaledMaximumFlingVelocity



        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.strokeWidth = shortLineWidth

        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.textSize = textSize
        mTextPaint.color = textColor


        convertValue2Number()

        mScroller = Scroller(context)

    }


    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    }

    private fun sp2px(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics).toInt()
    }

    /**
     * 尺寸
     */
    private var mWidth = 0
    /**
     * 尺寸
     */
    private  var mHalfWidth:Int = 0
    /**
     * 尺寸
     */
    private  var mHeight:Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
       mWidth = MeasureSpec.getSize(widthMeasureSpec)
       mHeight=MeasureSpec.getSize(heightMeasureSpec)
       val widthSpecMode =  MeasureSpec.getMode(widthMeasureSpec)
       if(widthSpecMode==MeasureSpec.AT_MOST){
          mWidth = dp2px(80f)
       }
       mHalfWidth = mWidth shr 1
        //最大范围;
       if (mWidthRangeNumber == 0) {
            mWidthRangeNumber = (mWidth / gradationGap * mNumberUnit).toInt()
        }
       setMeasuredDimension(mWidth, mHeight)
    }


    private var mDownX = 0
    private var  mLastX = 0
    private  var mLastY = 0
    private var  isMoved = false

    private  var  mVelocityTracker :VelocityTracker ?= null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val action = it.action
            val x =it.x.toInt()
            val y = it.y.toInt()

            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain()
            }
            mVelocityTracker?.addMovement(event)


            when(action){
                MotionEvent.ACTION_DOWN -> {
                    mDownX = it.x.toInt()
                    isMoved = false
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = x - mLastX;
                    // 判断是否已经滑动
                    if (!isMoved) {
                        val dy = y - mLastY
                        // 滑动的触发条件：水平滑动大于垂直滑动；滑动距离大于阈值
                        isMoved = abs(dx) > abs(dy) && abs(x - mDownX) >= TOUCH_SLOP
                    }
                    mCurrentDistance += -dx.toFloat()
                    calculateValue()
                }
                MotionEvent.ACTION_UP -> {
                    mVelocityTracker?.computeCurrentVelocity(1000, MAX_FLING_VELOCITY.toFloat())
                    val xVelocity = mVelocityTracker?.xVelocity
                    xVelocity?.let { it ->
                        if (abs(it) < MIN_FLING_VELOCITY) {
                            // 滑动刻度
                            scrollToGradation()
                        } else {

                            // 速度具有方向性，需要取反
                            mScroller.fling(
                                mCurrentDistance.toInt(), 0, (-xVelocity).toInt(), 0,
                                0, mNumberRangeDistance.toInt(), 0, 0
                            )
                            invalidate()
                        }
                    }


                }
            }
            mLastX = x
            mLastY = y
        }

        return  true
    }


    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScroller.currX != mScroller.finalX) {
                mCurrentDistance = mScroller.currX.toFloat()
                calculateValue()
            } else {
                scrollToGradation()
            }
        }
    }




    private fun scrollToGradation() {
        mCurrentNumber = mMinNumber + Math.round(mCurrentDistance / gradationGap) * mNumberUnit
        mCurrentNumber = min(max(mCurrentNumber, mMinNumber), mMaxNumber)
        mCurrentDistance = (mCurrentNumber - mMinNumber) / mNumberUnit * gradationGap
        currentValue = mCurrentNumber / 10f
        invalidate()
    }

    /**
     * 根据distance距离，计算数值
     */
    private fun calculateValue() {
        // 限定范围：在最小值与最大值之间
        mCurrentDistance = min(max(mCurrentDistance, 0f), mNumberRangeDistance)
        mCurrentNumber = mMinNumber + (mCurrentDistance / gradationGap).toInt() * mNumberUnit
        currentValue = mCurrentNumber / 10f
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        LogUtils.d("onDraw")
        canvas?.apply {
            this.drawColor(bgColor)
            /// indicators ;
            mPaint.color=gradationColor
            mPaint.strokeWidth =shortLineWidth
            val topLineY = shortLineWidth*0.5f
            //this.drawLine(0f,0f,mWidth.toFloat(),0f,mPaint)
            this.drawLine(0f, topLineY, mWidth.toFloat(), topLineY, mPaint)

            var startNumber = (mCurrentDistance -mHalfWidth).toInt()/gradationGap.toInt()*mNumberUnit+mMinNumber
            val expendUnit = startNumber shl 1
            startNumber-=expendUnit
            if(startNumber<mMinNumber){
                startNumber = mMinNumber
            }
            // 右侧扩展
            var rightMaxNum: Int = startNumber + expendUnit + mWidthRangeNumber + expendUnit
            if (rightMaxNum > mMaxNumber) {
                rightMaxNum = mMaxNumber
            }
            // 当前绘制刻度对应控件左侧的位置
            var distance: Float = mHalfWidth - (mCurrentDistance - (startNumber - mMinNumber) / mNumberUnit * gradationGap)
            val perUnitCount = mNumberUnit * numberPerCount

            while (startNumber<=rightMaxNum){
                // 短刻度

                if(startNumber%perUnitCount==0){

                    // 长刻度：刻度宽度为短刻度的2倍
                    mPaint.strokeWidth = longLineWidth
                    canvas.drawLine(distance, 0f, distance, longGradationLen, mPaint)

                    // 数值
                    // 数值
                    val fNum = startNumber / 10f
                    var text = fNum.toString()
                    if (text.endsWith(".0")) {
                        text = text.substring(0, text.length - 2)
                    }
                    val textWidth = mTextPaint.measureText(text)
                    canvas.drawText(
                        text,
                        distance - textWidth * .5f,
                        longGradationLen + gradationNumberGap + textSize,
                        mTextPaint
                    )
                }
                // 短刻度
                mPaint.strokeWidth = shortLineWidth
                canvas.drawLine(distance, 0f, distance, shortGradationLen, mPaint)
                startNumber += mNumberUnit
                distance += gradationGap
            }
        }
    }

    private fun convertValue2Number() {
        mMinNumber = (minValue * 10).toInt()
        mMaxNumber = (maxValue * 10).toInt()
        mCurrentNumber = (currentValue * 10).toInt()
        mNumberUnit = (gradationUnit * 10).toInt()
        mCurrentDistance = (mCurrentNumber - mMinNumber) / mNumberUnit * gradationGap
        mNumberRangeDistance = (mMaxNumber - mMinNumber) / mNumberUnit * gradationGap
        if (mWidth != 0) {
            // 初始化时，在onMeasure()里计算
            mWidthRangeNumber = (mWidth / gradationGap * mNumberUnit).toInt()
        }
    }


}