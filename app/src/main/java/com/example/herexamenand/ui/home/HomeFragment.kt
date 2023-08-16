package com.example.herexamenand.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.relations.entities.AttendeeWithUserAndEvent
import com.example.herexamenand.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: EventItemAdapter
    private lateinit var calendarView: CalendarView
    private var eventList = emptyList<AttendeeWithUserAndEvent>()
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        calendarView = root.findViewById(R.id.calendar_view)
        calendarView.date = homeViewModel.selectedDate.value!!

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= EventItemAdapter(emptyList())
        val eventOverview : RecyclerView = view.findViewById(R.id.event_overview)
        eventOverview.layoutManager = LinearLayoutManager(requireContext())
        eventOverview.adapter = adapter

        GlobalScope.launch(Dispatchers.IO){
            val allAttendees = MyApplication.database.AttendeeDao().getAllEntities()
            while (eventList.isEmpty()){
                eventList = MyApplication.database.AttendeeDao().getMyAttendeesWithEventAndUser(MyApplication.currentUser.userId)
            }
            setEventList(calendarView.date)
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val tempCalendar = Calendar.getInstance()
            tempCalendar.set(year, month, dayOfMonth)
            view.date = tempCalendar.timeInMillis
            setEventList(view.date)
        }


        parentFragmentManager.beginTransaction().detach(this).attach(this)
    }

    private fun setEventList(date: Long) {
        viewLifecycleOwner.lifecycleScope.launch{
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            adapter.setNewList(eventList.filter { e -> e.event.date == dateFormat.format(date) })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.selectedDate.value = calendarView.date
        _binding = null
    }
}