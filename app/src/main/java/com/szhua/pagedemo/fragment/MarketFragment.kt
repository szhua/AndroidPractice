package com.szhua.pagedemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.szhua.pagedemo.adapter.FavoriteShoeAdapter
import com.szhua.pagedemo.common.BaseConstant
import com.szhua.pagedemo.databinding.MarketFragmentBinding
import com.szhua.pagedemo.utils.AppPrefsUtils
import com.szhua.pagedemo.viewmodel.CustomViewModelProvider
import com.szhua.pagedemo.viewmodel.FavoriteShoeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarketFragment :Fragment() {


    private val favoriteShoeModel:FavoriteShoeModel by viewModels {
        CustomViewModelProvider.providerFavoriteModel(requireContext(),AppPrefsUtils.getLong(BaseConstant.SP_USER_ID))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding:MarketFragmentBinding = MarketFragmentBinding.inflate(inflater, container,false)
        val adapter =FavoriteShoeAdapter(requireContext())
        binding.recycler.adapter =adapter
        onSubscribeUi(adapter,binding)
        return binding.root
    }

    private fun onSubscribeUi(adapter: FavoriteShoeAdapter,binding: MarketFragmentBinding) {
        binding.empty.bind(arrayOf(binding.recycler))
        binding.empty.triggerLoading()
        favoriteShoeModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Thread.sleep(4000)
                LogUtils.d("fdsf")
            }
            LogUtils.d("launch")
            favoriteShoeModel.shoes.observe(viewLifecycleOwner){
                if (it.isNotEmpty()){
                    adapter.submitList(it)
                }
                LogUtils.d("fdsfsdfsf")
                binding.empty.triggerOkOrEmpty(it.isNotEmpty())
            }
        }

    }


}