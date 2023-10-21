package ru.lazyhat.work.nutriguide.data

import ru.lazyhat.work.nutriguide.pages.eat.EatTime
import ru.lazyhat.work.nutriguide.pages.eat.pages.ResultDialogState
import ru.lazyhat.work.nutriguide.pages.home.HomeEatBoxState
import ru.lazyhat.work.nutriguide.pages.home.HomeState
import ru.lazyhat.work.nutriguide.staticdata.DataSource.df
import kotlinx.serialization.json.Json
import ru.lazyhat.work.nutriguide.data.products.NutritionFacts
import ru.lazyhat.work.nutriguide.data.products.ProductInfo
import ru.lazyhat.work.nutriguide.data.products.ProductState
import ru.lazyhat.work.nutriguide.data.products.db.day.DateTime
import ru.lazyhat.work.nutriguide.data.products.db.day.Day
import ru.lazyhat.work.nutriguide.data.products.db.day.LocalDay
import ru.lazyhat.work.nutriguide.data.products.db.infos.LocalInfo
import ru.lazyhat.work.nutriguide.data.products.db.products.LocalProduct
import ru.lazyhat.work.nutriguide.data.userdata.LocalUser
import ru.lazyhat.work.nutriguide.data.userdata.User

fun LocalUser.toExternal(): User =
    User(
        height = this.height,
        age = this.age,
        weight = this.weight,
        gender = this.gender,
        activity = activeness
    )

fun LocalUser.copy(user: User): LocalUser =
    this.copy(
        height = user.height,
        age = user.age,
        weight = user.weight,
        gender = user.gender,
        activeness = user.activity
    )

fun ProductState.toLocal(time: EatTime): LocalProduct =
    LocalProduct(time.name, info.id, weight)

fun List<Pair<EatTime, ProductState>>.filter(time: EatTime): List<ProductState> =
    this.filter { it.first == time }.map { it.second }

fun ResultDialogState.toProductState(): ProductState = ProductState(weight, info)

fun String.formatAsFloat(): String =
    this.filter { it.isDigit() || it == '.' }.let {
        it.count { c -> c == '.' }.let { count ->
            if (count > 1) it.filterIndexed { index, c -> !(c == '.' && index != it.indexOfLast { k -> k == '.' }) }
            else if (count == 1) it.dropLast(it.substringAfter('.').length.let { l -> if (l == 0) 0 else l - 1 })
            else it
        }
    }

fun NutritionFacts.getCurrentNutrition(weight: Float): NutritionFacts =
    NutritionFacts(
        this.calories * weight / 100,
        this.proteins * weight / 100,
        this.fats * weight / 100,
        this.carbohydrates * weight / 100
    )

fun LocalInfo.toExternal(): ProductInfo =
    ProductInfo(
        id,
        label,
        NutritionFacts(calories100, proteins100, fats100, carbohydrates100),
        pieceWeight
    )

fun ProductInfo.toLocal(): LocalInfo = LocalInfo(
    id,
    label,
    nutrition100.calories,
    nutrition100.proteins,
    nutrition100.fats,
    nutrition100.carbohydrates,
    pieceWeight
)

fun Day.toHomeState(): HomeState {
    require(this.eatBoxCalories.size == 4) { "EAT BOX SIZE NOT 4" }
    return HomeState(
        date = this.time,
        this.caloriesDay,
        this.caloriesCurrent,
        this.caloriesDay - this.caloriesCurrent,
        listOf(
            HomeEatBoxState(
                EatTime.BreakFast,
                this.eatBoxCalories[0] / this.caloriesDay * 100,
                this.eatBoxCalories[0]
            ),
            HomeEatBoxState(
                EatTime.Lunch,
                this.eatBoxCalories[1] / this.caloriesDay * 100,
                this.eatBoxCalories[1]
            ),
            HomeEatBoxState(
                EatTime.Dinner,
                this.eatBoxCalories[2] / this.caloriesDay * 100,
                this.eatBoxCalories[2]
            ),
            HomeEatBoxState(
                EatTime.Snack,
                this.eatBoxCalories[3] / this.caloriesDay * 100,
                this.eatBoxCalories[3]
            )
        )
    )
}

fun DateTime?.toString(): String = if (this == null) "Сегодня"
else "${if (this.day < 10) "0${this.day}" else this.day}.${if (this.month < 10) "0${this.month}" else this.month}.${this.year}"

fun DateTime.encodeToString(): String = Json.encodeToString(DateTime.serializer(), this)
fun String.decodeToDateTime(): DateTime = Json.decodeFromString(DateTime.serializer(), this)

fun List<Float>.encodeToString(): String = this.joinToString(separator = "-") {
   df.format(it).replace(',', '.')
}

fun String.decodeToListFloat(): List<Float> = this.split('-').map { it.toFloat() }


fun LocalDay.toDay(): Day = Day(
    time = this.time.decodeToDateTime(),
    caloriesDay,
    currentCalories,
    eatBoxCalories.decodeToListFloat()
)

fun Day.toLocal(): LocalDay = LocalDay(
    this.time.encodeToString(),
    caloriesDay,
    caloriesCurrent,
    eatBoxCalories.encodeToString()
)