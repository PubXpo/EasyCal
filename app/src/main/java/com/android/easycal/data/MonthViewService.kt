package com.android.easycal.data

import android.content.ContentValues
import android.content.Context
import android.content.res.TypedArray
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.android.easycal.R
import com.android.easycal.ui.MonthViewActivity
import java.util.Calendar
import java.util.Locale

class MonthViewService(private val activity: MonthViewActivity) {

    // Shared preferences for storing selected semester
    private val sharedPreferences by lazy {
        activity.getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
    }

    // Constants for semester selection
    private val semester2 = "2 Semesters"
    private val semester3 = "3 Semesters"

    // Arrays for month images, titles, and descriptions
    private lateinit var monthImages: TypedArray
    private lateinit var monthTitles: Array<String>
    private lateinit var monthDescriptions: Array<CharSequence>

    // Current month index
    private var currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH)

    // Views to setup
    private lateinit var semesterTextView: TextView
    private lateinit var monthImageView: ImageView
    private lateinit var monthDescriptionTextView: TextView

    /**
     * Set up the semester text view by getting the selected semester from shared preferences
     */
    fun setupSemesterTextView() {
        semesterTextView = activity.findViewById(R.id.semester_text_view)
        val semester = sharedPreferences.getString("semester", "")
        semesterTextView.text = semester
    }

    /**
     * Load month data based on the selected semester
     * by getting details from the resources folder
     */
    fun loadMonthData() {
        val semester = sharedPreferences.getString("semester", "")
        monthImages = when (semester) {
            semester2 -> activity.resources.obtainTypedArray(R.array.month_images_2s)
            semester3 -> activity.resources.obtainTypedArray(R.array.month_images_3s)
            else -> {
                Log.w("MonthViewActivity", "Invalid semester: $semester")
                activity.resources.obtainTypedArray(R.array.month_images_2s)
            }
        }
        monthTitles = activity.resources.getStringArray(R.array.month_titles)
        monthDescriptions = when (semester) {
            semester2 -> activity.resources.getTextArray(R.array.month_descriptions_2s)
            semester3 -> activity.resources.getTextArray(R.array.month_descriptions_3s)
            else -> {
                Log.w("MonthViewActivity", "Invalid semester selected: $semester")
                activity.resources.getTextArray(R.array.month_descriptions_2s)
            }
        }
    }

    /**
     * Set up the month image and description based on the current month
     */
    fun setupMonthViews() {
        monthImageView = activity.findViewById(R.id.calendar_image)
        monthDescriptionTextView = activity.findViewById(R.id.calendar_details)

        val calendar = Calendar.getInstance()
        currentMonthIndex = calendar.get(Calendar.MONTH)

        for (i in monthTitles.indices) {
            if (monthTitles[i].lowercase(Locale.ROOT)
                    .contains(
                        calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                            .lowercase(Locale.ROOT)
                    )
            ) {
                currentMonthIndex = i
                break
            }
        }

        if (currentMonthIndex == -1) {
            currentMonthIndex = 0
            Log.w("MonthViewActivity", "Unable to find current month in month titles array")
        }

        monthImageView.setImageResource(monthImages.getResourceId(currentMonthIndex, -1))
        monthDescriptionTextView.text = HtmlCompat.fromHtml(
            monthDescriptions[currentMonthIndex].toString(),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        updateMonthView()
    }

    /**
     * Sets up navigation buttons for switching between academic months
     * Displays a toast message if it is the first or last academic month.
     */
    fun setupNavigationButtons() {
        //  When the next button is clicked, the current month index is incremented
        //  if it is not the last academic month.
        val nextButton = activity.findViewById<Button>(R.id.next_month_button)
        nextButton.setOnClickListener {
            if (currentMonthIndex == monthImages.length() - 1) {
                Toast.makeText(activity, "This is the last academic month", Toast.LENGTH_SHORT)
                    .show()
            } else {
                currentMonthIndex++
                updateMonthView()
            }
        }

        // When the previous button is clicked, the current month index is decremented
        // if it is not the first academic month.
        val prevButton = activity.findViewById<Button>(R.id.prev_month_button)
        prevButton.setOnClickListener {
            if (currentMonthIndex == 0) {
                Toast.makeText(activity, "This is the first academic month", Toast.LENGTH_SHORT)
                    .show()
            } else {
                currentMonthIndex--
                updateMonthView()
            }
        }
    }

    /**
     * Update teh month view by setting the current month's image and description in the UI
     * Log the current month's index, title, and description for debugging purposes.
     */
    private fun updateMonthView() {
        // Get the month image
        val currentMonthDrawableId = monthImages.getResourceId(currentMonthIndex, -1)
        monthImageView.setImageResource(currentMonthDrawableId)

        // Get the month description
        monthDescriptionTextView.text = monthDescriptions[currentMonthIndex]

        // Debugging log cat messages
        Log.d(ContentValues.TAG, "Month index: $currentMonthIndex")
        Log.d(ContentValues.TAG, "Month title: ${monthTitles[currentMonthIndex]}")
        Log.d(ContentValues.TAG, "Month description: ${monthDescriptions[currentMonthIndex]}")
    }
}


