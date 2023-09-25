package com.lazyhat.products.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.lazyhat.products.NavigationActions
import com.lazyhat.products.R
import com.lazyhat.products.data.ProductPage
import com.lazyhat.products.ui.theme.ProductsTheme

@Composable
fun MainScreen(navigationActions: NavigationActions) {
    Image(
        painterResource(id = R.drawable.img), "back",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    Box(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp),
            text = "Выберите категорию",
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
        )
        Column(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductPage.values().forEach {
                CategoryButton(label = it.label) {
                    navigationActions.navigateToProductsPage(it)
                }
            }
        }
    }
}

@Composable
fun CategoryButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(onClick = onClick, shape = RoundedCornerShape(10.dp)) {
        Text(text = label, style = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.Medium))
    }
}

@Composable
@Preview(showBackground = true)
fun MainPreview() {
    ProductsTheme {
        MainScreen(navigationActions = NavigationActions(rememberNavController()))
    }
}