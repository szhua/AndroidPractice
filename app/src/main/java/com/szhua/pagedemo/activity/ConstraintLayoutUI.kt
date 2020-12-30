package com.szhua.pagedemo.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.blankj.utilcode.util.LogUtils
import com.szhua.pagedemo.R
import com.szhua.pagedemo.adapter.FavoriteShoeAdapter2
import com.szhua.pagedemo.adapter.MyTagAdapter
import com.szhua.pagedemo.common.BaseConstant.SP_USER_ID
import com.szhua.pagedemo.utils.AppPrefsUtils
import com.szhua.pagedemo.viewmodel.CustomViewModelProvider
import com.szhua.pagedemo.viewmodel.FavoriteShoeModel
import com.szhua.pagedemo.widget.FLowLayoutManager
import kotlinx.android.synthetic.main.activity_constraint_layout_u_i.*

class ConstraintLayoutUI : AppCompatActivity() {

    private val  favoriteShoeModel:FavoriteShoeModel  by  viewModels{
         CustomViewModelProvider.providerFavoriteModel(this, AppPrefsUtils.getLong(SP_USER_ID))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout_u_i)

        view_pager.orientation = ORIENTATION_HORIZONTAL
        view_pager.adapter = FavoriteShoeAdapter2(this)

        favoriteShoeModel.shoes.observe(this){
            LogUtils.d(it.size)
            (view_pager.adapter as FavoriteShoeAdapter2).shoes = it
        }

        val data = mutableListOf(
            "111","2222","sdfdsfdsfdsfdsf","fdsfdsfdsfdsf","fdsfdsf","11","fdsfdsfdsfdsff",
            "111","2222","sdfdsfdsfdsfdsf","fdsfdsfdsfdsf","fdsfdsf","11","fdsfdsfdsfdsff",
            "111","2222","sdfdsfdsfdsfdsf","fdsfdsfdsfdsf","fdsfdsf","11","fdsfdsfdsfdsff",
            "111","2222","fdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsffdsfdsfdsfdsf","fdsfdsfdsfdsf","fdsfdsf","11","fdsfdsfdsfdsff",
            "111","2222","sdfdsfdsfdsfdsf","fdsfdsfdsfdsf","fdsfdsf","11","fdsfdsfdsfdsff",
            "111","2222","sdfdsfdsfdsfdsf","fdsfdsfdsfdsf","fdsfdsf","11","fdsfdsfdsfdsff",
            "111","2222","sdfdsfdsfdsfdsf","fdsfdsfdsfdsf","fdsfdsf","11","fdsfdsfdsfdsff"
        )

        (recycler_view.layoutManager as FLowLayoutManager).lines =3
        recycler_view.adapter = MyTagAdapter(data)

        changeLines.setOnClickListener {
          val manager =  recycler_view.layoutManager as FLowLayoutManager
          if(manager.lines>3){
              manager.setMaxRows(3)
              changeLines.text ="展开"
          }else {
              manager.setMaxRows(Int.MAX_VALUE)
              changeLines.text ="收起"
          }
        }
      //  flow_widget.referencedIds = intArrayOf(R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4)
    }


}