package com.szhua.pagedemo.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.szhua.pagedemo.db.repository.UserRepository
import com.szhua.pagedemo.viewmodel.LoginModel
import com.szhua.pagedemo.viewmodel.RegisterModel

class RegisterModelFactory(private  val repository: UserRepository,private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  RegisterModel(repository) as T
    }

}