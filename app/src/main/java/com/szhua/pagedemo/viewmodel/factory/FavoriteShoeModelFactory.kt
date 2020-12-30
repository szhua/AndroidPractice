package com.szhua.pagedemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.szhua.pagedemo.db.repository.ShoeRepository
import com.szhua.pagedemo.viewmodel.FavoriteShoeModel

class FavoriteShoeModelFactory(private val shoeRepository: ShoeRepository, val userId: Long) :ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  FavoriteShoeModel(shoeRepository ,userId) as T
    }

}