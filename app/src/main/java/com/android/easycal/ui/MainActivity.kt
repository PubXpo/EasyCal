package com.android.easycal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.easycal.R
import com.android.easycal.data.MainService
import com.android.easycal.data.ThemeService


class MainActivity : AppCompatActivity() {

    // Initialize services
    private lateinit var mainService: MainService
    private lateinit var themeService: ThemeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply visual condition theme based on user selection
        themeService = ThemeService(this)
        themeService.applyVisualCondition(this)

        setContentView(R.layout.activity_main)

        // Apply filters to icon background
        themeService.applyColorFilters(this)

        // Initialize views
        mainService = MainService(this)
        mainService.initializeViews()

        // Set click listeners for buttons
        mainService.setListeners()
    }
}
