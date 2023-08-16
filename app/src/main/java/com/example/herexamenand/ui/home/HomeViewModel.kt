package com.example.herexamenand.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.herexamenand.data.entities.User
import java.sql.Date
import java.util.*

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val selectedDate = MutableLiveData<Long>().apply {
        val tempCalendar = Calendar.getInstance()
        value = tempCalendar.timeInMillis
    }
}