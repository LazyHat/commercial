package ru.lazyhat.work.nutriguide.pages.eat.pages.eathome

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.lazyhat.work.nutriguide.MainActivity
import ru.lazyhat.work.nutriguide.data.filter
import ru.lazyhat.work.nutriguide.data.products.ProductRepository
import ru.lazyhat.work.nutriguide.data.products.ProductState
import ru.lazyhat.work.nutriguide.pages.eat.EatTime
import ru.lazyhat.work.nutriguide.pages.eat.pages.ProductDialogState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EatHomeState(
    val time: EatTime = EatTime.BreakFast,
    val products: List<ProductState> = listOf(),
    val productDialogState: ProductDialogState? = null,
    val searchValue: String = ""
)

sealed class EatHomeEvent {
    data class ChangeEatTime(val new: EatTime) : EatHomeEvent()
    data class OpenDialog(val dState: ProductDialogState) : EatHomeEvent()
    data class UpdateProduct(val productState: ProductState) : EatHomeEvent()
    data class DeleteProduct(val productState: ProductState) : EatHomeEvent()
    data class SearchValueChange(val new: String) : EatHomeEvent()
    object DismissDialog : EatHomeEvent()
}

class EatHomeViewModel @AssistedInject constructor(
    @Assisted private val time: EatTime,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _time = MutableStateFlow(time)

    private val _dialogState = MutableStateFlow<ProductDialogState?>(null)

    private val _searchValue = MutableStateFlow("")

    val uiState: StateFlow<EatHomeState> =
        combine(
            _time,
            _dialogState,
            productRepository.getAllProductsStream(),
            _searchValue
        ) { currentTime, dState, products, search ->
            EatHomeState(
                currentTime,
                products.filter(currentTime)
                    .filter { search.isEmpty() || it.info.label.lowercase().contains(search.lowercase()) },
                dState,
                search
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EatHomeState())

    fun createEvent(e: EatHomeEvent) = onEvent(e)

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private fun onEvent(e: EatHomeEvent) = when (e) {
        is EatHomeEvent.ChangeEatTime -> _time.update { e.new }
        is EatHomeEvent.OpenDialog -> _dialogState.update { e.dState }
        is EatHomeEvent.DismissDialog -> _dialogState.update { null }
        is EatHomeEvent.UpdateProduct -> {
            viewModelScope.launch {
                productRepository.upsertProduct(uiState.value.time, e.productState)
                _dialogState.update { null }
            }
        }

        is EatHomeEvent.DeleteProduct -> {
            viewModelScope.launch {
                productRepository.deleteProduct(uiState.value.time, e.productState)
            }
        }

        is EatHomeEvent.SearchValueChange ->
            _searchValue.update { e.new }
    }

    @AssistedFactory
    interface Factory {
        fun create(time: EatTime): EatHomeViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            time: EatTime
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(time) as T
            }
        }
    }
}

@Composable
fun eatHomeViewModel(time: EatTime): EatHomeViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        MainActivity.ViewModelFactoryProvider::class.java
    ).eatHomeViewModelFactory()

    return viewModel(factory = EatHomeViewModel.provideFactory(factory, time))
}