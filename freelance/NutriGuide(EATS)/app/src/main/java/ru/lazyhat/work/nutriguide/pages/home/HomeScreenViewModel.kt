package ru.lazyhat.work.nutriguide.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.lazyhat.work.nutriguide.data.products.ProductRepository
import ru.lazyhat.work.nutriguide.data.toHomeState
import javax.inject.Inject

data class HomeScreenState(
    val homePagesPrevious: List<HomeState> = emptyList(),
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    productRepository: ProductRepository
) : ViewModel() {
    val uiState = productRepository.getPrevDaysStream().map {
        HomeScreenState(
            it.map { day ->
                day.toHomeState()
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeScreenState())
}