package com.example.herexamenand.ui.notifications

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.Invite
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

    private lateinit var add_friend_button: Button



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        selectDateButton = root.findViewById(R.id.edit_date_event_date)
        selectDateButton.setOnClickListener{
            onDatePickerClick()
        }

        selectStartTime = root.findViewById(R.id.button_event_time_start)
        selectStartTime.setOnClickListener {
            onTimePickerClick(selectStartTime)
        }
        selectEndTime = root.findViewById(R.id.button_event_time_end)
        selectEndTime.setOnClickListener {
            onTimePickerClick(selectEndTime)
        }
        createInvite = root.findViewById(R.id.button_create_event)
        createInvite.setOnClickListener {
            createInvite()
        }

        inputName = root.findViewById(R.id.edit_text_event_name)

        add_friend_button = root.findViewById(R.id.button_go_to_add_friend)
        add_friend_button.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_notifications_to_navigation_new_friend)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= FriendItemAdapter(emptyList())
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
        val invite : Invite = Invite(
            0,
            selectDateButton.text.toString(),
            selectStartTime.text.toString().plus(" - ").plus(selectEndTime.text.toString()),
            inputName.text.toString(),
            1
        )

        val inviteDao = MyApplication.database.InviteDao()
        GlobalScope.launch(Dispatchers.IO) {
            inviteDao.insert(invite)

        }

        clearInputs()

    }

    private fun clearInputs() {
        selectDateButton.text = ""
        selectStartTime.text = ""
        selectEndTime.text = ""
        inputName.text.clear()
    }

    private fun onTimePickerClick(selector: Button) {
        val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            // Update the calendar with the selected time
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            // Format the selected time and display it
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedTime = timeFormat.format(calendar.time)
            selector.text = selectedTime
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
            selectDateButton.text = selectedDate
        }

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), dateListener, initialYear, initialMonth, initialDay)
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
