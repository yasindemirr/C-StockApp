package com.demir.chachi.repository

import com.demir.chachi.db.PrdouctDao
import com.demir.chachi.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository
@Inject constructor(val productDao: PrdouctDao) {
    suspend fun insertProduct(product: Product){
        productDao.insertProduct(product)
    }
    suspend fun deletetProduct(product: Product){
        productDao.deleteProduct(product)
    }
    suspend fun updatetProduct(product: Product){
        productDao.updateProduct(product)
    }
    suspend fun updateAddList(userId: Int, newAddList: Int,isEnableQuantity:Boolean){
        productDao.updateAddList(userId,newAddList,isEnableQuantity)
    }
    suspend fun updateListQuantiy(userId: Int, newListeAdedi: Int,yeniAdet:Int){
        productDao.updateQuantity(userId,newListeAdedi,yeniAdet)
    }
    fun readAllProduct(): Flow<List<Product>>{
      return  productDao.readAllProduct()
    }


}