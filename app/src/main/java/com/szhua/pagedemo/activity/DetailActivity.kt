package com.szhua.pagedemo.activity

import android.animation.Animator
import android.app.ZygotePreload
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewParent
import androidx.activity.viewModels
import androidx.annotation.WorkerThread
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.LogUtils
import com.szhua.pagedemo.R
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.databinding.ActivityDetailBinding
import com.szhua.pagedemo.utils.blurBitmap
import com.szhua.pagedemo.viewmodel.CustomViewModelProvider
import com.szhua.pagedemo.viewmodel.DetailModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private val detailModel :DetailModel by viewModels {
            CustomViewModelProvider.providerDetailModel(this,intent.getLongExtra(BaseConstant.DETAIL_SHOE_ID, 1L))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail)
        binding.model =detailModel
        initListener(binding)








    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }



    private fun initListener(binding: ActivityDetailBinding) {
        binding.lifecycleOwner =this
        binding.activity =this
        fb_favourite.setOnClickListener {
             it.animate().rotation(360.0f)
                 .scaleX(0.0f)
                 .scaleY(0.0f)
                 .setListener(object :Animator.AnimatorListener{
                     override fun onAnimationStart(animation: Animator?) {}
                     override fun onAnimationEnd(animation: Animator?) {
                          detailModel.favouriteShoe()
                     }
                     override fun onAnimationCancel(animation: Animator?) {}
                     override fun onAnimationRepeat(animation: Animator?) {}
                 })
        }

    }
}