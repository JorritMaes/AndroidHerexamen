package com.example.herexamenand.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.databinding.FragmentDashboardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: InviteItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= InviteItemAdapter(emptyList())
        val inviteOverview: RecyclerView= view.findViewById(R.id.invite_overview)
        inviteOverview.layoutManager = LinearLayoutManager(requireContext())
        inviteOverview.adapter = adapter

        fetchAllInvites()
    }

    override fun onResume() {
        super.onResume()
        fetchAllInvites()
    }

    private fun fetchAllInvites() {
        viewLifecycleOwner.lifecycleScope.launch{
            val allInvitesList= MyApplication.database.InviteDao().getAllEntities()
            adapter.setNewList(allInvitesList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}