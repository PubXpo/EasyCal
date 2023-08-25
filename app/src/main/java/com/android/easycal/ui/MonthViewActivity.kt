package com.android.easycal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.easycal.R
import com.android.easycal.data.MonthViewService
import com.android.easycal.data.ThemeService


class MonthViewActivity : AppCompatActivity() {

    // Initialize services
    private lateinit var monthViewService: MonthViewService
    private lateinit var themeService: ThemeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply visual condition theme based on user selection
        // Create a new instance of the ThemeService and apply the selected theme to the activity
        themeService = ThemeService(this)
        themeService.applyVisualCondition(this)

        setContentView(R.layout.activity_month_view)

        // Set up the toolbar with the appropriate theme and title
        themeService.setupToolbar(this)

        // Set up the semester text view to display the current semester
        monthViewService = MonthViewService(this)
        monthViewService.setupSemesterTextView()

        // Load data for the current month from the database and display it in the calendar view
        monthViewService.loadMonthData()

        // Set up the calendar views with the appropriate data and listeners
        monthViewService.setupMonthViews()

        // Set up the navigation buttons with the appropriate listeners
        monthViewService.setupNavigationButtons()
    }

}
