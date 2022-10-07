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


//        checkInList.addAll(CheckInItemFetcher.getItems())

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = CheckInItemAdapter(checkInList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // load in database info
        lifecycleScope.launch {
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
                    checkInList.clear()
                    checkInList.addAll(mappedList)
                    checkInList.sortWith(compareBy<CheckIn> { it.date}.thenByDescending { it.timeOfDay })
                    adapter.notifyDataSetChanged()
                    // display or hide welcome message
                    welcomeNewUser = findViewById(R.id.welcomeNewUser)
                    if (!checkInList.isEmpty()) {
                        welcomeNewUser.visibility = View.INVISIBLE
                        welcomeNewUser.height = 0
                    }
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

        // testing out date picker
        val datePicker : DatePicker = customAlertDialogView.findViewById(R.id.editTextDate)
        var selectedDate = LocalDate.of(LocalDate.now().year, LocalDate.now().month, LocalDate.now().dayOfMonth)
        var today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
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
                val date = LocalDate.now()
                val weight = weightInput.text.toString().toDouble()
                val bodyFat = bodyFatInput.text.toString().toDouble()

                if (timeOfDayInt == R.id.morningRadio) {
                    timeOfDay = "Morning"
                } else {
                    timeOfDay = "Evening"
                }

//                val newCheckIn = CheckIn(timeOfDay, weight, bodyFat, "imageUrl", selectedDate.toString())
//
//                checkInList.add(newCheckIn)

                println("Printing List First $checkInList")

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

//                checkInList.add(CheckIn(timeOfDay, weight, bodyFat, "bodyImageUrl", selectedDate.toString()))

                checkInList.sortWith(compareBy<CheckIn> { it.date}.thenBy { it.timeOfDay })

                adapter.notifyDataSetChanged()

//                lifecycleScope.launch {
//                    println("Printing List Second $checkInList")
//                    checkInList.clear()
//                    adapter.notifyDataSetChanged()
//                    println("Printing List Third $checkInList")
//                    (application as MyApp).db.checkInDao().getAll().collect { databaseList ->
//                        databaseList.map { entity ->
//                            CheckIn(
//                                entity.timeOfDay,
//                                entity.weight,
//                                entity.bodyFat,
//                                entity.bodyImage,
//                                entity.date
//                            )
//                        }.also { mappedList ->
//                            checkInList.clear()
//                            println("Printing List Fourth $checkInList")
//                            println("Printing Mapped List $mappedList")
//                            checkInList.addAll(mappedList)
//                            println("Printing List Fifth $checkInList")
//                            adapter.notifyDataSetChanged()
//                        }
//                    }
//                }




//                Toast.makeText(this, "Added $weight and $timeOfDay for $selectedDate", Toast.LENGTH_LONG).show()

//                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("Ortiz's Error", e.toString())
                Toast.makeText(this, "Sorry, there was an error adding that item.", Toast.LENGTH_SHORT).show()
            }
        }

        addDialog.show()
    }
}