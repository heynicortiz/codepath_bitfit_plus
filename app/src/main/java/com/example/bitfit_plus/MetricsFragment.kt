package com.example.bitfit_plus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MetricsFragment : Fragment() {

    private val checkInList = arrayListOf<CheckIn>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Change this statement to store the view in a variable instead of a return statement
        val view = inflater.inflate(R.layout.fragment_metrics, container, false)

        // Add these configurations for the recyclerView and to configure the adapter
        val layoutManager = LinearLayoutManager(context)


        // user metrics table
        val userMetricsLinearLayout = view.findViewById<LinearLayout>(R.id.userMetricsLinearLayout)
        val firstCheckInDisplay = view.findViewById<TextView>(R.id.firstCheckInDisplay)
        val latestCheckInDisplay = view.findViewById<TextView>(R.id.latestCheckInDisplay)
        val firstWeightDisplay = view.findViewById<TextView>(R.id.firstWeightDisplay)
        val latestWeightDisplay = view.findViewById<TextView>(R.id.latestWeightDisplay)
        val averageWeightDisplay = view.findViewById<TextView>(R.id.averageWeightDisplay)
        val firstBodyFatDisplay = view.findViewById<TextView>(R.id.firstBodyFatDisplay)
        val latestBodyFatDisplay = view.findViewById<TextView>(R.id.latestBodyFatDisplay)
        val averageBodyFatDisplay = view.findViewById<TextView>(R.id.averageBodyFatDisplay)


        // load in database info
        lifecycleScope.launch(Dispatchers.Main) {
            (activity?.application as MyApp).db.checkInDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    CheckIn(
                        entity.timeOfDay,
                        entity.weight,
                        entity.bodyFat,
                        entity.bodyImage,
                        entity.date
                    )
                }.also { mappedList ->
                    checkInList.addAll(mappedList)
                    println("The list is now: $checkInList")
                    // sort the list by date then Morning before Evening
                    checkInList.sortWith(compareBy<CheckIn> { it.date}.thenByDescending { it.timeOfDay })

                    if (!checkInList.isEmpty()) {
                        userMetricsLinearLayout.visibility = View.VISIBLE
                        val firstCheckIn = checkInList[0]
                        val latestCheckIn = checkInList[checkInList.size -1]
                        firstCheckInDisplay.text = firstCheckIn.date.toString()
                        latestCheckInDisplay.text = latestCheckIn.date.toString()
                        firstWeightDisplay.text = firstCheckIn.weight.toString()
                        latestWeightDisplay.text = latestCheckIn.weight.toString()
                        firstBodyFatDisplay.text = firstCheckIn.bodyFat.toString()
                        latestBodyFatDisplay.text = latestCheckIn.bodyFat.toString()

                        var totalWeight : Double = 0.0
                        var totalBodyFat : Double = 0.0
                        var checkInCount = 0
                        checkInList.forEach {
                            totalWeight += it.weight!!
                            totalBodyFat += it.bodyFat!!
                            checkInCount += 1
                        }

                        averageWeightDisplay.text = String.format("%.2f", ( totalWeight / checkInCount))
                        averageBodyFatDisplay.text = String.format("%.2f", ( totalBodyFat / checkInCount))

                    }
                }
            }
        }

        return view
    }

    companion object {
        fun newInstance(): MetricsFragment {
            return MetricsFragment()
        }
    }
}