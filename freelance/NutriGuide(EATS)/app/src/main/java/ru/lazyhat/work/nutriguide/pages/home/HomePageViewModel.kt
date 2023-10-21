package ru.lazyhat.work.nutriguide.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.lazyhat.work.nutriguide.staticdata.getCaloriesToEat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.lazyhat.work.nutriguide.data.filter
import ru.lazyhat.work.nutriguide.data.getCurrentNutrition
import ru.lazyhat.work.nutriguide.data.products.ProductRepository
import ru.lazyhat.work.nutriguide.data.products.db.day.DateTime
import ru.lazyhat.work.nutriguide.data.userdata.UserRepository
import ru.lazyhat.work.nutriguide.pages.eat.EatTime
import javax.inject.Inject

data class HomeState(
    val date: DateTime?,
    val caloriesDay: Float,
    val currentCalories: Float,
    val needCalories: Float,
    val eatBoxes: List<HomeEatBoxState>
)

data class HomeEatBoxState(
    val time: EatTime,
    val percent: Float,
    val calories: Float
)

@HiltViewModel
class HomePageViewModel @Inject constructor(
    userRepository: UserRepository,
    productRepository: ProductRepository
) : ViewModel() {
    val uiState: StateFlow<HomeState> =
        combine(
            userRepository.getUserDataStream(),
            productRepository.getAllProductsStream()
        ) { user, products ->
            var currentCalories = 0f
            val needCaloriesPerDay = user.getCaloriesToEat()
            products.forEach {
                currentCalories += it.second.info.nutrition100.getCurrentNutrition(it.second.weight).calories
            }
            HomeState(
                null,
                caloriesDay = needCaloriesPerDay,
                currentCalories = currentCalories,
                needCalories = needCaloriesPerDay - currentCalories,
                EatTime.values().map {
                    var calories = 0f
                    products.filter(it).forEach { product ->
                        calories += product.info.nutrition100.getCurrentNutrition(product.weight).calories
                    }
                    HomeEatBoxState(
                        time = it,
                        percent = calories / needCaloriesPerDay * 100,
                        calories = calories
                    )
                }
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeState(null,0f, 0f, 0f, emptyList())
        )
}