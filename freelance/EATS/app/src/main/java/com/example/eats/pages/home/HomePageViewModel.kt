package com.example.eats.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eats.data.filter
import com.example.eats.data.getCurrentNutrition
import com.example.eats.data.products.ProductRepository
import com.example.eats.data.products.db.day.DateTime
import com.example.eats.data.userdata.UserRepository
import com.example.eats.pages.eat.EatTime
import com.example.eats.staticdata.getCaloriesToEat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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