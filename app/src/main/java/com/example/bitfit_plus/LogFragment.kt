package com.example.bitfit_plus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class LogFragment : Fragment() {

    private lateinit var adapter : CheckInItemAdapter
    private val checkInList = arrayListOf<CheckIn>()
    private lateinit var welcomeNewUser : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // load in database info
        lifecycleScope.launch(Main) {
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
                    println("The list was: $checkInList")
                    checkInList.clear()
                    adapter.notifyDataSetChanged()
                    println("HEY THE LIST GOT CLEARED AGAIN??")
                    checkInList.addAll(mappedList)
                    println("The list is now: $checkInList")
                    // sort the list by date then Morning before Evening
                    checkInList.sortWith(compareBy<CheckIn> { it.date}.thenByDescending { it.timeOfDay })
                    adapter.notifyDataSetChanged()

                    if (checkInList.isNotEmpty()) {
                        welcomeNewUser = view?.findViewById(R.id.welcomeNewUser)!!
                        welcomeNewUser.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Change this statement to store the view in a variable instead of a return statement
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        // Add these configurations for the recyclerView and to configure the adapter
        val layoutManager = LinearLayoutManager(context)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        adapter = CheckInItemAdapter(checkInList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        return view
    }

    companion object {
        fun newInstance(): LogFragment {
            return LogFragment()
        }
    }
}