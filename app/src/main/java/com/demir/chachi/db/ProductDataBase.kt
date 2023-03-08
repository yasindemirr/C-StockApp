package com.demir.chachi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demir.chachi.model.Product
import com.demir.chachi.model.ProductBill

@Database(entities = [Product::class], version = 1)
abstract class ProductDataBase: RoomDatabase() {
    abstract fun productDao():PrdouctDao
}

@Database(entities = [ProductBill::class], version = 1)
abstract class ProductBillDataBase: RoomDatabase() {
    abstract fun productBillDao():ProductBillDao
}