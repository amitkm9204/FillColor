package com.example.fillcolor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.fillcolor.model.ColoringTemplate
import com.example.fillcolor.ui.screens.ColoringScreen
import com.example.fillcolor.ui.screens.TemplateSelectionScreen
import com.example.fillcolor.ui.theme.FillColorTheme
import com.example.fillcolor.utils.LevelManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LevelManager.init(this)
        enableEdgeToEdge()
        setContent {
            FillColorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KidsColoringApp()
                }
            }
        }
    }
}

@Composable
fun KidsColoringApp() {
    var selectedTemplate by remember { mutableStateOf<ColoringTemplate?>(null) }

    val currentTemplate = selectedTemplate
    if (currentTemplate == null) {
        TemplateSelectionScreen(
            onSelectTemplate = { template ->
                selectedTemplate = template
            }
        )
    } else {
        ColoringScreen(
            template = currentTemplate,
            onBackClick = {
                selectedTemplate = null
            },
            onChooseAnotherPicture = {
                selectedTemplate = null
            }
        )
    }
}
