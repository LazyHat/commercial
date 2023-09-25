package com.lazyhat.work.tracker.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.lazyhat.work.tracker.Permissions
import com.lazyhat.work.tracker.ui.screen.MainScreen
import com.lazyhat.work.tracker.ui.theme.TrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackerTheme {
                val permissionRequester = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(), onResult = {
                        it.forEach {
                            Log.w("PERMISSIONS REQUEST", "${it.key}, ${it.value}")
                        }
                    }
                )
                LaunchedEffect(key1 = Unit) {
                    Permissions.values().forEach {
                        Log.w(
                            "PERMISSION CHECK",
                            "${it.name}, ${Permissions.check(it, applicationContext)}"
                        )
                    }
                }
                if (Permissions.getAllIsGranted(applicationContext).any { !it }) {
                    SideEffect {
                        permissionRequester.launch(Permissions.getDenied(applicationContext))
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}