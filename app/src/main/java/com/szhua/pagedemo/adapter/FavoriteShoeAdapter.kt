package com.szhua.pagedemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.szhua.pagedemo.activity.DetailActivity
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.databinding.FavoriteRecyclerItemBinding
import com.szhua.pagedemo.db.data.Shoe

class FavoriteShoeAdapter constructor(val context: Context):ListAdapter<Shoe,FavoriteShoeAdapter.FavoriteViewHolder>(ShoeDiffCallback()) {

    class  FavoriteViewHolder(private  val favoriteRecyclerItemBinding :FavoriteRecyclerItemBinding):RecyclerView.ViewHolder(favoriteRecyclerItemBinding.root){


        fun bind(listener: View.OnClickListener, shoe:Shoe ){
              favoriteRecyclerItemBinding.apply {
                  this.listener = listener
                  this.shoe = shoe
                  executePendingBindings()
              }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
         return  FavoriteViewHolder(FavoriteRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val shoe = getItem(position)
        holder.apply {
            bind({
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(BaseConstant.DETAIL_SHOE_ID, shoe.id)
                context.startActivity(intent)
            },shoe)
            itemView.tag =shoe
        }
    }
}