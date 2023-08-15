package com.example.herexamenand.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.User

class FriendItemAdapter( var friendList: List<User>, private var invitedUsersList: MutableLiveData<ArrayList<User>>) :
    RecyclerView.Adapter<FriendItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_list_item, parent, false)

        return FriendItemAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = friendList[position]

        holder.friendName.text = currentItem.username
        holder.invited.isChecked = false

        holder.invited.setOnClickListener{
            if (holder.invited.isChecked){
                invitedUsersList.value?.add(currentItem)
            }
            else{
                invitedUsersList.value?.remove(currentItem)
            }
        }


    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName : TextView = itemView.findViewById(R.id.friend_name)
        val invited: CheckBox = itemView.findViewById(R.id.invite_checkbox)
    }

    fun setNewList(newList: List<User>){
        friendList = newList
        this.
        notifyDataSetChanged()
    }

}
