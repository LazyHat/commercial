package com.lazyhat.work.aquaphor.di

import com.lazyhat.work.aquaphor.data.repository.FilterRepository
import com.lazyhat.work.aquaphor.domain.usecaseproviders.DateWorkerUseCases
import com.lazyhat.work.aquaphor.domain.usecaseproviders.HomeUseCases
import com.lazyhat.work.aquaphor.domain.usecaseproviders.MainUseCases
import com.lazyhat.work.aquaphor.domain.usecaseproviders.RegisterUseCases
import com.lazyhat.work.aquaphor.domain.usecases.DataIsNotEmpty
import com.lazyhat.work.aquaphor.domain.usecases.GetAvailableFiltersFlow
import com.lazyhat.work.aquaphor.domain.usecases.GetExpiresDate
import com.lazyhat.work.aquaphor.domain.usecases.GetFilter
import com.lazyhat.work.aquaphor.domain.usecases.GetFilterFlow
import com.lazyhat.work.aquaphor.domain.usecases.UpdateFilter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Зависимости UseКейсов нужны для сообщения репозитория с ViewModel
@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {
    @Singleton
    @Provides
    fun provideRegisterUseCasesProvider(
        getAvailableFilters: GetAvailableFiltersFlow,
        updateFilter: UpdateFilter,
        getFilter: GetFilter
    ) =
        RegisterUseCases(getAvailableFilters, updateFilter, getFilter)

    @Singleton
    @Provides
    fun provideHomeUseCasesProvider(
        getFilterFlow: GetFilterFlow
    ) =
        HomeUseCases(getFilterFlow)

    @Singleton
    @Provides
    fun provideMainUseCasesProvider(
        dataIsNotEmpty: DataIsNotEmpty
    ) =
        MainUseCases(dataIsNotEmpty)

    @Singleton
    @Provides
    fun provideDWUseCasesProvider(
        getExpiresDate: GetExpiresDate
    ) = DateWorkerUseCases(getExpiresDate)

    @Singleton
    @Provides
    fun provideGAFUseCase(filterRepository: FilterRepository) =
        GetAvailableFiltersFlow(filterRepository)

    @Singleton
    @Provides
    fun provideUpdateFilterUseCase(filterRepository: FilterRepository) =
        UpdateFilter(filterRepository)

    @Singleton
    @Provides
    fun provideGetFilterUseCase(filterRepository: FilterRepository) = GetFilter(filterRepository)

    @Singleton
    @Provides
    fun provideGetFilterFlowUseCase(filterRepository: FilterRepository) =
        GetFilterFlow(filterRepository)

    @Singleton
    @Provides
    fun provideDINEUseCase(filterRepository: FilterRepository) =
        DataIsNotEmpty(filterRepository)

    @Singleton
    @Provides
    fun provideGEDUseCase(filterRepository: FilterRepository) = GetExpiresDate(filterRepository)
}