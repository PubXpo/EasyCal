package com.android.easycal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.easycal.R
import com.android.easycal.data.BankHolidayService
import com.android.easycal.data.ThemeService

class BankHolidayActivity : AppCompatActivity() {

    // Initialize services
    private lateinit var bankHolidayService: BankHolidayService
    private lateinit var themeService: ThemeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply visual condition theme based on user selection
        // Create a new instance of the ThemeService and apply the selected theme to the activity
        themeService = ThemeService(this)
        themeService.applyVisualCondition(this)

        setContentView(R.layout.activity_bank_holiday)

        // Initialize views
        bankHolidayService = BankHolidayService(this)
        bankHolidayService.initializeViews()

        // Set up the toolbar with the appropriate theme and title
        themeService.setupToolbar(this)

        // Apply visual condition data from SharedPreferences
        themeService.applyHolidayColorFilters(this)

        // Check cache or API for data and populate the entries
        bankHolidayService.checkAndPopulateCachedData()
        bankHolidayService.checkAndRetrieveData()
    }
}