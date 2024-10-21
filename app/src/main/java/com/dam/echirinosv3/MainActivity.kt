package com.dam.echirinosv3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dam.echirinosv3.ui.main.MainApp
import com.dam.echirinosv3.ui.main.MainVM
import com.dam.echirinosv3.ui.theme.EChirinosv3Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EChirinosv3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowsSize = calculateWindowSizeClass(this)
                    val mainVM: MainVM = viewModel(factory = MainVM.Factory)
                    MainApp(mainVM, windowsSize)
                }
            }
        }
    }
}
