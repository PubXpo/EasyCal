package com.android.easycal.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.android.easycal.R
import com.android.easycal.ui.BankHolidayActivity
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BankHolidayService(private val activity: BankHolidayActivity) {
    companion object {
        private const val TAG = "BankHolidayService"
    }

    // Shared preferences for storing selected semester
    private val sharedPreferences by lazy {
        activity.getSharedPreferences("shared_semester", Context.MODE_PRIVATE)
    }

    // URL of the UK gov API
    private val apiUrl = "https://www.gov.uk/bank-holidays.json"

    // Views in the layout
    private lateinit var nextHolidayTitle: TextView
    private lateinit var nextHolidayDetails: TextView
    private lateinit var upcomingHolidaysTitle: TextView
    private lateinit var upcomingHolidaysList: ListView

    /**
     * Initialize the views for the buttons.
     */
    fun initializeViews() {
        nextHolidayTitle = activity.findViewById(R.id.nextHolidayTitle)
        nextHolidayDetails = activity.findViewById(R.id.nextHolidayDetails)
        upcomingHolidaysTitle = activity.findViewById(R.id.upcomingHolidaysTitle)
        upcomingHolidaysList = activity.findViewById(R.id.upcomingHolidaysList)
    }

    /**
     * Check if cached data exists and use cached data to populate views
     */
    fun checkAndPopulateCachedData() {
        Log.d(TAG, "checkAndPopulateCachedData: Checking for cached data")
        val cachedData = sharedPreferences.getString("holidayData", null)
        if (cachedData != null) {
            Log.d(TAG, "checkAndPopulateCachedData: Cached data found, populating views")
            populateViews(JSONObject(cachedData))
        }
    }

    /**
     * Check device info for available network
     */
    private fun isNetworkAvailable(): Boolean {
        Log.d(TAG, "Checking network availability")
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * Check for network connectivity and retrieve data from API
     */
    fun checkAndRetrieveData() {
        if (isNetworkAvailable()) {
            Log.d(TAG, "checkAndRetrieveData: Network is available, retrieving data from API")
            retrieveDataFromApi()
        } else {
            Log.d(TAG, "checkAndRetrieveData: No network connectivity, showing toast message")
            Toast.makeText(
                activity,
                "No internet connectivity. Please check your network connection.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Retrieve data from API
     */
    private fun retrieveDataFromApi() {
        Log.d(TAG, "retrieveDataFromApi: Making API request")
        makeApiRequest(apiUrl) { responseData ->
            // Cache the retrieved data
            sharedPreferences.edit().putString("holidayData", responseData.toString()).apply()
            Log.d(TAG, "retrieveDataFromApi: Retrieved data cached successfully")

            // Save the retrieved data to a local file
            val fileName = "bank_holidays.json"
            saveDataToFile(fileName, responseData.toString())
            Log.d(TAG, "retrieveDataFromApi: Retrieved data saved to file successfully")

            // Use the retrieved data to populate views
            populateViews(responseData)
            Log.d(TAG, "retrieveDataFromApi: Retrieved data used to populate views successfully")
        }
    }

    /**
     * Make an API request
     */
    private fun makeApiRequest(urlString: String, onSuccess: (responseData: JSONObject) -> Unit) {
        Log.d(TAG, "makeApiRequest: Making API request to $urlString")

        // Create a new concurrent thread to perform the network request
        Thread {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            try {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                val responseData = JSONObject(response)

                // Use a Handler to run the onSuccess callback on the main thread
                Handler(Looper.getMainLooper()).post {
                    onSuccess(responseData)
                    Log.d(TAG, "makeApiRequest: API request successful, response: $responseData")
                }
            } catch (e: Exception) {
                e.printStackTrace()

                // Use a Handler to log the error message on the main thread
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "makeApiRequest: API request failed, error message: ${e.message}")
                }
            } finally {
                connection.disconnect()
                Log.d(TAG, "makeApiRequest: Disconnected from API")
            }
        }.start()
    }

    /**
     * Save data to a file
     */
    private fun saveDataToFile(fileName: String, data: String) {
        Log.d(TAG, "saveDataToFile: Saving data to file: $fileName")
        val outputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE)
        outputStream.write(data.toByteArray())
        outputStream.close()
        Log.d(TAG, "saveDataToFile: Data saved to file successfully")
    }

    /**
     * Populate views with data from API response
     * Check device info for local date and list holidays that come after
     */
    @SuppressLint("SetTextI18n")
    private fun populateViews(data: JSONObject) {
        Log.d(TAG, "Populating views")
        val events = data.getJSONObject("england-and-wales").getJSONArray("events")
        val nextHolidayData = getNextHolidayData(events)
        val upcomingHolidaysData = getUpcomingHolidaysData(events)
        populateNextHolidayView(nextHolidayData)
        populateUpcomingHolidaysView(upcomingHolidaysData)
    }

    /**
     * Get the next upcoming holiday from the given JSONArray of holiday events
     */
    private fun getNextHolidayData(events: JSONArray): JSONObject? {
        Log.d(TAG, "Getting next holiday data")
        val currentDate = Calendar.getInstance().time
        for (i in 0 until events.length()) {
            val holidayData = events.getJSONObject(i)
            val holidayDate = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(holidayData.getString("date"))
            if (holidayDate != null) {
                if (holidayDate >= currentDate) {
                    return holidayData
                }
            }
        }
        return null
    }

    /**
     * Get a list of upcoming holidays from the given JSONArray of holiday events
     */
    private fun getUpcomingHolidaysData(events: JSONArray): List<JSONObject> {
        Log.d(TAG, "Getting upcoming holidays data")
        val upcomingHolidaysData = mutableListOf<JSONObject>()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 0 until events.length()) {
            val holidayData = events.getJSONObject(i)
            val holidayDate = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(holidayData.getString("date"))
            val holidayYear = holidayDate.year + 1900
            if (holidayDate != null) {
                if (holidayYear == currentYear && holidayDate > Calendar.getInstance().time) {
                    upcomingHolidaysData.add(holidayData)
                }
            }
        }
        return upcomingHolidaysData
    }

    /**
     * Populate the view displaying the next upcoming holiday
     */
    private fun populateNextHolidayView(nextHolidayData: JSONObject?) {
        Log.d(TAG, "Populating next holiday view")
        nextHolidayTitle.text = "Next Holiday (Campus is Closed)"
        if (nextHolidayData != null) {
            nextHolidayDetails.text = Html.fromHtml(
                "<big><b>${nextHolidayData.getString("date")}</b></big><br><br>${
                    nextHolidayData.getString("title")
                }"
            )
        } else {
            nextHolidayDetails.text = "No upcoming holidays for this year found."
        }
    }

    /**
     * Populate the view displaying the list of upcoming holidays
     */
    private fun populateUpcomingHolidaysView(upcomingHolidaysData: List<JSONObject>) {
        Log.d(TAG, "Populating upcoming holidays view")
        upcomingHolidaysTitle.text = HtmlCompat.fromHtml(
            activity.getString(R.string.upcoming_holidays_this_year),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        val upcomingHolidays = upcomingHolidaysData.map {
            val holidayDate = it.getString("date")
            val holidayTitle = it.getString("title")
            "$holidayDate - $holidayTitle"
        }

        // user an adapter to map the data to the List
        upcomingHolidaysList.adapter =
            ArrayAdapter(activity, android.R.layout.simple_list_item_1, upcomingHolidays)
    }
}