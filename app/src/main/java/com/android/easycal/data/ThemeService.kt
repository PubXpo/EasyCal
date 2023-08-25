package com.android.easycal.data

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.easycal.R
import com.android.easycal.ui.MainActivity
import com.google.android.material.appbar.MaterialToolbar

class ThemeService(private val context: Context) {
    companion object {
        private const val TAG = "ThemeService"
    }

    // Initialize shared preferences
    private val sharedPreferences by lazy {
        context.getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
    }

    // Background - resources
    private lateinit var bgCalendar: ImageView
    private lateinit var bgResource: ImageView
    private lateinit var bgHoliday: ImageView
    private lateinit var bgSettings: ImageView

    // Initialize views
    private fun initializeViews(activity: AppCompatActivity) {
        bgCalendar = activity.findViewById(R.id.calendar_bg)
        bgResource = activity.findViewById(R.id.resources_bg)
        bgHoliday = activity.findViewById(R.id.holidays_bg)
        bgSettings = activity.findViewById(R.id.settings_bg)
    }

    /**
     * Apply the selected visual condition theme
     */
    fun applyVisualCondition(activity: AppCompatActivity) {
        // Retrieve the visual condition from shared preferences
        val visualCondition = sharedPreferences.getString("visual_condition", "")
        // Map the visual condition to the corresponding theme
        val theme = when (visualCondition) {
            "Protanopia" -> R.style.Base_Theme_EasyCal_Protanopia
            "Deuteranopia" -> R.style.Base_Theme_EasyCal_Deuteranopia
            "Tritanopia" -> R.style.Base_Theme_EasyCal_Tritanopia
            "Achromatopsia" -> R.style.Base_Theme_EasyCal_Achromatopsia
            else -> R.style.Base_Theme_EasyCal
        }
        // Apply the theme to the activity
        activity.setTheme(theme)
        // Logcat debug message
        Log.d(TAG, "Applied visual condition: $visualCondition")
    }

    /**
     * Setup the toolbar
     */
    fun setupToolbar(activity: AppCompatActivity) {
        // Retrieve the MaterialToolbar from the layout
        val toolbar: MaterialToolbar = activity.findViewById(R.id.topBar)
        // Set the navigation click listener to navigate back to the dashboard activity
        toolbar.setNavigationOnClickListener {
            val dashboardIntent = Intent(activity, MainActivity::class.java)
            context.startActivity(dashboardIntent)
            activity.finish()
        }
        // Logcat info message
        Log.i(TAG, "Toolbar is set up")
    }

    /**
     * Check visual preference and adjust icon colors
     */
    fun applyColorFilters(activity: AppCompatActivity) {
        // Get the visual condition from shared preferences
        val visualCondition = sharedPreferences.getString("visual_condition", "")
        Log.d(TAG, "Visual condition is: $visualCondition")

        // Initialize the views
        initializeViews(activity)

        // Set the colors based on the visual condition
        val colors = when (visualCondition) {
            "Protanopia" -> listOf(
                R.color.protanopia_red,
                R.color.protanopia_yellow,
                R.color.protanopia_teal,
                R.color.protanopia_green
            )

            "Deuteranopia" -> listOf(
                R.color.deuteranopia_red,
                R.color.deuteranopia_yellow,
                R.color.deuteranopia_teal,
                R.color.deuteranopia_green
            )

            "Tritanopia" -> listOf(
                R.color.tritanopia_red,
                R.color.tritanopia_yellow,
                R.color.tritanopia_teal,
                R.color.tritanopia_green
            )

            "Achromatopsia" -> listOf(
                R.color.achromatopsia_gray,
                R.color.achromatopsia_gray,
                R.color.achromatopsia_gray,
                R.color.achromatopsia_gray
            )

            else -> listOf(
                R.color.red,
                R.color.yellow,
                R.color.teal,
                R.color.green
            )
        }
        Log.d(TAG, "Setting icon colors to: $colors")

        // Set the background tint of each icon to the corresponding color
        bgCalendar.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(activity, colors[0]))
        bgHoliday.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(activity, colors[1]))
        bgResource.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(activity, colors[2]))
        bgSettings.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(activity, colors[3]))
    }

    /**
     * Apply visual color filters to the nextHolidayDetails view based on user's visual preference
     */
    fun applyHolidayColorFilters(activity: AppCompatActivity) {
        Log.i(TAG, "Applying holiday color filters")
        val visualCondition = sharedPreferences.getString("visual_condition", "")
        val colorResourceId = when (visualCondition) {
            "Protanopia" -> R.color.protanopia_red_transparent
            "Deuteranopia" -> R.color.deuteranopia_red_transparent
            "Tritanopia" -> R.color.tritanopia_red_transparent
            "Achromatopsia" -> R.color.achromatopsia_white
            else -> R.color.red_transparent
        }
        val nextHolidayDetails = activity.findViewById<TextView>(R.id.nextHolidayDetails)
        nextHolidayDetails.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(activity, colorResourceId)
        )
        Log.d(
            TAG,
            "Holiday color filter applied: visualCondition=$visualCondition, colorResourceId=$colorResourceId"
        )
    }

}