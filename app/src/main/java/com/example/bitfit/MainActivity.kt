package com.example.bitfit

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.bitfit.databinding.ActivityScrollingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.Call
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding
    private val checkInList = arrayListOf<CheckIn>()

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView : View

    private lateinit var welcomeNewUser: TextView

    private lateinit var adapter : CheckInItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        // fab button
        binding.fab.setOnClickListener {
            // Inflate Custom alert dialog view
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.add_check_in_dialog, null, false)

            // call custom addItem function/dialog
            addCheckIn()
        }

        // dialog creator
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)

        // user metrics table
        val userMetricsLinearLayout = findViewById<LinearLayout>(R.id.userMetricsLinearLayout)
        val firstCheckInDisplay = findViewById<TextView>(R.id.firstCheckInDisplay)
        val latestCheckInDisplay = findViewById<TextView>(R.id.latestCheckInDisplay)
        val firstWeightDisplay = findViewById<TextView>(R.id.firstWeightDisplay)
        val latestWeightDisplay = findViewById<TextView>(R.id.latestWeightDisplay)
        val averageWeightDisplay = findViewById<TextView>(R.id.averageWeightDisplay)
        val firstBodyFatDisplay = findViewById<TextView>(R.id.firstBodyFatDisplay)
        val latestBodyFatDisplay = findViewById<TextView>(R.id.latestBodyFatDisplay)
        val averageBodyFatDisplay = findViewById<TextView>(R.id.averageBodyFatDisplay)


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = CheckInItemAdapter(checkInList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // load in database info
        lifecycleScope.launch(Main) {
            (application as MyApp).db.checkInDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    CheckIn(
                        entity.timeOfDay,
                        entity.weight,
                        entity.bodyFat,
                        entity.bodyImage,
                        entity.date
                    )
                }.also { mappedList ->
                    println("The list was: $checkInList")
                    checkInList.clear()
                    println("HEY THE LIST GOT CLEARED AGAIN??")
                    checkInList.addAll(mappedList)
                    println("The list is now: $checkInList")
                    // display or hide welcome message
                    welcomeNewUser = findViewById(R.id.welcomeNewUser)
                    if (!checkInList.isEmpty()) {
                        welcomeNewUser.visibility = View.GONE
                        userMetricsLinearLayout.visibility = View.VISIBLE
                    } else {
//                        userMetricsLinearLayout.visibility = View.GONE
                    }
                    // sort the list by date then Morning before Evening
                    checkInList.sortWith(compareBy<CheckIn> { it.date}.thenByDescending { it.timeOfDay })
                    adapter.notifyDataSetChanged()
                }
            }
        }


    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_scrolling, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun addCheckIn() {
        val timeOfDayGroup : RadioGroup = customAlertDialogView.findViewById(R.id.timeOfDayGroup)
        val morningRadio : RadioButton = customAlertDialogView.findViewById(R.id.morningRadio)
        val eveningRadio : RadioButton = customAlertDialogView.findViewById(R.id.eveningRadio)
        val weightInput : EditText = customAlertDialogView.findViewById(R.id.weightInput)
        val bodyFatInput : EditText = customAlertDialogView.findViewById(R.id.bodyFatInput)
        val selectImage : Button = customAlertDialogView.findViewById(R.id.selectImage)
        val bodyImageInput : EditText = customAlertDialogView.findViewById(R.id.bodyImageInput)

        val addDialog = MaterialAlertDialogBuilder(customAlertDialogView.context)
        addDialog.setView(customAlertDialogView)
        addDialog.setTitle("Add a Check-In")

        // date picker initialization to today
        val datePicker : DatePicker = customAlertDialogView.findViewById(R.id.editTextDate)
        val now = LocalDate.now()
        var selectedDate = LocalDate.of(now.year, now.month, now.dayOfMonth)
        datePicker.init(now.year, now.monthValue - 1, now.dayOfMonth)
        { view, year, month, day ->
            val month = month + 1
            selectedDate = LocalDate.of(year, month, day)
        }

        // Respond to negative button press
        addDialog.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this, "Cancel toast", Toast.LENGTH_SHORT).show()
        }

        // Respond to positive button press
        addDialog.setPositiveButton("Add") { dialog, which ->
            try {
                val timeOfDayInt = timeOfDayGroup.checkedRadioButtonId
                var timeOfDay : String
                val weight = weightInput.text.toString().toDouble()
                val bodyFat = bodyFatInput.text.toString().toDouble()

                if (timeOfDayInt == R.id.morningRadio) {
                    timeOfDay = "Morning"
                } else {
                    timeOfDay = "Evening"
                }

                // with the input informaiton, create a new CheckInEntity and add to the database
                lifecycleScope.launch(IO) {
                    (application as MyApp).db.checkInDao().insert(
                        CheckInEntity(
                            timeOfDay = timeOfDay,
                            weight = weight,
                            bodyFat = bodyFat,
                            bodyImage = "bodyImageUrl",
                            date = selectedDate.toString()
                        )
                    )
                }
//
//                checkInList.sortWith(compareBy<CheckIn> { it.date}.thenBy { it.timeOfDay })
//                println("Printing List Third $checkInList")
//                adapter.notifyDataSetChanged()
//                println("Printing List Fourth $checkInList")
            } catch (e: Exception) {
                // for any exceptions here, just display a generic error
                Log.e("BitFit's Error", e.toString())
                Toast.makeText(this, "Sorry, there was an error adding that item.", Toast.LENGTH_SHORT).show()
            }

        }

        // finally, show the dialog for the user input
        addDialog.show()
    }
}