package com.android.easycal.data

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.LinearLayout
import com.android.easycal.R
import com.android.easycal.ui.BankHolidayActivity
import com.android.easycal.ui.MainActivity
import com.android.easycal.ui.MonthViewActivity
import com.android.easycal.ui.SettingsActivity

class MainService(private val activity: MainActivity) {
    companion object {
        private const val TAG = "MainService"
    }

    // Button - resources
    private lateinit var calendar: LinearLayout
    private lateinit var resource: LinearLayout
    private lateinit var holiday: LinearLayout
    private lateinit var settings: LinearLayout

    /**
     * Initialize the views for the buttons.
     */
    fun initializeViews() {
        calendar = activity.findViewById(R.id.btnCalendar)
        resource = activity.findViewById(R.id.btnResources)
        holiday = activity.findViewById(R.id.btnHolidays)
        settings = activity.findViewById(R.id.btnSettings)
    }

    /**
     * Set the click listeners for the buttons.
     */
    fun setListeners() {
        // calendar button navigates to calendar month view activity
        calendar.setOnClickListener {
            // navigate to calendar month view activity
            val intent = Intent(activity, MonthViewActivity::class.java)
            activity.startActivity(intent)
            Log.d(TAG, "Calendar button clicked")
        }

        // holiday button navigates to bank holiday activity
        holiday.setOnClickListener {
            // navigate to bank holiday activity
            val intent = Intent(activity, BankHolidayActivity::class.java)
            activity.startActivity(intent)
            Log.d(TAG, "Holiday button clicked")
        }

        // resource button navigates to an external link via browser
        resource.setOnClickListener {
            // navigate to external link via browser
            val url =
                "https://www.staffs.ac.uk/students/support/student-inclusion-team/disability-support"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity.startActivity(intent)
            Log.d(TAG, "Resource button clicked")
        }

        // settings button navigates to settings activity
        settings.setOnClickListener {
            // navigate to settings activity
            val intent = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(intent)
            Log.d(TAG, "Settings button clicked")
        }
    }
}