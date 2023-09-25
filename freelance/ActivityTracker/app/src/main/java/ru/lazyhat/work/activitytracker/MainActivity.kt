package ru.lazyhat.work.activitytracker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.AndroidEntryPoint
import ru.lazyhat.work.activitytracker.ui.screens.ErrorPermissionsScreen
import ru.lazyhat.work.activitytracker.ui.screens.LoadingScreen
import ru.lazyhat.work.activitytracker.ui.screens.main.MainScreen
import ru.lazyhat.work.activitytracker.ui.theme.ActivityTrackerTheme

private val permissions = arrayOf(
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.SEND_SMS,
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.POST_NOTIFICATIONS
    else Manifest.permission.ACCESS_NOTIFICATION_POLICY
)

data class ModuleState(
    val isLoading: Boolean = true,
    val isError: Boolean = false
)

class ActivityState {
    private var permissionsState by mutableStateOf(ModuleState())

    enum class Screen {
        Loading,
        Main,
        PermissionError
    }

    fun permissionReset() {
        permissionsState = ModuleState()
    }

    fun permissionsGranted() {
        permissionsState = permissionsState.copy(isLoading = false)
    }

    fun permissionError() {
        permissionsState = permissionsState.copy(isError = true)
    }

    fun getScreen(): Screen {
        return if (permissionsState.isError)
            Screen.PermissionError
        else if (permissionsState.isLoading)
            Screen.Loading
        else
            Screen.Main
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by remember { mutableStateOf(ActivityState()) }
            val context = LocalContext.current
            val launcher = rememberPermissionRequester {
                if (it) {
                    state.permissionsGranted()
                } else {
                    state.permissionError()
                }
            }
            LaunchedEffect(key1 = Unit) {
                if (context.checkPermissions(permissions))
                    state.permissionsGranted()
                else
                    launcher.launch(permissions)
            }
            ActivityTrackerTheme {
                Crossfade(targetState = state)
                {
                    when (it.getScreen()) {
                        ActivityState.Screen.Loading -> LoadingScreen()
                        ActivityState.Screen.Main -> MainScreen()
                        ActivityState.Screen.PermissionError -> ErrorPermissionsScreen {
                            state.permissionReset()
                            launcher.launch(
                                permissions
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Context.checkPermissions(permissions: Array<String>): Boolean =
    permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }

@Composable
private fun rememberPermissionRequester(onResultAll: (Boolean) -> Unit): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { results ->
            onResultAll(results.all { it.value })
        }
    )