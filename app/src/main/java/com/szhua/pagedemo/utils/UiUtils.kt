package com.szhua.pagedemo.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes


object UiUtils {

    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }



    fun getViewMeasuredHeight(tv: TextView): Int {
        tv.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return tv.measuredHeight
    }

    /**
     * dp转px
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * dp转px
     */
    fun dip2px(dpValue: Float): Int {
        val scale: Float = Resources.getSystem().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun hasEmpty(edits: List<TextView>): Boolean {
        for (editText in edits) {
            if (TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })) {
                return true
            }
        }
        return false
    }

    fun hasEmpty(vararg edits: TextView): Boolean {
        for (editText in edits) {
            if (TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })) {
                return true
            }
        }
        return false
    }

    fun hasEmpty(edits: Array<ImageView>): Boolean {
        for (imageView in edits) {
            if (TextUtils.isEmpty(imageView.getTag().toString().trim())) {
                return true
            }
        }
        return false
    }


    fun inflaterLayout(context: Context?, @LayoutRes layoutRes: Int): View? {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(layoutRes, null)
    }

    /**
     * 圆角Drawable
     *
     * @param radius 圆角
     * @param color  填充颜色
     */
    fun getShapeDrawable(radius: Int, @ColorInt color: Int): GradientDrawable? {
        val gd = GradientDrawable()
        gd.setColor(color)
        gd.cornerRadius = radius.toFloat()
        return gd
    }


}