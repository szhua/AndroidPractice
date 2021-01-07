package com.szhua.pagedemo.widget

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import kotlin.math.max


class FLowLayoutManager(var lines: Int = Int.MAX_VALUE): RecyclerView.LayoutManager() {

    private var currentLineWidth = 0
    private var currentLineTop = 0
    private var itemPositions  = SparseArray<Rect>()
    private  var currentRow = Row()
    private var  rows = arrayListOf<Row>()
    private var totalHeight = 0
    private var verticalScrollOffset = 0


    override fun isAutoMeasureEnabled(): Boolean {
        return  true
    }


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return  RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
         recycler?.let {
             state?.let {
                 onLayoutChildrenMine(recycler, state)
             }
         }
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {


        LogUtils.d("totalHeight:$totalHeight")
        LogUtils.d("verticalScrollOffset:$verticalScrollOffset")
        LogUtils.d("dy:$dy")
        LogUtils.d("height:${height - paddingTop - paddingBottom}")


        var travelY =dy
        //上部停止；()
        if(verticalScrollOffset+travelY<0){
             //travelY =0  没有修正的作用 （）
             travelY = -verticalScrollOffset
             //travelY = 0
            //底部停止；
        }else if(verticalScrollOffset+travelY>totalHeight-(height-paddingTop-paddingBottom)){
            travelY = totalHeight-(height-paddingTop-paddingBottom)-verticalScrollOffset
          //  travelY =0
        }
        verticalScrollOffset+=travelY
        offsetChildrenVertical(-travelY)

        return  travelY
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    open fun setMaxRows(count: Int){
        this.lines= count
        requestLayout()
    }


    fun generateViewCacheBitmap(view: View): Bitmap? {
        view.destroyDrawingCache()
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(widthMeasureSpec, heightMeasureSpec)
        val width = view.measuredWidth
        val height = view.measuredHeight
        view.layout(0, 0, width, height)
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        // 请注意，必须要生成新的Bitmap
        // ImageView内部有对DrawingCache回收的机制
        return Bitmap.createBitmap(view.drawingCache)
    }



    private fun onLayoutChildrenMine(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
           if (itemCount==0) {
               LogUtils.d(" LogUtils.d('itemCOunt0')")
               return
           }
           if (state.isPreLayout){
               LogUtils.d(" LogUtils.d('preLayout')")
               return
           }
           removeAllViews()
           //
           detachAndScrapAttachedViews(recycler)

           rows.clear()
           itemPositions.clear()
           currentRow =Row()
           currentLineWidth=0
           totalHeight= 0

           currentLineTop =paddingTop

           for ( i in 0 until itemCount){

               val childAt = recycler.getViewForPosition(i)
               if (childAt.visibility == GONE){
                    continue
               }
               measureChildWithMargins(childAt, 0, 0)
               val childWidth  = getDecoratedMeasuredWidth(childAt)
               val childHeight = getDecoratedMeasuredHeight(childAt)

               //未换行
               if (currentLineWidth+childWidth<width-paddingLeft-paddingRight){
                   val itemLeft = paddingLeft + currentLineWidth
                   val itemRight= itemLeft+ childWidth
                   val itemTop = currentLineTop
                   var itemRect = itemPositions.get(i)
                   if(itemRect==null){
                       itemRect= Rect()
                   }

                   itemRect.set(itemLeft, itemTop, itemRight, itemTop + childHeight)
                   itemPositions.put(i, itemRect)
                   //最大的高度；
                   currentRow.cuTop =currentLineTop
                   currentRow.maxHeight =max(currentRow.maxHeight, childHeight)
                   currentRow.addView(Item(childHeight, childAt, itemRect))

                   currentLineWidth+=childWidth
               }else{
                   //对上一行的测算  。rows添加。当前的lineTop，当前的总高，（暂时和lineTop相同）
                   rows.add(currentRow)
                   currentLineTop+=currentRow.maxHeight
                   totalHeight +=currentRow.maxHeight

                   if(rows.size>=lines){
                       currentRow.maxHeight=0
                       break
                   }

                   //当前item ；
                   /// position 缓存；
                   val itemLeft =paddingLeft
                   val itemRight =itemLeft+childWidth
                   val itemTop = currentLineTop
                   var  itemRect = itemPositions.get(i)
                   if (itemRect==null){
                       itemRect = Rect()
                   }
                   itemRect.set(itemLeft, itemTop, itemRight, itemTop + childHeight)

                   itemPositions.put(i, itemRect)

                   //当前已用行宽
                   currentLineWidth = childWidth

                   //当前行信息
                   currentRow =Row()
                   currentRow.cuTop =currentLineTop
                   currentRow.maxHeight =childHeight
                   currentRow.addView(Item(childHeight, childAt, itemRect))
               }
           }

          if(currentRow.maxHeight!=0) {
              //最后一次；
              totalHeight += currentRow.maxHeight
              rows.add(currentRow)
          }

          totalHeight = max(totalHeight, height - paddingTop - paddingBottom)

           //布局填充 ；
           fillLayout(recycler, state)


    }

    private fun fillLayout(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
         if(state.isPreLayout)return
         rows.forEach { row ->
             val lineTop =row.cuTop
             val lineBottom =row.maxHeight+lineTop
             row.views.forEach {
                 val scrap = it.view
                 addView(scrap)
                 val rect =it.rect
                 layoutDecoratedWithMargins(
                     scrap,
                     rect.left,
                     rect.top - verticalScrollOffset,
                     rect.right,
                     rect.bottom - verticalScrollOffset
                 )
             }
         }
    }


    class  Row{
        var cuTop =0
        var maxHeight = 0
        var views = arrayListOf<Item>()
        fun addView(view: Item){
            views.add(view)
        }
    }

    class  Item(val height: Int = 0, var view: View, var rect: Rect)

}