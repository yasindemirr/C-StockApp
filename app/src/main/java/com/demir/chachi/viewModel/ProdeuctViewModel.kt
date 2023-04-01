package com.demir.chachi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.demir.chachi.model.Product
import com.demir.chachi.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProdeuctViewModel
    @Inject constructor(val repository: ProductRepository) :ViewModel() {
        fun realAllProduct():LiveData<List<Product>>{
            return repository.readAllProduct().flowOn(Dispatchers.Main)
                .asLiveData(viewModelScope.coroutineContext)
        }
    fun realAllListProduct():LiveData<List<Product>>{
        return repository.readAllListProduct().flowOn(Dispatchers.Main)
            .asLiveData(viewModelScope.coroutineContext)
    }
    fun insertProduct(product: Product)=viewModelScope.launch {
        repository.insertProduct(product)
    }
    fun deleteProduct(product: Product)=viewModelScope.launch {
        repository.deletetProduct(product)
    }

    fun updateProduct(product: Product)=viewModelScope.launch {
        repository.updatetProduct(product)
    }
    fun updateAddList(userId: Int, newAddList: Int,isEnableQuantity:Boolean)
    =viewModelScope.launch {
        repository.updateAddList(userId,newAddList,isEnableQuantity)
    }
    fun updateQuantityList(userId: Int, newListeAdedi: Int,yeniAdet:Int)
    =viewModelScope.launch {
        repository.updateListQuantiy(userId,newListeAdedi,yeniAdet)
        }
    }

