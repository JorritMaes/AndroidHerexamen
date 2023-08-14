package com.example.herexamenand.ui.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.data.daos.FriendsCrossRefDao
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendManageItemAdapter(private var userList: List<User>) :
    RecyclerView.Adapter<FriendManageItemAdapter.ViewHolder>() {
    private val userDao = MyApplication.database.UserDao()
    private val friendsCrossRefDao = MyApplication.database.FriendsCrossRefDao()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.base_friend_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.friend_name.text = currentItem.username
        holder.remove_friend.setOnClickListener {
            removeFriend(currentItem.userId)
            userList = userList.filter { e -> e.userId != currentItem.userId  }
            notifyItemRemoved(position)
        }
    }

    private fun removeFriend(userId: Long) {
        GlobalScope.launch(Dispatchers.IO){
            friendsCrossRefDao.delete(FriendsCrossRef(userId,1))
            friendsCrossRefDao.delete(FriendsCrossRef(1,userId))
        }
    }

    fun setNewList(newList: List<User>){
        userList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friend_name: TextView = itemView.findViewById(R.id.overview_item_friend_name)
        val remove_friend : Button = itemView.findViewById(R.id.friend_overview_remove_button)
    }

}