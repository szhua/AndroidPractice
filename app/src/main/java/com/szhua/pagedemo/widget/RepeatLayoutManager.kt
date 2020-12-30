package com.szhua.pagedemo.widget

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.blankj.utilcode.util.LogUtils


class RepeatLayoutManager : RecyclerView.LayoutManager() {





    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun canScrollHorizontally(): Boolean {
        return  true
    }


    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        if (itemCount<=0){
            return
        }
        if (state?.isPreLayout == true){
            return
        }
        recycler?.let { detachAndScrapAttachedViews(it) }
        var  itemLeft =paddingLeft
        for (i in 0..Int.MAX_VALUE){
            if (itemLeft>=width-paddingRight){
                break
            }
            val itemView = recycler?.getViewForPosition(i)
            itemView?.let {
                addView(it)
                measureChildWithMargins(it, 0, 0)
                val right = itemLeft+getDecoratedMeasuredWidth(itemView)
                val top = paddingTop
                val bottom =top+ getDecoratedMeasuredHeight(itemView)-paddingBottom
                layoutDecorated(it, itemLeft, top, right, bottom)
                itemLeft = right
            }
        }





    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {

        LogUtils.d(childCount)

         recycler?.let {
             fill(it, dx > 0)
         }
         offsetChildrenHorizontal(-dx)
         recycler?.let {
             recyclerChildView(dx>0,it)
         }
         return  dx
    }

    private fun fill(recycler: RecyclerView.Recycler, fillEnd: Boolean) {
            if (childCount==0){
                return
            }
            if (fillEnd){
                var anchorView  =getChildAt(childCount - 1)
                if (anchorView!=null){
                    val anchorPosition  =getPosition(anchorView)
                    while (anchorView!!.right < width - paddingRight){
                        LogUtils.d("fillRightChildView")
                        var position = (anchorPosition+1)%itemCount
                        if (position<0) position+=itemCount
                        val scrapItem  = recycler.getViewForPosition(position)
                        scrapItem.let {
                            addView(it)
                            measureChildWithMargins(it, 0, 0)
                            val left = anchorView!!.right
                            val right = left+getDecoratedMeasuredWidth(it)
                            val top = paddingTop
                            val bottom =top+ getDecoratedMeasuredHeight(it)-paddingBottom
                            layoutDecorated(it, left, top, right, bottom)
                        }
                        anchorView = scrapItem
                    }
                }
            }else{
                var anchorView  =getChildAt(0)
                if (anchorView!=null) {
                    val anchorPosition = getPosition(anchorView!!)
                    while (anchorView!!.left>paddingLeft){
                        var position = (anchorPosition-1)%itemCount
                        if (position<0) position +=itemCount
                        val scrapItem  = recycler.getViewForPosition(position)
                        scrapItem.let {
                            addView(scrapItem,0)
                            measureChildWithMargins(it, 0, 0)
                            val right = anchorView!!.left
                            val left = right-getDecoratedMeasuredWidth(it)
                            val top = paddingTop
                            val bottom =top+ getDecoratedMeasuredHeight(it)-paddingBottom
                            layoutDecorated(it, left, top, right, bottom)
                        }
                        anchorView = scrapItem
                    }
                }
            }
    }


    /**
     * 回收不可见的子View
     */
    private fun recyclerChildView(fillEnd: Boolean, recycler: Recycler) {
        if (fillEnd) {
            //回收头部
            var i = 0
            while (true) {
                val view: View? = getChildAt(i)
                val needRecycler = view != null && view.right < paddingLeft
                if (needRecycler) {
                    view?.let { removeAndRecycleView(it, recycler) }
                } else {
                    return
                }
                i++
            }
        } else {
            //回收尾部
            var i = childCount - 1
            while (true) {
                val view: View? = getChildAt(i)
                val needRecycler = view != null && view.left > width - paddingRight
                if (needRecycler) {
                    view?.let { removeAndRecycleView(it, recycler) }
                } else {
                    return
                }
                i--
            }
        }
    }




}