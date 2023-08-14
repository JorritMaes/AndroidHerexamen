package com.example.herexamenand.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.User

class FriendItemAdapter(private var friendList: List<User>) :
    RecyclerView.Adapter<FriendItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_list_item, parent, false)

        return FriendItemAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = friendList[position]

        holder.friendName.text = currentItem.username
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName : TextView = itemView.findViewById(R.id.friend_name)
    }

    fun setNewList(newList: List<User>){
        friendList = newList
        notifyDataSetChanged()
    }

}
