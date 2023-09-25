package ru.lazyhat.work.activitytracker.ui.screens.main.customui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.lazyhat.work.activitytracker.data.models.Contact

@Composable
fun ContactBox(
    modifier: Modifier = Modifier,
    contact: Contact,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier.padding(vertical = 10.dp)
            .let {
                if (onClick != null)
                    it.clickable(onClick = onClick)
                else
                    it
            }
    ) {
        Text(contact.name)
        Text(contact.phone)
    }
}