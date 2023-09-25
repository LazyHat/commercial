package com.lazyhat.work.aquaphor.ui.screens.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.work.aquaphor.R
import com.lazyhat.work.aquaphor.ui.textfields.DatePickerTextField
import com.lazyhat.work.aquaphor.ui.textfields.DropDownStringTextField
import com.lazyhat.work.aquaphor.ui.theme.BigRalewayStyle
import com.lazyhat.work.aquaphor.ui.theme.RalewayFamily
import com.lazyhat.work.aquaphor.ui.theme.RobotoFamily

//Экран регистрации

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel = hiltViewModel(),
    onResult: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isOpenedDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.installDate)

    Column(
        Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painterResource(id = R.drawable.header),
                "header",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                stringResource(id = R.string.register_title),
                style = BigRalewayStyle,
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .padding(10.dp)
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(id = R.string.filter),
                style = TextStyle(
                    fontFamily = RalewayFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.W400
                )
            )
            DropDownStringTextField(
                items = uiState.availableFilters,
                currentValue = uiState.currentFilter,
                hint = stringResource(id = R.string.enter_filter),
                fontSize = 20.sp,
                onValueChange = { viewModel.createEvent(RegisterScreenEvent.FilterChange(it)) })
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(id = R.string.install_date),
                style = TextStyle(
                    fontFamily = RalewayFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.W400
                )
            )
            DatePickerTextField(
                modifier = Modifier.fillMaxWidth(),
                currentValue = uiState.installDate,
                hint = stringResource(id = R.string.enter_install_date),
                fontSize = 20.sp,
            ) {
                isOpenedDialog = true
            }
        }
        AnimatedVisibility(visible = uiState.error != null) {
            Text(color = Color.Red, text = uiState.error ?: "")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            onClick = { viewModel.createEvent(RegisterScreenEvent.Save(onResult)) }
        ) {
            Text(
                stringResource(id = R.string.save_button),
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.W400
                ),
                textAlign = TextAlign.Center
            )
        }
    }
    if (isOpenedDialog)
        DatePickerDialog(onDismissRequest = { isOpenedDialog = false }, confirmButton = {
            Button(onClick = {
                if (datePickerState.selectedDateMillis != null) {
                    viewModel.createEvent(RegisterScreenEvent.DateChange(datePickerState.selectedDateMillis))
                    isOpenedDialog = false
                }
            }) {
                Text(stringResource(id = R.string.pick_date))
            }
        }) {
            DatePicker(state = datePickerState)
        }
}
