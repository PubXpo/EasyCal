package com.android.easycal.data

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.android.easycal.R
import com.android.easycal.ui.MainActivity
import com.android.easycal.ui.SettingsActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsService(val context: Context) {
    companion object {
        private const val TAG = "SettingsService"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
    }

    /**
     * Setup semester radio group based on the value stored in SharedPreferences
     */
    fun setupSemester() {
        val semesterKey = "semester"
        val semesterRadioGroup =
            (context as SettingsActivity).findViewById<RadioGroup>(R.id.semester_radio_group)
        val semesterTextValue = sharedPreferences.getString(semesterKey, "")
        if (!semesterTextValue.isNullOrEmpty()) {
            val semesterValue = when (semesterTextValue) {
                "2 Semesters" -> R.id.semester_2_radio_button
                "3 Semesters" -> R.id.semester_3_radio_button
                else -> null
            }
            semesterValue?.let { semesterRadioGroup.check(semesterValue) }
        }
        // Logcat info message
        Log.i(TAG, "Semester radio group is set up")
    }

    /**
     * Setup visual condition spinner based on the value stored in SharedPreferences
     */
    fun setupVisualConditionSpinner() {
        val visualConditionKey = "visual_condition"
        val visualConditionSpinner =
            (context as SettingsActivity).findViewById<Spinner>(R.id.visual_condition_spinner)
        val visualConditionValue = sharedPreferences.getString(visualConditionKey, "")
        if (!visualConditionValue.isNullOrEmpty()) {
            val position = (visualConditionSpinner.adapter as ArrayAdapter<String>).getPosition(
                visualConditionValue
            )
            visualConditionSpinner.setSelection(position)
        }
        // Logcat debug message
        Log.d(TAG, "Visual condition spinner is set up")
    }

    /**
     * Setup light mode switch based on the value stored in SharedPreferences
     */
    fun setupToggleModeSwitch() {
        val toggleModeKey = "toggle_mode"
        val lightModeSwitch =
            (context as SettingsActivity).findViewById<SwitchMaterial>(R.id.light_mode_switch)
        val toggleModeValue = sharedPreferences.getBoolean(toggleModeKey, false)
        lightModeSwitch.isChecked = toggleModeValue

        if (!toggleModeValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        // Logcat debug message
        Log.d(TAG, "Toggle mode switch is set up")
    }

    /**
     * Setup save button and handle the saving of settings to SharedPreferences
     */
    // this long function is done for testing, it needs to be reduced at some point
    // to minimize the LOC and complexity
    fun setupSaveButton() {
        // Keys for shared preferences
        val semesterKey = "semester"
        val visualConditionKey = "visual_condition"
        val toggleModeKey = "toggle_mode"

        // Save button view
        val saveButton = (context as SettingsActivity).findViewById<Button>(R.id.btnSaveChanges)

        saveButton.setOnClickListener {
            // Save selected semester to SharedPreferences
            val semesterRadioGroup = context.findViewById<RadioGroup>(R.id.semester_radio_group)
            val semesterTextValue = when (semesterRadioGroup.checkedRadioButtonId) {
                R.id.semester_2_radio_button -> "2 Semesters"
                R.id.semester_3_radio_button -> "3 Semesters"
                else -> ""
            }
            val editor = sharedPreferences.edit()
            editor.putString(semesterKey, semesterTextValue)

            // Save selected visual condition to SharedPreferences
            val visualConditionSpinner =
                context.findViewById<Spinner>(R.id.visual_condition_spinner)
            val selectedVisualCondition = visualConditionSpinner.selectedItem.toString()
            editor.putString(visualConditionKey, selectedVisualCondition)

            // Save selected toggle mode to SharedPreferences
            val lightModeSwitch = context.findViewById<SwitchMaterial>(R.id.light_mode_switch)
            val selectedToggleMode = lightModeSwitch.isChecked
            editor.putBoolean(toggleModeKey, selectedToggleMode)

            // Set night mode based on selected toggle mode
            if (!selectedToggleMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            // Commit the changes to SharedPreferences
            editor.apply()

            // Show a toast message to indicate successful save
            Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()

            // Redirect to dashboard activity
            val dashboardIntent = Intent(context, MainActivity::class.java)
            context.startActivity(dashboardIntent)
            context.finish()
        }
    }
}
