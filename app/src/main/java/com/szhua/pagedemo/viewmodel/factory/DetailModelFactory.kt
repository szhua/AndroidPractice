package com.szhua.pagedemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.szhua.pagedemo.db.repository.FavouriteShoeRepository
import com.szhua.pagedemo.db.repository.ShoeRepository
import com.szhua.pagedemo.viewmodel.DetailModel

class DetailModelFactory constructor( private val shoeRepository: ShoeRepository
                                      , private val favouriteShoeRepository: FavouriteShoeRepository
                                      , private val shoeId: Long
                                      , private val userId: Long) :ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailModel(shoeRepository,favouriteShoeRepository,shoeId,userId) as T
    }
}