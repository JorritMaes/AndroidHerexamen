package com.example.herexamenand.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.herexamenand.data.entities.User

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val invitedFriends = MutableLiveData<ArrayList<User>>().apply {
        value = ArrayList<User>()
    }
}