package com.example.bitfit_plus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.bitfit_plus.databinding.ActivityScrollingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding

    // material dialog popup
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView : View

    // fragment support
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val logFragment: Fragment = LogFragment()
    private val metricsFragment: Fragment = MetricsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityScrollingBinding.inflate(layoutInflater)
        val viewRoot = binding.root
        setContentView(viewRoot)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        // set up for middle fab button
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false

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

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.list_button -> fragment = logFragment
                R.id.metrics_button -> fragment = metricsFragment
            }
            replaceFragment(fragment)
            if (fragment == logFragment) {
                try {
                    supportFragmentManager.beginTransaction().remove(metricsFragment).commit()
                } catch (e: Exception) {
                    //
                }

            } else {
                try {
                    supportFragmentManager.beginTransaction().remove(logFragment).commit()
                } catch (e: Exception) {
                    //
                }
            }
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.list_button

        // Call helper method to swap the FrameLayout with the fragment
        replaceFragment(logFragment)
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_fragment_layout, fragment)
        fragmentTransaction.commit()
    }

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

                // with the input information, create a new CheckInEntity and add to the database
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
                    supportFragmentManager.beginTransaction().detach(logFragment).commit()
                    supportFragmentManager.beginTransaction().attach(logFragment).commit()
                }
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