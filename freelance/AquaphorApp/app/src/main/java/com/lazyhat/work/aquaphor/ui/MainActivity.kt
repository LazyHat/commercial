package com.lazyhat.work.aquaphor.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.lazyhat.work.aquaphor.ui.navigation.MainNavHost
import com.lazyhat.work.aquaphor.ui.navigation.MainNavRoutes
import com.lazyhat.work.aquaphor.ui.screens.LoadingScreen
import com.lazyhat.work.aquaphor.ui.theme.AquaphorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//Активити - главный экран, на котором распологается все остальное
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted: Boolean ->
                    if (isGranted)
                        Log.w("Notification Permission Request", "GRANTED")
                    else
                        Log.w("Notification Permission Request", "DENIED")
                }
            )

            LaunchedEffect(key1 = Unit) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        "android.permission.POST_NOTIFICATIONS"
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.w("Notification Permission Check", "DENIED")
                    launcher.launch("android.permission.POST_NOTIFICATIONS")
                } else
                    Log.w("Notification Permission Check", "GRANTED")
            }

            val viewModel: MainActivityViewModel = hiltViewModel()
            val scope = rememberCoroutineScope()

            val navController = rememberNavController()
            var startDestination by remember { mutableStateOf(MainNavRoutes.Register) }
            var loading by remember { mutableStateOf(true) }

            LaunchedEffect(key1 = Unit) {
                scope.launch {
                    if (viewModel.dataIsNotEmpty())
                        startDestination = MainNavRoutes.Home
                    delay(500)
                    loading = false
                }
            }
            AquaphorTheme {
                MainNavHost(navController = navController, startDestination = startDestination)
                AnimatedVisibility(visible = loading, exit = fadeOut()) {
                    LoadingScreen()
                }
            }
        }
    }
}