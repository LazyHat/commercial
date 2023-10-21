package ru.lazyhat.work.nutriguide.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.lazyhat.work.nutriguide.staticdata.getCaloriesToEat
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.lazyhat.work.nutriguide.data.products.ProductRepository
import ru.lazyhat.work.nutriguide.data.products.db.day.DateTime
import ru.lazyhat.work.nutriguide.data.products.db.day.Day
import ru.lazyhat.work.nutriguide.data.userdata.UserRepository
import java.time.LocalDateTime

@HiltWorker
class DayWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted parameters: WorkerParameters,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : CoroutineWorker(ctx, parameters) {
    override suspend fun doWork(): Result {
        val time = LocalDateTime.now()
        val date = DateTime(
            time.dayOfMonth - 1,
            time.monthValue,
            time.year
        )
        val prevDay = productRepository.getPrevDays().find { it.time == date }
        if (prevDay != null || time.hour < 23)
            return Result.success()
        else {
            val currentCaloriesTime = mutableListOf(0f, 0f, 0f, 0f)
            productRepository.getAllProducts().forEach {
                currentCaloriesTime[it.first.ordinal] +=
                    it.second.weight * it.second.info.nutrition100.calories / 100
            }
            var currentCalories = 0f
            currentCaloriesTime.forEach { currentCalories += it }
            productRepository.insertPrevDay(
                new = Day(
                    time = date,
                    caloriesDay = userRepository.getUserData().getCaloriesToEat(),
                    currentCalories,
                    eatBoxCalories = currentCaloriesTime
                )
            )
            productRepository.deleteAllProducts()
            return Result.success()
        }
    }
}