package com.example.studentstudyplanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.studentstudyplanner.ui.navigation.MainNavGraph
import com.example.studentstudyplanner.ui.theme.StudentStudyPlannerTheme

class ThemeController {
    var darkTheme by mutableStateOf(false)
        private set

    fun toggleDarkMode() {
        darkTheme = !darkTheme
    }
}

val LocalThemeController = compositionLocalOf { ThemeController() }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
        enableEdgeToEdge()
        val themeController = ThemeController()
        setContent {
            CompositionLocalProvider(LocalThemeController provides themeController) {
                StudentStudyPlannerTheme(darkTheme = themeController.darkTheme) {
                    MainNavGraph()
                }
            }
        }
    }
}
