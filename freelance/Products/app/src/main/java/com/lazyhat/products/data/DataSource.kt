package com.lazyhat.products.data

import java.text.DecimalFormat

enum class ProductPage(val label: String, val products: List<Product>) {
    IceCream(
        "Мороженное",
        listOf(
            "Alpen Gold" costs 60.4f,
            "Чистая линия" costs 50.5f,
            "Русский стандарт" costs 40.5f
        )
    ),
    BabyFood(
        "Детское питание",
        listOf(
            "Фруто няня" costs 30f,
            "Тёма" costs 60f
        )
    ),
    TeaCoffeeCacao(
        "Чай, кофе, какао",
        listOf(
            "Nescafe Gold" costs 400f,
            "Jardin" costs 300f,
            "Nescafe Cappuccino" costs 30f
        )
    ),
    Fruits(
        "Фрукты",
        listOf(
            "Кокос" costs 40f,
            "Банан" costs 15f,
            "Картофель" costs 40f
        )
    )
}

infix fun String.costs(cost: Float): Product = Product(this, cost)

val df = DecimalFormat("#.##")

data class Product(
    val name: String,
    val cost: Float
)