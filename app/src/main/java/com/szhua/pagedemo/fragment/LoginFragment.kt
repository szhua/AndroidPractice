package com.szhua.pagedemo.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.impl.model.WorkTypeConverters.StateIds.SUCCEEDED
import com.blankj.utilcode.util.LogUtils
import com.szhua.pagedemo.MainActivity
import com.szhua.pagedemo.R
import com.szhua.pagedemo.common.BaseApplication
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.databinding.LoginFragmentBinding
import com.szhua.pagedemo.db.RepositoryProvider
import com.szhua.pagedemo.utils.AppPrefsUtils
import com.szhua.pagedemo.viewmodel.CustomViewModelProvider
import com.szhua.pagedemo.viewmodel.LoginModel
import com.szhua.pagedemo.worker.ShoeWorker
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.*

class  LoginFragment : Fragment(){


    private  val loginModel: LoginModel by viewModels{
        CustomViewModelProvider.providerLoginModel(requireContext())
    }


     private val  request :WorkRequest by lazy {
        OneTimeWorkRequestBuilder<ShoeWorker>().build()
     }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater
            , R.layout.login_fragment
            , container
            , false
        )
        onSubscribeUi(binding)

        loginModel.viewModelScope.launch {
            val shoes =  withContext(Dispatchers.IO){
                RepositoryProvider.providerShoeRepository(BaseApplication.context)
                    .getPageShoes(1,10)
            }
            val isFirstLaunch = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
            if (shoes.isEmpty()||isFirstLaunch){
                // 读取鞋的集合
                WorkManager.getInstance().enqueue(request)
            }
        }

        WorkManager.getInstance()
            .getWorkInfoByIdLiveData(request.id)
            .observe(this, {
                if (it != null && it.state.isFinished) {
                    if (it.state == WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
                    }
                }
            })






//        // 判断当前是否是第一次登陆
//        val isFirstLaunch = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
//        if(isFirstLaunch){
//            onFirstLaunch()
//        }
        return binding.root

    }







    private fun onSubscribeUi(binding: LoginFragmentBinding) {
        binding.model = loginModel
        binding.activity = activity

        // 如果使用LiveData下面这句必须加上 ！！！
        binding.lifecycleOwner = this

        binding.btnLogin.setOnClickListener {
           try {
               loginModel.login()?.observe(this, Observer { user ->
                   user?.let {
                       AppPrefsUtils.putLong(BaseConstant.SP_USER_ID, it.id)
                       AppPrefsUtils.putString(BaseConstant.SP_USER_NAME, it.account)
                       val intent = Intent(context, MainActivity::class.java)
                       context!!.startActivity(intent)
                       Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show()
                   }?:let {

                   }
               })
           }catch (e:Exception){
               Log.e("leilei",e.toString())
           }finally {

           }
        }

        arguments?.getString(BaseConstant.ARGS_NAME)?.apply {
            loginModel.n.value = this
        }
    }


    // 第一次启动的时候调用
    private fun onFirstLaunch(){
        loginModel.viewModelScope.launch(Dispatchers.Main) {
            val str = withContext(Dispatchers.IO) {
                loginModel.onFirstLaunch()
            }
            Toast.makeText(context!!,str,Toast.LENGTH_SHORT).show()
           // AppPrefsUtils.putBoolean(BaseConstant.IS_FIRST_LAUNCH,false)
        }
    }





}

