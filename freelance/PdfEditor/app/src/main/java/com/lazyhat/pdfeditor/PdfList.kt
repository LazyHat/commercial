package com.lazyhat.pdfeditor

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity


//Основной экран
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfList() {
    val context = LocalContext.current

    //Лаунчер активити,которая выбирает файл и редактирует его
    val launcher = rememberPathPickerLauncher(onResult = {
        openDocumentEditor(it, context)
    })

    Scaffold(Modifier.fillMaxSize()) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .padding(horizontal = 30.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Редактор PDF",
                    style = TextStyle(
                        fontSize = 30.sp, fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Простой редактор, для удобного просмотра и изменения ваших файлов",
                    softWrap = true,
                    textAlign = TextAlign.Center
                )
            }

            //Фон
            Image(
                painterResource(id = R.drawable.background),
                "back",
                alpha = 0.3f,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            //Кнопка выбора файла
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter).testTag("button"), shape = RoundedCornerShape(8.dp), onClick = {
                launcher.launch(arrayOf("application/pdf"))
            }) {
                Text(
                    text = "Выбрать файл",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

//Контракт активити, которая ищет файл
@Composable
private fun rememberPathPickerLauncher(onResult: (Uri) -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
) {
    Log.w("ListenDoc", it.toString())
    it?.let { onResult(it) }
}

//Запуск активити, которая редактирует PDF
fun openDocumentEditor(uri: Uri, context: Context) {
    DocumentActivity.openDocument(
        context,
        uri,
        ViewerConfig.Builder()
            .multiTabEnabled(false)
            .fullscreenModeEnabled(true)
            .documentEditingEnabled(true)
            .showSaveCopyOption(false)
            .build()
    )
}