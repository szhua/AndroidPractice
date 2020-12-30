package com.szhua.pagedemo.viewmodel

import android.text.Editable
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.work.ListenableWorker
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.szhua.pagedemo.common.BaseApplication
import com.szhua.pagedemo.common.listener.SimpleWatcher
import com.szhua.pagedemo.db.RepositoryProvider
import com.szhua.pagedemo.db.dao.FavouriteShoeDao
import com.szhua.pagedemo.db.data.Shoe
import com.szhua.pagedemo.db.data.User
import com.szhua.pagedemo.db.repository.FavouriteShoeRepository
import com.szhua.pagedemo.db.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginModel constructor(
    private val repository: UserRepository
) : ViewModel() {

    val n = MutableLiveData("")
    val p = MutableLiveData("123456")
    val enable = MutableLiveData(false)

    /*val n = ObservableField<String>("")
    val p = ObservableField<String>("")*/
    //lateinit var lifecycleOwner: LifecycleOwner

    /**
     * 用户名改变回调的函数
     */
    fun onNameChanged(s: CharSequence) {
        //n.set(s.toString())
        n.value = s.toString()
        judgeEnable()
    }

    private fun judgeEnable(){
        enable.value = n.value!!.isNotEmpty() && p.value!!.isNotEmpty()
    }

    /**
     * 密码改变的回调函数
     */
    fun onPwdChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //p.set(s.toString())
        p.value = s.toString()
        judgeEnable()
    }

    // SimpleWatcher 是简化了的TextWatcher
    val nameWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)

            n.value = s.toString()
            //n.set(s.toString())
            judgeEnable()
        }
    }

    val pwdWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)
            //p.set(s.toString())
            p.value = s.toString()
            judgeEnable()
        }
    }

    fun login(): LiveData<User?>? {
        val pwd = p.value!!
        val account = n.value!!
        //val pwd = p.get()!!
        //val account = n.get()!!

        return repository.login(account, pwd)
    }


    /**
     * 第一次启动的时候调用
     */
    fun onFirstLaunch():String {




        val context = BaseApplication.context
        context.assets.open("shoes.json").use {
            JsonReader(it.reader()).use {
                val shoeType = object : TypeToken<List<Shoe>>() {}.type
                val shoeList: List<Shoe> = Gson().fromJson(it, shoeType)
                val shoeDao = RepositoryProvider.providerShoeRepository(context)
                shoeDao.getPageShoes(0,1)
            }

        }
        return "初始化数据成功！"
    }
}