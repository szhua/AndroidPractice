package com.szhua.pagedemo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.szhua.pagedemo.R

class MyTagAdapter constructor(  data :  MutableList<String>) :BaseQuickAdapter<String,BaseViewHolder>(R.layout.tag_recycler_item,data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tag,item)
    }

}