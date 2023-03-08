package com.demir.chachi.repository

import com.demir.chachi.db.PrdouctDao
import com.demir.chachi.db.ProductBillDao
import com.demir.chachi.model.Product
import com.demir.chachi.model.ProductBill
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductBillRepository
@Inject constructor(val productBillDao: ProductBillDao) {
    suspend fun insertProduct(productBill: ProductBill){
        productBillDao.insertProductBill(productBill)
    }
    suspend fun updatetProduct(productBill: ProductBill){
        productBillDao.updateProductBill(productBill)
    }
     fun getInfo():Flow<ProductBill>{
        return productBillDao.getInfo()
    }
}