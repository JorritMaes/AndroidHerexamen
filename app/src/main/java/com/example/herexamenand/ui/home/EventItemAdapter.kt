package com.example.herexamenand.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.Attendee
import com.example.herexamenand.data.entities.relations.entities.EventWithAttendees
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventItemAdapter(private var eventList: List<EventWithAttendees>) :
    RecyclerView.Adapter<EventItemAdapter.ViewHolder>() {
    private val userDao = MyApplication.database.UserDao()
    private val attendeeDao = MyApplication.database.AttendeeDao()
    private lateinit var attendeeString: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_list_item, parent, false)

        attendeeString = parent.context.getString(R.string.attendees)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = eventList[position]
        GlobalScope.launch(Dispatchers.Main){
            holder.host.text = userDao.find(currentItem.event.userId).username

        }

        holder.name.text = currentItem.event.name
        holder.date.text = currentItem.event.date
        holder.times.text = currentItem.event.times
        holder.attendeeCount.text = currentItem.attendeeList.size.toString().plus(" ").plus(attendeeString)


    }

    fun setNewList(newList: List<EventWithAttendees>){
        eventList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val host: TextView = itemView.findViewById(R.id.event_host)
        val name : TextView = itemView.findViewById(R.id.event_name)
        val date: TextView = itemView.findViewById(R.id.event_date)
        val times : TextView = itemView.findViewById(R.id.event_times)
        val attendeeCount: TextView = itemView.findViewById(R.id.event_attendee_count)
    }
}
