package com.android.easycal.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity
import com.android.easycal.R
import com.android.easycal.data.SplashService

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "SplashActivity"
    }

    // declare a SplashService object as a class property
    private lateinit var splashService: SplashService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize the SplashService with the current context
        splashService = SplashService(this)

        // Start the delayed start of the Dashboard activity
        splashService.startDelayed()
        Log.i(TAG, "startDelayed called")

        // Show the activity in fullscreen mode
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
    }
}
