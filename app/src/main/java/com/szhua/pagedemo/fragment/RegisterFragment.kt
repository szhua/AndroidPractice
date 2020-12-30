package com.szhua.pagedemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blankj.utilcode.util.LogUtils
import com.szhua.pagedemo.R
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.databinding.RegisterFragmentBinding
import com.szhua.pagedemo.viewmodel.CustomViewModelProvider
import com.szhua.pagedemo.viewmodel.RegisterModel

class RegisterFragment :Fragment() {


    private val registerModel:RegisterModel by viewModels {
        CustomViewModelProvider.providerRegisterModel(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:RegisterFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.register_fragment,container,false
        )
        initData(binding)
        onSubscribeUi(binding)
        return  binding.root
    }


    private fun initData(binding: RegisterFragmentBinding) {
        // SafeArgs的使用
        val safeArgs:RegisterFragmentArgs by navArgs()
        val email = safeArgs.email
        LogUtils.d(safeArgs.email)
        binding.lifecycleOwner = this
        binding.model = registerModel
        binding.model?.mail?.value = email
        binding.activity = activity
    }

    private fun onSubscribeUi(binding: RegisterFragmentBinding) {
        binding.btnRegister.setOnClickListener {
            registerModel.register()
            val bundle = Bundle()
            bundle.putString(BaseConstant.ARGS_NAME, registerModel.n.value)
            findNavController().navigate(R.id.login, bundle, null)
        }
    }
}