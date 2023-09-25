package ru.lazyhat.work.myhome

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ru.lazyhat.work.myhome.ui.screens.main.MainScreen
import ru.lazyhat.work.myhome.ui.theme.MyHomeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyHomeTheme {
                MainScreen()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("ACTIVITY", "STOP")
    }
}