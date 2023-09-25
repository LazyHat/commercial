package com.example.eats.di

import android.content.Context
import androidx.work.WorkManager
import com.example.eats.data.products.DefaultProductsRepository
import com.example.eats.data.products.ProductRepository
import com.example.eats.data.products.db.ProductDao
import com.example.eats.data.products.db.ProductDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Singleton
    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideProductRepository(dao: ProductDao, workManager: WorkManager): ProductRepository =
        DefaultProductsRepository(dao, workManager)

    @Singleton
    @Provides
    fun provideProductDataBase(@ApplicationContext context: Context): ProductDataBase =
        ProductDataBase.getInstance(context)

    @Singleton
    @Provides
    fun provideProductDao(db: ProductDataBase): ProductDao = db.productDao()

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}

@Module
@InstallIn(ActivityRetainedComponent::class)
interface AssistedInjectModule