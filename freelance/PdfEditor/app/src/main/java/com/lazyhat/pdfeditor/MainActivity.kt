package com.lazyhat.pdfeditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.pdfeditor.ui.theme.PDFEditorTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Корутины
            val scope = rememberCoroutineScope()

            //Состояние загрузки
            var loading by remember { mutableStateOf(true) }

            //Задержка в секунду, для экрана запуска
            LaunchedEffect(key1 = Unit, block = {
                scope.launch {
                    delay(1000)
                    loading = false
                }
            })

            PDFEditorTheme() {
                AnimatedVisibility(visible = loading, exit = fadeOut()) {
                    //Экран запуска приложения
                    LoadingScreen()
                }
                AnimatedVisibility(visible = !loading, enter = fadeIn()) {
                    //Основной интерфейс
                    PdfList()
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            //Иконка приложения
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                "launcher",
                modifier = Modifier.size(230.dp)
            )
            //Текст к ней
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
        }
    }
}