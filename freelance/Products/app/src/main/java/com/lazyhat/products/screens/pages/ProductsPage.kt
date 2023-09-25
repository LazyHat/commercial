package com.lazyhat.products.screens.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.products.R
import com.lazyhat.products.data.ProductPage
import com.lazyhat.products.data.df
import com.lazyhat.products.ui.theme.ProductsTheme

@Composable
fun ProductsPage(pageInfo: ProductPage, back: () -> Unit) {
    Image(
        painterResource(id = R.drawable.img), "back",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = back,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("НАЗАД", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }
            }
            Text(
                text = pageInfo.label,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp),
                modifier = Modifier.padding(10.dp)
            )
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(pageInfo.products) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.name,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
                            )
                            Text(
                                text = df.format(it.cost) + " руб.",
                                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewProductPage() {
    ProductsTheme {
        ProductsPage(pageInfo = ProductPage.TeaCoffeeCacao) {

        }
    }
}