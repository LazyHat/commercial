package com.example.eats.data.products.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.eats.data.products.db.day.DAY_TN
import com.example.eats.data.products.db.day.LocalDay
import com.example.eats.data.products.db.infos.INFO_TN
import com.example.eats.data.products.db.infos.LocalInfo
import com.example.eats.data.products.db.products.LocalProduct
import com.example.eats.data.products.db.products.PRODUCT_TN
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Upsert(entity = LocalProduct::class)
    suspend fun upsertProduct(item: LocalProduct)

    @Query("SELECT * FROM $PRODUCT_TN")
    fun getAllProductsStream(): Flow<List<LocalProduct>>

    @Query("SELECT * FROM $PRODUCT_TN")
    suspend fun getAllProducts(): List<LocalProduct>

    @Query("DELETE FROM $PRODUCT_TN WHERE id = :id AND time = :time")
    suspend fun deleteProduct(id: String, time: String)

    @Query("DELETE FROM $PRODUCT_TN")
    suspend fun deleteAllProducts()

    @Query("SELECT * FROM $INFO_TN")
    fun getAllInfosStream(): Flow<List<LocalInfo>>

    @Insert(entity = LocalInfo::class)
    suspend fun insertInfo(item: LocalInfo)

    @Query("SELECT * FROM $DAY_TN")
    suspend fun getPrevDays(): List<LocalDay>

    @Query("SELECT * FROM $DAY_TN")
    fun getPrevDaysStream(): Flow<List<LocalDay>>

    @Upsert(LocalDay::class)
    suspend fun insertPrevDay(day: LocalDay)
}