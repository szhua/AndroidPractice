package com.szhua.pagedemo.binding

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.szhua.pagedemo.R
import com.szhua.pagedemo.common.listener.SimpleWatcher
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view:ImageView,imageUrl:String?){
    if(!imageUrl.isNullOrEmpty()){
        Glide.with(view.context)
            .asBitmap()
           // .load("https://img.xclient.info/attachment/2019/01/ca1fa473-36b1-5871-2fad-256d16293676.png")
            .load(imageUrl)
            .placeholder(R.drawable.glide_placeholder)
            .centerCrop()
            .into(view)
    }
}

// 加载带圆角的头像
@BindingAdapter("imageTransFromUrl")
fun bindImageTransFromUrl(view:ImageView,imageUrl:String?){
    if(!imageUrl.isNullOrEmpty()){
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.drawable.glide_placeholder)
            .apply(bitmapTransform(RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL)))
            .into(view)
    }
}

// 文本监听器
@BindingAdapter("addTextChangedListener")
fun addTextChangedListener(editText: EditText, simpleWatcher: SimpleWatcher) {
    editText.addTextChangedListener(simpleWatcher)
}
