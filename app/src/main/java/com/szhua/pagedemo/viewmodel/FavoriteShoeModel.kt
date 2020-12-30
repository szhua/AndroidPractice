package com.szhua.pagedemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.szhua.pagedemo.db.data.FavouriteShoe
import com.szhua.pagedemo.db.data.Shoe
import com.szhua.pagedemo.db.repository.FavouriteShoeRepository
import com.szhua.pagedemo.db.repository.ShoeRepository

class FavoriteShoeModel constructor(private val shoeRepository: ShoeRepository, userId:Long) :ViewModel() {

    val shoes:LiveData<List<Shoe>> = shoeRepository.getShoesByUserId(userId)









}