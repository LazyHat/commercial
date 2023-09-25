package com.lazyhat.pdfeditor

import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lazyhat.pdfeditor.ui.theme.PDFEditorTheme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get: Rule
    val rule = createComposeRule()

    @Before
    fun setUp() {
        rule.setContent {
            val context = LocalContext.current
            PDFEditorTheme {
                PdfList()
                Button(
                    onClick = {
                        openDocumentEditor(
                            context = context,
                            uri = "content://com.android.providers.media.documents/document/document%3A1000004080".toUri()
                        )
                    },
                    modifier = Modifier.testTag("buttonDoc")
                ) {
                }
            }
        }
    }

    @Test
    fun backgroundImageTest() {
        rule.onNodeWithTag("img").assertDoesNotExist()
    }

    @Test
    fun button_test() {
        rule.onNodeWithText("Выбрать файл").assertExists()
        rule.onNodeWithTag("button").assertHasClickAction()
    }

    @Test
    fun click_test() {
        rule.onNodeWithTag("button").performClick()
    }

    @Test
    fun editor_test() {
        rule.onNodeWithTag("buttonDoc").performClick()
    }
}