package ru.lazyhat.work.nutriguide.pages.eat.pages.addinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.lazyhat.work.nutriguide.pages.eat.FieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.lazyhat.work.nutriguide.data.products.ProductRepository
import javax.inject.Inject

private const val BUNDLE_KEY = "add_info_b"

data class AddInfoState(
    val label: FieldState = FieldState.empty(),
    val calories: FieldState = FieldState.empty(),
    val proteins: FieldState = FieldState.empty(),
    val fats: FieldState = FieldState.empty(),
    val carbohydrates: FieldState = FieldState.empty()
)

sealed class AddInfoEvent {
    data class LabelChange(val new: String) : AddInfoEvent()
    data class CaloriesChange(val new: String) : AddInfoEvent()
    data class ProteinsChange(val new: String) : AddInfoEvent()
    data class FatsChange(val new: String) : AddInfoEvent()
    data class CarbohydratesChange(val new: String) : AddInfoEvent()
    data class Result(val result: () -> Unit) : AddInfoEvent()
}

@HiltViewModel
class AddInfoViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiState = savedStateHandle.getStateFlow(BUNDLE_KEY, AddInfoState().toBundle())
        .map { it.toAddInfoState() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddInfoState())

    fun createEvent(e: AddInfoEvent) = onEvent(e)

    private fun onEvent(e: AddInfoEvent) = when (e) {
        is AddInfoEvent.LabelChange -> with(uiState.value.label) {
            if (error != "")
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(label = copy(value = e.new, error = "")).toBundle()
            else
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(label = copy(value = e.new)).toBundle()
        }

        is AddInfoEvent.CaloriesChange -> with(uiState.value.calories) {
            if (error != "")
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(calories = copy(value = e.new, error = "")).toBundle()
            else
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(calories = copy(value = e.new)).toBundle()
        }

        is AddInfoEvent.ProteinsChange -> with(uiState.value.proteins) {
            if (error != "")
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(proteins = copy(value = e.new, error = "")).toBundle()
            else
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(proteins = copy(value = e.new)).toBundle()
        }

        is AddInfoEvent.FatsChange -> with(uiState.value.fats) {
            if (error != "")
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(fats = copy(value = e.new, error = "")).toBundle()
            else
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(fats = copy(value = e.new)).toBundle()
        }

        is AddInfoEvent.CarbohydratesChange -> with(uiState.value.carbohydrates) {
            if (error != "")
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(carbohydrates = copy(value = e.new, error = "")).toBundle()
            else
                savedStateHandle[BUNDLE_KEY] =
                    uiState.value.copy(carbohydrates = copy(value = e.new)).toBundle()
        }

        is AddInfoEvent.Result -> {
            @Suppress("ControlFlowWithEmptyBody")
            if (checkNutritionInfo()) {
                viewModelScope.launch {
                    productRepository.insertProductInfo(uiState.value.toProductInfo())
                }
                e.result()
            } else {
            }
        }
    }

    private fun checkNutritionInfo(): Boolean = true.run {
        if (uiState.value.label.value.isEmpty()) {
            savedStateHandle[BUNDLE_KEY] =
                uiState.value.let {
                    it.copy(label = it.label.copy(error = "Неверное значение"))
                }.toBundle()
            false
        } else
            this
    }.run {
        if (uiState.value.calories.value.isEmpty()) {
            savedStateHandle[BUNDLE_KEY] =
                uiState.value.let {
                    it.copy(calories = it.calories.copy(error = "Неверное значение"))
                }.toBundle()
            false
        } else
            this
    }.run {
        if (uiState.value.proteins.value.isEmpty()) {
            savedStateHandle[BUNDLE_KEY] =
                uiState.value.let {
                    it.copy(proteins = it.proteins.copy(error = "Неверное значение"))
                }.toBundle()
            false
        } else
            this
    }.run {
        if (uiState.value.fats.value.isEmpty()) {
            savedStateHandle[BUNDLE_KEY] =
                uiState.value.let {
                    it.copy(fats = it.fats.copy(error = "Неверное значение"))
                }.toBundle()
            false
        } else
            this
    }.run {
        if (uiState.value.carbohydrates.value.isEmpty()) {
            savedStateHandle[BUNDLE_KEY] =
                uiState.value.let {
                    it.copy(carbohydrates = it.carbohydrates.copy(error = "Неверное значение"))
                }.toBundle()
            false
        } else
            this
    }
}