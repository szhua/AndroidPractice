package com.szhua.pagedemo.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.szhua.pagedemo.R
import com.szhua.pagedemo.utils.UiUtils
import kotlin.math.abs


class MultiSwitch  : View {
    /**
     * 滑块的状态
     */
    enum class ThumbStatus {
        STATUS_STATIC, STATUS_DRAG, STATUS_ANIMATION
    }

    class  ThumbState{
        var pos = 0
        var offset = 0
        var canDrag = false
        var state: ThumbStatus = ThumbStatus.STATUS_STATIC
        constructor(pos: Int, offset: Int) {
            this.pos = pos
            this.offset = offset
        }
    }

     companion object{
         val TAG = "MultiSwitch"
     }

    enum class SwitchShape{
        RECT,
        OVAl
    }

    enum class SwitchType{
        TEXT,
        ICON
    }

    private val defaultWidth by lazy {
        UiUtils.dip2px(112f)
    }
    private val defaultHeight by lazy {
        UiUtils.dip2px(56f)
    }
    private val cornerRadius by lazy {
        UiUtils.dip2px(4.0f)
    }

    // 类型
    private var mType: SwitchType? = null

    // 图标资源
    private lateinit var   mIconRes: IntArray

    // 文本内容
    private  var   mItems: Array<String> = arrayOf()

    // 内容数量
    private  var mItemCount = 0

    // 内容的横向坐标
    private  lateinit  var  mItemCoordinate: IntArray


    private var mShape: SwitchShape? = null

    // 背景色
    private var  mBgColor = 0

    // 背景文本 Or Icon 颜色
    private var  mBgTextColor = 0

    // 滑块颜色
    private var  mThumbColor = 0

    // 滑块上文本 or Icon 颜色
    private var  mThumbTextColor = 0

    // 一个Item所占的长和宽
    private var  mWidth = 0
    private var  mHeight = 0
    private var  mAveWidth = 0

    // 文本大小
    private var  mTextSize = 0

    // 图标大小
    private var  mIconSize = 0

    // 边框大小
    private var  mThumbBorderWidth = 0

    private lateinit var   mBgColorPaint: Paint
    private lateinit var  mBgTextPaint: Paint
    private lateinit var  mThumbColorPaint: Paint
    private lateinit var  mThumbTextPaint: Paint
    private lateinit var  mThumbBorderPaint: Paint

    // 当前滑块的状态
    private lateinit var  mThumbState: ThumbState
    private var  mThumbMargin = 0

    // 动画
    private var  mValueAnimator: ValueAnimator? = null
    private var  canSelect = true


    // 点击时间
    private var pressTime: Long = 0



     constructor(context: Context):this(context, null, 0)
     constructor(context: Context, attrs: AttributeSet):this(context, attrs, 0)
     constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(
         context,
         attrs,
         defStyleAttr
     ){
         init(context, attrs)
     }

    /**
     * 设置选择项内容
     */
    fun setItemsArray(items: Array<String>) {
        if (items.size <= 1) throw UnsupportedOperationException("items'length can't be null or smaller than 1")
        if (mType === SwitchType.ICON) return
        mItems=items
        mItemCount = items.size
        mItemCoordinate = IntArray(items.size + 1)
        fillArray()
        invalidate()
    }

     private fun init(context: Context, attrs: AttributeSet?){
         val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiSwitch)

         mBgColor = typedArray.getColor(
             R.styleable.MultiSwitch_msBackgroundColor, resources.getColor(
                 R.color.textSecond
             )
         )
         mBgTextColor = typedArray.getColor(
             R.styleable.MultiSwitch_msNormalTextColor, resources.getColor(
                 R.color.textPrimary
             )
         )
         mThumbColor = typedArray.getColor(
             R.styleable.MultiSwitch_msThumbColor, resources.getColor(
                 R.color.colorPrimary
             )
         )
         mThumbTextColor = typedArray.getColor(
             R.styleable.MultiSwitch_msThumbTextColor,
             resources.getColor(R.color.white)
         )
         mTextSize = typedArray.getDimensionPixelSize(
             R.styleable.MultiSwitch_msTextSize,
             UiUtils.sp2px(context, 20f)
         )
         mIconSize = typedArray.getDimensionPixelSize(
             R.styleable.MultiSwitch_msIconSize,
             UiUtils.dip2px(context, 24f)
         )
         mType = SwitchType.values()[typedArray.getInt(
             R.styleable.MultiSwitch_msType,
             SwitchType.TEXT.ordinal
         )]
         mShape = SwitchShape.values()[typedArray.getInt(
             R.styleable.MultiSwitch_msShape,
             SwitchShape.RECT.ordinal
         )]
         mThumbMargin = typedArray.getDimensionPixelOffset(
             R.styleable.MultiSwitch_msThumbMargin,
             UiUtils.dip2px(2f)
         )
         mThumbBorderWidth = typedArray.getDimensionPixelOffset(
             R.styleable.MultiSwitch_msThumbBorderWidth,
             UiUtils.dip2px(0f)
         )
         val thumbBorderColor = typedArray.getColor(
             R.styleable.MultiSwitch_msThumbBorderColor,
             resources.getColor(R.color.white)
         )
         // TODO 设置默认位置

         // 初始化画笔
         // TODO 设置默认位置

         // 初始化画笔
         mBgColorPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
         mBgColorPaint.color = mBgColor
         mBgTextPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
         mBgTextPaint.textSize = mTextSize.toFloat()
         mBgTextPaint.color = mBgTextColor
         mThumbColorPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
         mThumbColorPaint.color = mThumbColor
         mThumbBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
         mThumbBorderPaint.style = Paint.Style.STROKE
         mThumbBorderPaint.strokeWidth = mThumbBorderWidth.toFloat()
         mThumbBorderPaint.color = thumbBorderColor
         mThumbTextPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
         mThumbTextPaint.textSize = mTextSize.toFloat()
         mThumbTextPaint.color = mThumbTextColor

         // 初始化滑块状态
         // 初始化滑块状态
         mThumbState = ThumbState(0, 0)

     }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode =MeasureSpec.getMode(heightMeasureSpec)
        var widthSpec = widthMeasureSpec
        var heightSpec =heightMeasureSpec
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            widthSpec = MeasureSpec.makeMeasureSpec(defaultWidth * mItemCount, MeasureSpec.EXACTLY)
        }
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            heightSpec = MeasureSpec.makeMeasureSpec(defaultHeight, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthSpec, heightSpec)

        val w = MeasureSpec.getSize(widthSpec)
        val h = MeasureSpec.getSize(heightSpec)
        if (w != 0) {
            mWidth = w
        }
        if (h != 0) {
            mHeight = h
        }
        if (mWidth != 0 && mItemCount != 0) {
            fillArray()
        }
        if (mHeight != 0) {
            top = 0
            bottom = top + mHeight
        }
    }

    private fun fillArray() {
        if (mWidth==0)return
        val aveWidth = mWidth/mItemCount
        var reminder = mWidth % mItemCount
        var startLeft = 0
        mItemCoordinate[0] =startLeft
        for ((index, _) in mItemCoordinate.withIndex()) {
            if (index==0) continue
            startLeft +=aveWidth
            if (reminder>0){
                startLeft++
                reminder--
            }
            mItemCoordinate[index] = startLeft
        }
        mAveWidth = aveWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        fillArray()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (mItemCount==0) return

        val l = mItemCoordinate[0]
        val r = mItemCoordinate[mItemCoordinate.size - 1]
        val t = top
        val b =bottom


        mBgColorPaint.style =  Paint.Style.FILL
        val radius: Int
        if(mShape == SwitchShape.RECT){
            radius = cornerRadius
            drawRoundRect(canvas, l, t, r, b, radius, mBgColorPaint)
        }else{
            radius = mHeight / 2
            drawRoundRect(canvas, l, t, r, b, radius, mBgColorPaint)
        }


// 2. 绘制内容
        // 2. 绘制内容
        if (mType === SwitchType.TEXT) {
            // 绘制文字
            for (i in mItems.indices) {
                drawText(
                    canvas,
                    mItems[i], mItemCoordinate[i], top, mItemCoordinate[i + 1], bottom, mBgTextPaint
                )
            }
        } else {
            // 绘制图标
            for (i in mIconRes.indices) {
                drawIcon(
                    canvas,
                    mIconRes[i],
                    mItemCoordinate[i],
                    top,
                    mItemCoordinate[i + 1],
                    bottom,
                    mBgTextPaint
                )
            }
        }



        drawThumb(canvas)






    }

    private fun drawThumb(canvas: Canvas?) {
      val left = mItemCoordinate[mThumbState.pos]+mThumbState.offset
      val right =mItemCoordinate[mThumbState.pos + 1]+mThumbState.offset
      canvas?.save()
      val rect =  Rect(
          left + mThumbMargin,
          top + mThumbMargin,
          right - mThumbMargin,
          bottom - mThumbMargin
      )
      canvas?.clipRect(rect)
      val padding =mThumbMargin + mThumbBorderWidth
        if (mShape == SwitchShape.RECT) {
            drawRoundRect(
                canvas,
                left + padding,
                top + padding,
                right - padding,
                bottom - padding,
                cornerRadius,
                mThumbColorPaint
            )
            if (mThumbBorderWidth != 0)
                drawRoundRect(
                    canvas,
                    left + mThumbMargin,
                    top + mThumbMargin,
                    right - mThumbMargin,
                    bottom - mThumbMargin,
                    cornerRadius,
                    mThumbBorderPaint
                )

        } else {
            drawRoundRect(
                canvas,
                left + padding,
                top + padding,
                right - padding,
                bottom - padding,
                (bottom - top) / 2 - padding,
                mThumbColorPaint
            )
            if (mThumbBorderWidth != 0)
                drawRoundRect(
                    canvas,
                    left + mThumbMargin,
                    top + mThumbMargin,
                    right - mThumbMargin,
                    bottom - mThumbMargin,
                    (bottom - top) / 2 - mThumbMargin,
                    mThumbBorderPaint
                )
        }

        val first:Int
        val second :Int
        when {
            mThumbState.offset > 0 -> {
                first = mThumbState.pos
                second = first + 1
            }
            mThumbState.offset == 0 -> {
                first = mThumbState.pos
                second = -1
            }
            else -> {
                first = mThumbState.pos
                second = first - 1
            }
        }

        // 4. 绘制文字orIcon
        if (mType == SwitchType.TEXT) {
            drawText(
                canvas,
                mItems[first],
                mItemCoordinate[first],
                top,
                mItemCoordinate[first + 1],
                bottom,
                mThumbTextPaint
            )
            if (second != -1 && second <= mItemCount - 1) {
                drawText(
                    canvas,
                    mItems[second],
                    mItemCoordinate[second],
                    top,
                    mItemCoordinate[second + 1],
                    bottom,
                    mThumbTextPaint
                )
            }
        } else {
            drawIcon(
                canvas,
                mIconRes[first],
                mItemCoordinate[first],
                top,
                mItemCoordinate[first + 1],
                bottom,
                mThumbTextPaint
            )
            if (second >= 0) {
                drawIcon(
                    canvas,
                    mIconRes[second],
                    mItemCoordinate[second],
                    top,
                    mItemCoordinate[second + 1],
                    bottom,
                    mThumbTextPaint
                )
            }
        }




        canvas?.restore();



    }

    private fun drawRoundRect(
        canvas: Canvas?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        radius: Int,
        paint: Paint?
    ) {
        val rect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        canvas?.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint!!)
    }


    /**
     * 绘制文本
     */
    private fun drawText(
        canvas: Canvas?,
        text: String,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        paint: Paint
    ) {
        val w = right - left
        val h = bottom - top
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val fontMetrics = paint.fontMetrics
        val x: Int = left + (w / 2 - bounds.width() / 2)
        val y = ((h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top).toInt()
        canvas?.drawText(text, x.toFloat(), y.toFloat(), paint)
    }

    /**
     * 绘制图标
     */
    private fun drawIcon(
        canvas: Canvas?,
        res: Int,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        paint: Paint
    ) {
        var bitmap: Bitmap = getBitmap(context, res, paint.color)
        bitmap = zoomImg(bitmap, mIconSize, mIconSize)
        val cx = left + (right - left - bitmap.width) / 2
        val cy = top + (bottom - top - bitmap.height) / 2
        canvas?.drawBitmap(bitmap, cx.toFloat(), cy.toFloat(), paint)
    }


    /**
     * 对矢量图进行获取
     */
    private fun getBitmap(context: Context, vectorDrawableId: Int, color: Int): Bitmap {
        val bitmap: Bitmap
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable = context.getDrawable(vectorDrawableId)
            vectorDrawable!!.setTint(color)
            bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, vectorDrawableId)
        }
        return bitmap
    }


    /**
     * 对Bitmap进行缩放
     */
    private fun zoomImg(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        // 获得图片的宽高
        val width = bm.width
        val height = bm.height
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true)
    }


    /**
     * 检测移动状态
     */
    private fun checkDrag() {
        val l = mItemCoordinate[mThumbState.pos] + mThumbState.offset + mThumbMargin
        val r = mItemCoordinate[mThumbState.pos + 1] + mThumbState.offset - mThumbMargin
        val t = top + mThumbMargin
        val b = bottom - mThumbMargin
        if (curX >= l && curX <= r && curY >= t && curY <= b) {
            mThumbState.canDrag = true
        }
    }


    private var  curX = 0f
    private  var curY=0f
    private var  lastX = 0f
    private  var lastY=0f


    private val gestureDetector = GestureDetector(context,object :GestureDetector.OnGestureListener{
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


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        gestureDetector.onTouchEvent(event)


        if (mItemCount<=0 || !canSelect) return  true
        if (event==null) return true
        curX = event.x
        curY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                checkDrag()
                if (mThumbState.canDrag && mValueAnimator != null && mValueAnimator!!.isRunning) {
                    mValueAnimator!!.cancel()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mThumbState.canDrag) {
                    val deltaX = curX - lastX
                    if (deltaX != 0f) {
                        mThumbState.state = ThumbStatus.STATUS_DRAG
                        handleMove(deltaX)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                var pos = 0
                val isClick: Boolean = System.currentTimeMillis() - pressTime <= 300
                pos = calculatePos(curX, isClick)
                animate(mItemCoordinate[pos].toFloat())
                mThumbState.canDrag = false
            }
            MotionEvent.ACTION_CANCEL -> {
                val pos: Int = calculatePos(lastX, false)
                animate(mItemCoordinate[pos].toFloat())
                mThumbState.canDrag = false
            }
        }
        lastX = curX
        lastY = curY
        return true
    }


    /**
     * 处理移动
     *
     * @param dx 滑动距离
     */
    private fun handleMove(dx: Float) {
        // 如果当前处于最后一个或者第一个
        if (mThumbState.pos > mItemCount - 1) {
            mThumbState.pos = mItemCount - 1
            mThumbState.offset = 0
            return
        }
        if (mThumbState.pos < 0) {
            mThumbState.pos = 0
            mThumbState.offset = 0
            return
        }

        // 判断当前位置是否越界
        val targetOffset = mItemCoordinate[mThumbState.pos] + mThumbState.offset + dx
        if (targetOffset > mItemCoordinate[mItemCount - 1]) {
            mThumbState.pos = mItemCount - 1
            mThumbState.offset = 0
            return
        } else if (targetOffset < mItemCoordinate[0]) {
            mThumbState.pos = 0
            mThumbState.offset = 0
            return
        }
        mThumbState.offset += dx.toInt()
        if (abs(mThumbState.offset) >= mAveWidth) {
            var pos = mThumbState.pos
            if (mThumbState.offset > 0) {
                while (mItemCoordinate[pos] + mThumbState.offset >= mItemCoordinate[pos + 1]) {
                    pos++
                    mThumbState.offset -= mItemCoordinate[pos] - mItemCoordinate[pos - 1]
                }
            } else {
                while (pos != 0 && mItemCoordinate[pos] + mThumbState.offset <= mItemCoordinate[pos - 1]) {
                    pos--
                    mThumbState.offset += mItemCoordinate[pos + 1] - mItemCoordinate[pos]
                }
            }
            mThumbState.pos = pos
        }
//        if (mListener != null) {
//            val percent = mThumbState.offset.toFloat() / mAveWidth
//            mListener.onPositionOffsetPercent(mThumbState.pos, percent)
//        }
        postInvalidate()
    }

    private fun calculatePos(x: Float, isClick: Boolean): Int {
        var targetPos = mThumbState.pos
        if (isClick) {
            targetPos = (x / mAveWidth).toInt()
        } else {
            if (Math.abs(mThumbState.offset) > mAveWidth / 2) {
                if (mThumbState.offset > 0) {
                    targetPos = mThumbState.pos + 1
                }
                if (mThumbState.offset < 0) {
                    targetPos = mThumbState.pos - 1
                }
            }
        }

        // 判断当前位置是否越界
        if (targetPos < 0) targetPos = 0 else if (targetPos >= mItemCount) targetPos =
            mItemCount - 1
        return targetPos
    }

    private fun animate(x: Float) {
        val startX = mItemCoordinate[mThumbState.pos] + mThumbState.offset
        mValueAnimator = ValueAnimator.ofFloat(startX.toFloat(), x)
        mValueAnimator?.duration = 200
        mValueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            val cx = animation.animatedValue as Float
            val x = (cx / mAveWidth).toInt()
            mThumbState.pos = x
            mThumbState.offset = (cx - mItemCoordinate[mThumbState.pos]).toInt()
            postInvalidate()
        })
        mValueAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator?) {
                super.onAnimationCancel(animation)
                mThumbState.state = ThumbStatus.STATUS_STATIC
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mThumbState.state = ThumbStatus.STATUS_STATIC
//                if (mListener != null) {
//                    mListener.onPositionSelected(mThumbState.pos)
//                }
            }

            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                mThumbState.state = ThumbStatus.STATUS_ANIMATION
            }
        })
        mValueAnimator?.start()
    }


}