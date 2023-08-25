package com.android.easycal.data

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.android.easycal.ui.SettingsActivity
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.mockito.Mockito

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class SplashServiceTest {

    @Mock
    private lateinit var mockContext: Context

    private lateinit var splashService: SplashService

    @BeforeEach
    fun setUp() {
        mockContext = mock(Context::class.java)
        splashService = SplashService(mockContext)
    }

    @Test
    @DisplayName("Expecting Success: Start SettingsActivity with correct Intent when isFirstLaunch is true")
    @Order(1)
    fun startDelayedSuccess() {
        // Set up the shared preferences to indicate that this is the first launch
        val sharedPrefs = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isFirstTime", true)
        editor.commit()

        // Call the function being tested
        splashService.startDelayed()

        // Verify that startActivity was called with the correct intent
        val expectedIntent = Intent(mockContext, SettingsActivity::class.java)
        verify(mockContext).startActivity(expectedIntent)
    }

    @Test
    @DisplayName("Expecting Fail: Start SettingsActivity with incorrect Intent when isFirstLaunch is true")
    @Order(2)
    fun startDelayedFail() {
        // Set up the shared preferences to indicate that this is not the first launch
        val sharedPrefs = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isFirstTime", false)
        editor.commit()

        // Call the function being tested
        splashService.startDelayed()

        // Verify that startActivity was not called with the incorrect intent
        val expectedIntent = Intent(mockContext, SettingsActivity::class.java)
        verify(mockContext, Mockito.never()).startActivity(expectedIntent)
    }

}
