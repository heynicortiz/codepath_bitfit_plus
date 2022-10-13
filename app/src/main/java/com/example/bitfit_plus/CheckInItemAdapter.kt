package com.example.bitfit_plus

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private lateinit var rvRow: ConstraintLayout
private lateinit var  checkInDate: TextView
private lateinit var  checkInSession: ImageView
private lateinit var  checkInWeight: TextView
private lateinit var  checkInBodyFat: TextView

class CheckInItemAdapter(private val checkInItems: MutableList<CheckIn>): RecyclerView.Adapter<CheckInItemAdapter.ViewHolder>() {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // TODO: Create member variables for any view that will be set
        // as you render a row.

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each sub-view
        init {
            // TODO: Store each of the layout's views into
            // the public final member variables created above
            rvRow = itemView.findViewById(R.id.recycler_view_row)
            checkInDate = itemView.findViewById(R.id.check_in_date)
            checkInSession = itemView.findViewById(R.id.sessionImage)
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

        val dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy")

        val date = LocalDate.parse(checkInItem.date)

        checkInDate.text = date.format(dtf)
        if (checkInItem.timeOfDay.equals("Morning")) {
            checkInSession.setImageResource(R.drawable.ic_baseline_wb_sunny_24)
            checkInSession.setColorFilter(Color.parseColor("#ffde37"))
        } else {
            checkInSession.setImageResource(R.drawable.ic_baseline_nights_stay_24)
            checkInSession.setColorFilter(Color.parseColor("#357edd"))
        }
        checkInWeight.text = checkInItem.weight.toString() + " lbs, "
        checkInBodyFat.text = checkInItem.bodyFat.toString() + "% body fat"

        rvRow.setOnClickListener {
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