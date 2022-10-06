package com.example.bitfit

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CheckInItemAdapter(private val checkInItems: MutableList<CheckIn>): RecyclerView.Adapter<CheckInItemAdapter.ViewHolder>() {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // TODO: Create member variables for any view that will be set
        // as you render a row.
        val rvRow: ConstraintLayout
        val checkInDate: TextView
        val checkInSession: TextView
        val checkInWeight: TextView
        val checkInBodyFat: TextView

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each sub-view
        init {
            // TODO: Store each of the layout's views into
            // the public final member variables created above
            rvRow = itemView.findViewById(R.id.recycler_view_row)
            checkInDate = itemView.findViewById(R.id.check_in_date)
            checkInSession = itemView.findViewById(R.id.check_in_session)
            checkInWeight = itemView.findViewById(R.id.check_in_weight)
            checkInBodyFat = itemView.findViewById(R.id.check_in_body_fat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val  contactView = inflater.inflate(R.layout.check_in_item, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkInItem = checkInItems.get(position)

        val dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy")

        val date = LocalDateTime.parse(checkInItem.date)

        holder.checkInDate.text = date.format(dtf)
        holder.checkInSession.text = checkInItem.timeOfDay
        holder.checkInWeight.text = checkInItem.weight.toString() + " lbs"
        holder.checkInBodyFat.text = checkInItem.bodyFat.toString() + "%"

        holder.rvRow.setOnClickListener {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(checkInItem.bodyImage))
                ContextCompat.startActivity(it.context, browserIntent, null)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(it.context, "Invalid URL for " + checkInItem.date, Toast.LENGTH_LONG).show()
            }
        }

//        holder.rvRow.setOnLongClickListener{
//            try {
//                Log.v("NICS LOG - Original:", checkInItems.size.toString() + " - dropping: " + position)
//                Toast.makeText(it.context, "${checkInItem.name} removed", Toast.LENGTH_SHORT).show()
//                checkInItems.removeAt(position)
//                Log.v("NICS LOG - After:", checkInItems.size.toString())
//                notifyDataSetChanged()
//            } catch (e: ActivityNotFoundException) {
//                Toast.makeText(it.context, "Error deleting", Toast.LENGTH_SHORT).show()
//            }
//            true
//        }
    }

    override fun getItemCount(): Int {
        return checkInItems.size
    }
}