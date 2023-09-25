package com.example.eats.data.products

import com.example.eats.data.products.db.day.Day
import com.example.eats.pages.eat.EatTime
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun upsertProduct(time: EatTime, product: ProductState)
    fun getAllProductsStream(): Flow<List<Pair<EatTime, ProductState>>>
    suspend fun getAllProducts(): List<Pair<EatTime, ProductState>>
    suspend fun deleteProduct(time: EatTime, product: ProductState)
    suspend fun deleteAllProducts()
    fun getUnusedInfoStream(time: EatTime): Flow<List<ProductInfo>>
    suspend fun getAllProductsInfo(): List<ProductInfo>
    suspend fun insertProductInfo(new: ProductInfo)
    fun getPrevDaysStream(): Flow<List<Day>>
    suspend fun getPrevDays(): List<Day>

    suspend fun insertPrevDay(new: Day)
}