package com.szhua.pagedemo.viewmodel

import android.content.Context
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.db.RepositoryProvider
import com.szhua.pagedemo.db.repository.FavouriteShoeRepository
import com.szhua.pagedemo.db.repository.ShoeRepository
import com.szhua.pagedemo.db.repository.UserRepository
import com.szhua.pagedemo.utils.AppPrefsUtils
import com.szhua.pagedemo.viewmodel.factory.*

object CustomViewModelProvider{

    fun providerLoginModel(context: Context): LoginModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return LoginModelFactory(repository, context)
    }

    fun  providerRegisterModel(context: Context):RegisterModelFactory{
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
         return  RegisterModelFactory(repository,context)
    }
    fun  providerShoeModel(context: Context):ShoeModelFactory{
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        return  ShoeModelFactory(repository)
    }

    fun  providerDetailModel(context: Context ,shoeId:Long):DetailModelFactory{
        val shoeRepository:ShoeRepository =RepositoryProvider.providerShoeRepository(context)
        val favouriteShoeRepository:FavouriteShoeRepository =RepositoryProvider.providerFavouriteShoeRepository(context)
        val userId:Long = AppPrefsUtils.getLong(BaseConstant.SP_USER_ID)
        return  DetailModelFactory(shoeRepository,favouriteShoeRepository, shoeId ,userId)
    }


    fun  providerFavoriteModel(context: Context,userId:Long):FavoriteShoeModelFactory{
        val  shoeRepository =RepositoryProvider.providerShoeRepository(context)
        return  FavoriteShoeModelFactory(shoeRepository,userId)
    }

    fun providerMeModel(context: Context):MeModelFactory{
        val repository:UserRepository = RepositoryProvider.providerUserRepository(context)
        return MeModelFactory(repository)
    }



}

