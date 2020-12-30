package com.szhua.pagedemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szhua.pagedemo.activity.DetailActivity
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.databinding.FavoriteRecyclerItem2Binding
import com.szhua.pagedemo.db.data.Shoe

class FavoriteShoeAdapter2 constructor(val context: Context):RecyclerView.Adapter<FavoriteShoeAdapter2.FavoriteViewHolder>() {


    var  shoes : List<Shoe>  = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class  FavoriteViewHolder(private  val favoriteRecyclerItemBinding :FavoriteRecyclerItem2Binding):RecyclerView.ViewHolder(favoriteRecyclerItemBinding.root){
        fun bind(listener: View.OnClickListener, shoe:Shoe){
            favoriteRecyclerItemBinding.apply {
                this.listener = listener
                this.shoe = shoe
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return  FavoriteViewHolder(FavoriteRecyclerItem2Binding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val  shoe = shoes[position%shoes.size]
        holder.apply {
            bind({
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(BaseConstant.DETAIL_SHOE_ID, shoe.id)
                context.startActivity(intent)
            },shoe)
            itemView.tag =shoe
        }

    }

    override fun getItemCount(): Int {
        if(shoes.isEmpty()) return  0
        return  Int.MAX_VALUE
    }

}