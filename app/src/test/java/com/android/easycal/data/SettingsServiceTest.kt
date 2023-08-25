package com.android.easycal.data

import com.android.easycal.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import com.android.easycal.data.SettingsService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsServiceTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockLayoutInflater: LayoutInflater

    @Mock
    private lateinit var mockView: View

    @Mock
    private lateinit var mockRadioGroup: RadioGroup

    @Mock
    private lateinit var mockSpinner: Spinner

    @Mock
    private lateinit var mockSwitch: Switch

    @Mock
    private lateinit var mockButton: Button

    private lateinit var settingsService: SettingsService

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        settingsService = SettingsService(mockContext)
        `when`(mockContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(mockLayoutInflater)
        `when`(mockLayoutInflater.inflate(R.layout.activity_settings, null, false)).thenReturn(mockView)
        `when`(mockView.findViewById<RadioGroup>(R.id.semester_radio_group)).thenReturn(mockRadioGroup)
        `when`(mockView.findViewById<Spinner>(R.id.visual_condition_spinner)).thenReturn(mockSpinner)
        `when`(mockView.findViewById<Switch>(R.id.light_mode_switch)).thenReturn(mockSwitch)
        `when`(mockView.findViewById<Button>(R.id.btnSaveChanges)).thenReturn(mockButton)
    }

    @Test
    @DisplayName("Test setupSemester function success")
    fun testSetupSemesterSuccess() {
        `when`(mockContext.getSharedPreferences("shared_semester", Context.MODE_PRIVATE)).thenReturn(mock())
        `when`(mockRadioGroup.checkedRadioButtonId).thenReturn(R.id.semester_2_radio_button)
        settingsService.setupSemester()
        assertEquals(R.id.semester_2_radio_button, mockRadioGroup.checkedRadioButtonId)
    }

    @Test
    @DisplayName("Test setupSemester function fail")
    fun testSetupSemesterFail() {
        `when`(mockContext.getSharedPreferences("shared_semester", Context.MODE_PRIVATE)).thenReturn(mock())
        `when`(mockRadioGroup.checkedRadioButtonId).thenReturn(0)
        settingsService.setupSemester()
        assertEquals(0, mockRadioGroup.checkedRadioButtonId)
    }





//    @Test
//    fun setupVisualConditionSpinner() {
//    }
//
//    @Test
//    fun setupToggleModeSwitch() {
//    }
//
//    @Test
//    fun setupSaveButton() {
//    }
}