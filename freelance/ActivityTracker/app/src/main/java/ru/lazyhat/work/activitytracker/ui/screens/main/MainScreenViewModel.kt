package ru.lazyhat.work.activitytracker.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.lazyhat.work.activitytracker.data.contacts.ContactsService
import ru.lazyhat.work.activitytracker.data.models.Contact
import ru.lazyhat.work.activitytracker.data.repository.SettingsRepository
import ru.lazyhat.work.activitytracker.data.service.ServiceController
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val contactsService: ContactsService,
    private val serviceController: ServiceController
) : ViewModel() {

    private val _alertDialogOpened = MutableStateFlow(false)

    private val _serviceEnabledFlow = flow {
        while (true) {
            emit(serviceController.isServiceEnabled())
            kotlinx.coroutines.delay(1.seconds)
        }
    }

    val uiState = combine(
        settingsRepository.settingsFlow,
        _alertDialogOpened,
        _serviceEnabledFlow
    ) { appSettings, alertDialog, serviceEnabled ->
        MainScreenState(
            alertDialog,
            TextFieldState(appSettings.accuracyAccelerometer, null),
            TextFieldState(appSettings.sensorUpdateRate, null),
            TextFieldState(appSettings.allowedTimeOfImmobility, null),
            TextFieldState(appSettings.messageRepeatingRate, null),
            appSettings.timeOfImmobility,
            appSettings.contacts,
            serviceEnabled
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainScreenState.Default
    )

    fun createEvent(e: MainScreenEvent) = onEvent(e)

    private fun onEvent(e: MainScreenEvent): Any = when (e) {
        is MainScreenEvent.UpdateAllowedTimeOfImmobility -> viewModelScope.launch {
            settingsRepository.updateAllowedTimeOfImmobility(e.new)
        }

        is MainScreenEvent.UpdateAccuracyAccelerometer -> viewModelScope.launch {
            settingsRepository.updateAccuracyAccelerometer(e.new)
        }

        is MainScreenEvent.UpdateMessageRepeatingRate -> viewModelScope.launch {
            settingsRepository.updateMessageRepeatingRate(e.new)
        }

        is MainScreenEvent.UpdateSensorUpdateRate -> viewModelScope.launch {
            settingsRepository.updateSensorUpdateRate(e.new)
        }

        is MainScreenEvent.AddNewContact -> viewModelScope.launch {
            settingsRepository.addNewContact(e.new)
        }

        is MainScreenEvent.DeleteContact -> viewModelScope.launch {
            settingsRepository.deleteContact(e.index)
        }

        MainScreenEvent.CloseAlertDialog -> _alertDialogOpened.update { false }
        MainScreenEvent.OpenAlertDialog -> _alertDialogOpened.update { true }
        MainScreenEvent.StartService -> serviceController.startService()
        MainScreenEvent.StopService -> serviceController.stopService()
    }

    fun getContacts(onReceived: (List<Contact>) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            contactsService.getContacts().let {
                if (it == null)
                    onError()
                else
                    onReceived(it.filter { contact -> contact !in uiState.value.savedContacts })
            }
        }
    }
}