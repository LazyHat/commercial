package ru.lazyhat.work.nutriguide.pages.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.lazyhat.work.nutriguide.R
import ru.lazyhat.work.nutriguide.navigation.NavigationActions
import ru.lazyhat.work.nutriguide.navigation.Pages
import ru.lazyhat.work.nutriguide.pages.eat.EatTime
import ru.lazyhat.work.nutriguide.theme.NutriGuideTheme

@Composable
fun TopBar(label: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(14.dp),
        text = label,
        style = TextStyle(fontSize = 25.sp),
        color = MaterialTheme.colorScheme.background
    )
}

@Composable
fun TopBar(currentTime: EatTime, changeTime: (time: EatTime) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        DropDownEatText(selectedItem = currentTime, update = changeTime)
    }
}

@Composable
fun TabBar(currentPageIndex: Int, navActions: NavigationActions) {
    val pages = Pages.listNonNullPages()
    TabRow(
        selectedTabIndex = currentPageIndex,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        pages.forEachIndexed { index, page ->
            Tab(
                selected = index == currentPageIndex,
                onClick = { navActions.navigate(page.name) },
                selectedContentColor = MaterialTheme.colorScheme.background,
                unselectedContentColor = MaterialTheme.colorScheme.background
            ) {
                Image(
                    painterResource(id = page.image),
                    "img",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(
                        if (index == currentPageIndex)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    )
                )
                AnimatedVisibility(visible = index == currentPageIndex,
                    enter = expandVertically(expandFrom = Alignment.Top) { 0 },
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) { 0 }) {
                    Text(
                        text = stringResource(id = page.label),
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.height(3.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownEatText(
    selectedItem: EatTime,
    update: (time: EatTime) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {
        TextField(
            modifier = Modifier
                .menuAnchor(),
            value = stringResource(selectedItem.label),
            readOnly = true,
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.background,
               focusedContainerColor = MaterialTheme.colorScheme.primary,
               unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.background,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.background
            )
        )
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            EatTime.values().forEach {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = it.label)) }, onClick = {
                        update(it)
                        expanded = false
                    })
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier,
    value: String,
    style: TextStyle = TextStyle(fontSize = 16.sp),
    onValueChange: (new: String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = style,
        singleLine = true,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(if (focused) Color.Black else Color.Unspecified),
        decorationBox = {
            Card(
                modifier = modifier,
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .padding(vertical = 3.dp)
                        .padding(start = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty() && !focused) {
                            Text(
                                text = "Поиск",
                                color = Color.Gray.copy(alpha = 0.5f),
                                style = style
                            )
                        }
                        it()
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search"
                    )
                }
            }
        }
    )
}

@Composable
fun EATSTextField(
    modifier: Modifier = Modifier,
    value: String,
    style: TextStyle = TextStyle(fontSize = 16.sp),
    error: String = "",
    hintText: String = "",
    label: @Composable (() -> Unit)? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (new: String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val animColor by animateColorAsState(targetValue = if (error.isNotEmpty()) Color.Red else if (focused) color else Color.Gray)
    var width by remember { mutableStateOf(0.dp) }
    val ds = LocalDensity.current
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .defaultMinSize(minWidth = 200.dp)
            .onGloballyPositioned { width = with(ds) { it.size.width.toDp() } },
        textStyle = style,
        keyboardOptions = keyboardOptions,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(if (focused) Color.Black else Color.Unspecified),
        decorationBox = {
            Column(Modifier.width(width)) {
                label?.invoke()
                Box(Modifier.padding(start = 5.dp)) {
                    if (!focused && value.isEmpty())
                        Text(text = hintText, style = style, color = Color.Gray.copy(alpha = 0.5f))
                    it()
                }
                Box(
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(animColor)
                )
                Text(text = error, color = Color.Red)
            }
        }
    )
}

@Preview
@Composable
private fun PreviewTabBar() {
    NutriGuideTheme {
        TabBar(
            currentPageIndex = 0,
            navActions = NavigationActions(NavHostController(LocalContext.current))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFBBBBBB)
@Composable
private fun PreviewSearchBar() {
    NutriGuideTheme {
        Box(Modifier.padding(10.dp)) {
            SearchBar(Modifier.fillMaxWidth(), "") {}
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFBBBBBB)
@Composable
private fun PreviewTextField() {
    NutriGuideTheme {
        Box(Modifier.padding(10.dp)) {
            EATSTextField(value = "TextField", label = { Text("TextField") }, error = "Error") {}
        }
    }
}