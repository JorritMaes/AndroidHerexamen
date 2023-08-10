package com.example.herexamenand.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.Invite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InviteItemAdapter(private var inviteList: List<Invite>) :
    RecyclerView.Adapter<InviteItemAdapter.ViewHolder>() {
    private val userDao = MyApplication.database.UserDao()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invite_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = inviteList[position]
        GlobalScope.launch(Dispatchers.Main){
            holder.host.text = userDao.findByName("testuser").username
        }
        holder.name.text = currentItem.name
        holder.date.text = currentItem.date
        holder.times.text = currentItem.times

    }

    fun setNewList(newList: List<Invite>){
        inviteList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return inviteList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val host: TextView = itemView.findViewById(R.id.invite_host)
        val name : TextView = itemView.findViewById(R.id.invite_name)
        val date: TextView = itemView.findViewById(R.id.invite_date)
        val times : TextView = itemView.findViewById(R.id.invite_times)    }
}