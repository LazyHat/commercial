package com.example.eats.data.products.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eats.data.products.db.day.LocalDay
import com.example.eats.data.products.db.infos.LocalInfo
import com.example.eats.data.products.db.products.LocalProduct

@Database(
    entities = [LocalProduct::class, LocalInfo::class, LocalDay::class],
    version = 1,
    exportSchema = true
)
abstract class ProductDataBase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var instance: ProductDataBase? = null

        fun getInstance(context: Context): ProductDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, ProductDataBase::class.java, "Lessons.db").build()
    }
}