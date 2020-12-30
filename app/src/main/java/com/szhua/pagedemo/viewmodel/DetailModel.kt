package com.szhua.pagedemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szhua.pagedemo.db.data.FavouriteShoe
import com.szhua.pagedemo.db.data.Shoe
import com.szhua.pagedemo.db.repository.FavouriteShoeRepository
import com.szhua.pagedemo.db.repository.ShoeRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailModel constructor(private  val shoeRepository: ShoeRepository,
                              private val favouriteShoeRepository: FavouriteShoeRepository,
                              private  val shoeId:Long,
                              private val  userId :Long
) :ViewModel() {


    val  shoe :LiveData<Shoe> =shoeRepository.getShoeById(shoeId)
    val  favouriteShoe : LiveData<FavouriteShoe?> = favouriteShoeRepository.findFavouriteShoe(userId,shoeId)


    fun  favouriteShoe(){
        viewModelScope.launch {
            favouriteShoeRepository.createFavouriteShoe(userId,shoeId)
        }
    }

}