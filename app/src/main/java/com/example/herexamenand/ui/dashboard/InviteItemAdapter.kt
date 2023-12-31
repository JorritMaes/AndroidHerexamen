package com.example.herexamenand.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.MyApiManager
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InviteItemAdapter(private var inviteList: List<Invite>) :
    RecyclerView.Adapter<InviteItemAdapter.ViewHolder>() {
    private val userDao = MyApplication.database.UserDao()
    private val eventDao = MyApplication.database.EventDao()
    private val attendeeDao = MyApplication.database.AttendeeDao()
    private val inviteDao = MyApplication.database.InviteDao()
    private lateinit var user: User
    private lateinit var apiManager: MyApiManager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invite_list_item, parent, false)
        apiManager = MyApiManager(parent.context)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = inviteList[position]
        GlobalScope.launch(Dispatchers.Main){
            user = MyApplication.currentUser
            holder.host.text = user.username

        }
        holder.name.text = currentItem.name
        holder.date.text = currentItem.date
        holder.times.text = currentItem.times
        holder.acceptButton.setOnClickListener{
            acceptInvite(currentItem, position)
            inviteList = inviteList.filter { e -> currentItem.inviteId != e.inviteId }
            notifyItemRemoved(position)
        }
    }

    fun setNewList(newList: List<Invite>){
        inviteList = newList
        notifyDataSetChanged()
    }

    fun acceptInvite(currentInvite: Invite, position: Int){
        GlobalScope.launch(Dispatchers.Main){
            val attendee  = Attendee(0,user.userId, currentInvite.eventId, Presence.CONFIRMED)
            val attendeeId = apiManager.makeApiPostAttendeeCall(attendee)
            attendee.attendeeId
            attendeeDao.insert(attendee)
            apiManager.makeApiDeleteInviteCall(currentInvite)
            inviteDao.remove(currentInvite)


        }
    }

    override fun getItemCount(): Int {
        return inviteList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val host: TextView = itemView.findViewById(R.id.event_host)
        val name : TextView = itemView.findViewById(R.id.event_name)
        val date: TextView = itemView.findViewById(R.id.event_date)
        val times : TextView = itemView.findViewById(R.id.event_times)
        val acceptButton: Button = itemView.findViewById(R.id.button_accept_invite)
    }
}