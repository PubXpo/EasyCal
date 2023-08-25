package com.android.easycal.data

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.easycal.ui.MainActivity
import com.android.easycal.ui.SettingsActivity

class SplashService(private val context: Context) {
    // Set the time delay value to 3000ms
    private val delayTime: Long = 3000

    /**
     * Check if the app is launched for first time
     */
    private fun isFirstLaunch(): Boolean {
        val prefs = context.getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
        return prefs.getBoolean("isFirstTime", true)
    }

    /**
     * Update shared preferences to indicate that the application has been launched at least once
     */
    private fun updatePreferences() {
        val prefs = context.getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("isFirstTime", false)
        editor.apply()
    }

    /**
     * Start the appropriate activity based on whether it is the first launch or not
     */
    private fun startDashboardActivity() {
        val intent = if (isFirstLaunch()) {
            // Update preferences and start SettingsActivity activity if it is the first launch
            updatePreferences()
            Intent(context, SettingsActivity::class.java)
        } else {
            // Start MainActivity if it is not the first launch
            Intent(context, MainActivity::class.java)
        }
        context.startActivity(intent)
    }

    /**
     * Delay the start of the Dashboard by delayTime milliseconds and then starts the Dashboard/MainActivity
     */
    fun startDelayed() {
        // Delay the start of the Dashboard by WAIT_DELAY milliseconds
        android.os.Handler().postDelayed({
            startDashboardActivity()
            (context as AppCompatActivity).finish()
        }, delayTime)

        // Logcat info message to indicate that the delay has started
        Log.i("SplashService", "SplashActivity screen delay started")
        // Logcat debug message to indicate the delay time in milliseconds
        Log.d("SplashService", "SplashActivity screen delay time: $delayTime ms")
    }
}

