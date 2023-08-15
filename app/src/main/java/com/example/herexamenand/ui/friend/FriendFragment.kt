package com.example.herexamenand.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herexamenand.MyApiManager
import com.example.herexamenand.MyApplication
import com.example.herexamenand.R
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef
import com.example.herexamenand.databinding.FragmentNewFriendBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendFragment: Fragment()  {
    private var _binding: FragmentNewFriendBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: FriendManageItemAdapter
    private lateinit var addFriendButton: Button
    private lateinit var friendName: EditText
    private lateinit var noUserMessage: TextView

    private val userDao = MyApplication.database.UserDao()
    private val friendsCrossRefDao = MyApplication.database.FriendsCrossRefDao()
    private lateinit var apiManager: MyApiManager



    override fun onCreateView(
       inflater: LayoutInflater,
       container: ViewGroup?,
       savedInstanceState: Bundle?
    ): View {
        apiManager = MyApiManager(requireContext())

        _binding = FragmentNewFriendBinding.inflate(inflater, container, false)
        val root = binding.root

        friendName = root.findViewById(R.id.edit_text_friend_name)
        noUserMessage = root.findViewById(R.id.text_view_no_user)
        noUserMessage.isVisible = false

        addFriendButton = root.findViewById(R.id.button_add_new_friend)
        addFriendButton.setOnClickListener() {
            GlobalScope.launch(Dispatchers.Main){
                val friend: User = userDao.findByName(friendName.text.toString())
                if (friend != null){
                    GlobalScope.launch(Dispatchers.IO){
                        friendsCrossRefDao.insert(FriendsCrossRef(friend.userId, 1))
                        friendsCrossRefDao.insert(FriendsCrossRef(1, friend.userId))
                        apiManager.makeApiPostUserCall(MyApplication.currentUser)
                        // get him agan now with new friendlist
                        apiManager.makeApiPostUserCall(MyApplication.database.UserDao().find(friend.userId))
                        fetchFriends()
                    }
                    noUserMessage.isVisible = false
                }
                else {
                    noUserMessage.isVisible = true
                }

            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= FriendManageItemAdapter(emptyList())
        val eventOverview : RecyclerView = view.findViewById(R.id.add_friend_overview)
        eventOverview.layoutManager = LinearLayoutManager(requireContext())
        eventOverview.adapter = adapter

        fetchFriends()

    }

    fun fetchFriends(){
        viewLifecycleOwner.lifecycleScope.launch{
            val user = MyApplication.database.UserDao().getFriends(1)
            adapter.setNewList(user.friendList)

        }
    }

    override fun onDestroyView() {
       super.onDestroyView()
       _binding = null
    }
}