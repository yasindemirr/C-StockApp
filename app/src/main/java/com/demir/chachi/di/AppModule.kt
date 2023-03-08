package com.demir.chachi.di

import android.content.Context
import androidx.navigation.Navigator
import androidx.room.Entity
import androidx.room.Room
import com.demir.chachi.repository.ProductRepository
import com.demir.chachi.db.PrdouctDao
import com.demir.chachi.db.ProductBillDao
import com.demir.chachi.db.ProductBillDataBase
import com.demir.chachi.db.ProductDataBase
import com.demir.chachi.model.ProductBill
import com.demir.chachi.repository.ProductBillRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesAlertDao(productDataBase: ProductDataBase):PrdouctDao = productDataBase.productDao()

    @Provides
    fun providesAlertBillDao(productBillDataBase: ProductBillDataBase): ProductBillDao =
        productBillDataBase.productBillDao()
//Product Sınıfının
    @Provides
    @Singleton
    fun providesAlertDatabase(@ApplicationContext context: Context):ProductDataBase
            = Room.databaseBuilder(context,ProductDataBase::class.java,"AlertDatabase")
        .allowMainThreadQueries().build()
    //ProductBill Sınıfının
    @Provides
    @Singleton
    fun providesAlertBillDatabase(@ApplicationContext context: Context):ProductBillDataBase
            = Room.databaseBuilder(context,ProductBillDataBase::class.java,"AlertBillDatabase")
        .allowMainThreadQueries().build()

    @Provides
    fun providesUserRepository(prdouctDao: PrdouctDao) : ProductRepository
            = ProductRepository(prdouctDao)

    @Provides
    fun providesUserBillRepository(prdouctBillDao: ProductBillDao) : ProductBillRepository
            = ProductBillRepository(prdouctBillDao)

}