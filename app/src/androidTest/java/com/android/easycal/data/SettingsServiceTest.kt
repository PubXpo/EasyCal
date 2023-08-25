package com.android.easycal.data

import com.android.easycal.R

import android.content.Context
import android.content.SharedPreferences
import android.widget.RadioGroup
import androidx.test.core.app.ApplicationProvider
import com.android.easycal.ui.SettingsActivity
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.verify


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class SettingsServiceTest {

    private lateinit var settingsService: SettingsService
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var context: SettingsActivity

    @Mock
    private lateinit var radioGroup: RadioGroup

    @BeforeAll
    fun init() {
        MockitoAnnotations.openMocks(this)
        sharedPreferences = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
        settingsService = SettingsService(context)
    }

    @Test
    @DisplayName("Test setupSemester() function successfully")
    @Order(1)
    fun testSetupSemesterSuccess() {
        // Given
        `when`(context.findViewById<RadioGroup>(R.id.semester_radio_group)).thenReturn(radioGroup)
        `when`(radioGroup.checkedRadioButtonId).thenReturn(R.id.semester_2_radio_button)
        `when`(sharedPreferences.getString("semester", "")).thenReturn("2 Semesters")

        // When
        settingsService.setupSemester()

        // Then
        verify(radioGroup).check(R.id.semester_2_radio_button)
    }

    @Test
    @DisplayName("Test setupSemester() function fails")
    @Order(2)
    fun testSetupSemesterFail() {
        // Given
        `when`(context.findViewById<RadioGroup>(R.id.semester_radio_group)).thenReturn(radioGroup)
        `when`(radioGroup.checkedRadioButtonId).thenReturn(R.id.semester_2_radio_button)
        `when`(sharedPreferences.getString("semester", "")).thenReturn("Invalid Value")

        // When
        settingsService.setupSemester()

        // Then
        verify(radioGroup).check(R.id.semester_2_radio_button)
    }


    @Test
    fun setupVisualConditionSpinner() {
    }

    @Test
    fun setupToggleModeSwitch() {
    }

    @Test
    fun setupSaveButton() {
    }
}