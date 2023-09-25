package com.example.eats.pages.eat.pages.addproduct

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eats.MainActivity
import com.example.eats.data.products.ProductInfo
import com.example.eats.data.products.ProductRepository
import com.example.eats.data.products.ProductState
import com.example.eats.pages.eat.EatTime
import com.example.eats.pages.eat.pages.ProductDialogState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class AddProductEvent {
    data class InsertProduct(val product: ProductState) : AddProductEvent()
    data class OpenDialog(val dState: ProductDialogState) : AddProductEvent()
    data class SearchValueChange(val new: String) : AddProductEvent()
    object DismissDialog : AddProductEvent()
}

data class AddProductState(
    val time: EatTime,
    val productDialogState: ProductDialogState? = null,
    val availableProducts: List<ProductInfo> = emptyList(),
    val searchValue: String = ""
)

class AddProductViewModel @AssistedInject constructor(
    @Assisted private val currentEatTime: EatTime,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _productDialogState = MutableStateFlow<ProductDialogState?>(null)
    private val _searchValue = MutableStateFlow("")
    val uiState: StateFlow<AddProductState> =
        combine(
            _productDialogState.asStateFlow(),
            productRepository.getUnusedInfoStream(currentEatTime),
            _searchValue
        ) { dialogState, infos, search ->
            AddProductState(
                time = currentEatTime,
                productDialogState = dialogState,
                availableProducts = infos.filter {
                    it.label.lowercase().contains(search.lowercase())
                },
                search
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AddProductState(currentEatTime)
        )

    fun createEvent(e: AddProductEvent) = onEvent(e)

    //@Suppress("IMPLICIT_CAST_TO_ANY")
    private fun onEvent(e: AddProductEvent) = when (e) {
        is AddProductEvent.InsertProduct -> {
            viewModelScope.launch {
                productRepository.upsertProduct(
                    uiState.value.time,
                    e.product
                )
            }
            _productDialogState.update { null }
        }

        is AddProductEvent.OpenDialog -> _productDialogState.update { e.dState }

        is AddProductEvent.DismissDialog -> _productDialogState.update { null }

        is AddProductEvent.SearchValueChange -> _searchValue.update { e.new }
    }

    @AssistedFactory
    interface Factory {
        fun create(time: EatTime): AddProductViewModel
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
fun addProductViewModel(time: EatTime): AddProductViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        MainActivity.ViewModelFactoryProvider::class.java
    ).addProductViewModelFactory()

    return viewModel(factory = AddProductViewModel.provideFactory(factory, time))
}