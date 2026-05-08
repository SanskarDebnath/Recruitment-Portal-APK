package com.example.recruitment_portal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.recruitment_portal.ui.screens.MainScreen
import com.example.recruitment_portal.ui.theme.RecruitmentPortalTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecruitmentPortalTheme {
                MainScreen()
            }
        }
    }
}
