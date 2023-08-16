package com.example.herexamenand.ui.notifications

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.herexamenand.MyApiManager
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.*
import com.example.herexamenand.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: FriendItemAdapter


    private lateinit var selectDateButton: Button
    private val calendar = Calendar.getInstance()

    private lateinit var selectStartTime: Button
    private lateinit var selectEndTime: Button
    private lateinit var createInvite: Button

    private lateinit var inputName: EditText
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private lateinit var invalidDate: TextView
    private lateinit var invalidStartTime: TextView
    private lateinit var invalidEndTime: TextView

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var apiManager: MyApiManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        apiManager = MyApiManager(requireContext())

        selectDateButton = root.findViewById(R.id.edit_date_event_date)
        selectDateButton.text = notificationsViewModel.eventDate.value
        selectDateButton.setOnClickListener{
            onDatePickerClick()
        }

        selectStartTime = root.findViewById(R.id.button_event_time_start)
        selectStartTime.text = notificationsViewModel.startTime.value
        selectStartTime.setOnClickListener {
            onTimePickerClick(selectStartTime)
        }
        selectEndTime = root.findViewById(R.id.button_event_time_end)
        selectEndTime.text = notificationsViewModel.endTime.value
        selectEndTime.setOnClickListener {
            onTimePickerClick(selectEndTime)
        }
        createInvite = root.findViewById(R.id.button_create_event)
        createInvite.setOnClickListener {
            createInvite()
        }

        invalidDate = root.findViewById(R.id.text_view_invalid_date_message)
        invalidStartTime = root.findViewById(R.id.text_view_invalid_start_time_message)
        invalidEndTime = root.findViewById(R.id.text_view_invalid_end_time_message)

        inputName = root.findViewById(R.id.edit_text_event_name)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= FriendItemAdapter(emptyList(), notificationsViewModel.invitedFriends)
        val friendOverview : RecyclerView = view.findViewById(R.id.friend_overview)
        friendOverview.layoutManager = LinearLayoutManager(requireContext())
        friendOverview.adapter = adapter

        fetchFriends()
    }

    private fun fetchFriends() {
        viewLifecycleOwner.lifecycleScope.launch{
            val user = MyApplication.database.UserDao().getFriends(1)
            adapter.setNewList(user.friendList)

        }
    }

    private fun createInvite() {
        GlobalScope.launch(Dispatchers.IO){
            //Create an event and attendee for the current user
            val event : Event = Event(
                0,
                selectDateButton.text.toString(),
                selectStartTime.text.toString().plus(" - ").plus(selectEndTime.text.toString()),
                inputName.text.toString(),
                MyApplication.currentUser.userId
            )
            val eventId = apiManager.makeApiPostEventCall(event)
            event.eventId = eventId
            MyApplication.database.EventDao().insert(event)

            val attendee = Attendee(0, MyApplication.currentUser.userId, eventId, Presence.CONFIRMED)
            attendee.attendeeId = apiManager.makeApiPostAttendeeCall(attendee)
            MyApplication.database.AttendeeDao().insert(attendee)

            // For each invited friend create an invite
            notificationsViewModel.invitedFriends.value?.forEach {
                val invite : Invite = Invite(
                    0,
                    selectDateButton.text.toString(),
                    selectStartTime.text.toString().plus(" - ").plus(selectEndTime.text.toString()),
                    inputName.text.toString(),
                    it.userId,
                    eventId
                    )

                MyApplication.database.InviteDao().insert(invite)
                apiManager.makeApiPostInviteCall(invite)

            }

            clearInputs()
        }

    }

    private fun clearInputs() {
        GlobalScope.launch(Dispatchers.Main){
            selectDateButton.text = ""
            selectStartTime.text = ""
            selectEndTime.text = ""
            inputName.text.clear()
            //clear the checkboxes by reloading
            adapter.notifyDataSetChanged()
        }
    }

    private fun onTimePickerClick(selector: Button) {
        val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            // Update the calendar with the selected time
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            // Format the selected time and display it
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedTime = timeFormat.format(calendar.time)
            selector.text = selectedTime.toString()

            //Check if the start time comes after the end time
            checkTimes(selector, timeFormat)

        }

        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), timeListener, initialHour, initialMinute, true)
        timePickerDialog.show()
    }

    private fun onDatePickerClick() {
        val dateListener = DatePickerDialog.OnDateSetListener{_, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val selectedDate = dateFormat.format(calendar.time)
            if (!calendar.time.before(Calendar.getInstance().time)){
                selectDateButton.text = selectedDate
                invalidDate.isVisible = false

            }
            else{
                invalidDate.isVisible = true
            }

        }

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), dateListener, initialYear, initialMonth, initialDay)
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        notificationsViewModel.endTime.value = selectEndTime.text.toString()
        notificationsViewModel.startTime.value = selectStartTime.text.toString()
        notificationsViewModel.eventDate.value = selectDateButton.text.toString()
        notificationsViewModel.eventName.value = inputName.text.toString()
        _binding = null
    }

    fun checkTimes(timeselector: Button, timeFormat: SimpleDateFormat){
        if(timeselector.id == R.id.button_event_time_end && selectStartTime.text.toString().isNotBlank()) {
            if(timeFormat.parse(selectStartTime.text.toString())?.before(timeFormat.parse(timeselector.text.toString())) == false) {
                timeselector.text = " "
                invalidEndTime.isVisible = true
            }
            else {
                invalidEndTime.isVisible = false
            }
        }
        else if(timeselector.id == R.id.button_event_time_start && selectEndTime.text.toString().isNotBlank()) {
            if (timeFormat.parse(selectEndTime.text.toString())?.after(timeFormat.parse(timeselector.text.toString())) == false){
                timeselector.text = " "
                invalidStartTime.isVisible = true
            }
            else{
                invalidStartTime.isVisible = false

            }
        }

    }

}
