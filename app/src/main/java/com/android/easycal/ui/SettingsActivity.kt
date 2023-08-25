package com.android.easycal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.easycal.R
import com.android.easycal.data.SettingsService
import com.android.easycal.data.ThemeService

class SettingsActivity : AppCompatActivity() {

    // Initialize services
    private lateinit var settingsService: SettingsService
    private lateinit var themeService: ThemeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply visual condition theme based on user selection
        themeService = ThemeService(this)
        themeService.applyVisualCondition(this)

        setContentView(R.layout.activity_settings)

        // Set up the toolbar with the appropriate theme and title
        themeService.setupToolbar(this)

        // Setup semester, visual condition spinner, toggle mode switch, and save button
        settingsService = SettingsService(this)
        settingsService.setupSemester()
        settingsService.setupVisualConditionSpinner()
        settingsService.setupToggleModeSwitch()
        settingsService.setupSaveButton()
    }
}




