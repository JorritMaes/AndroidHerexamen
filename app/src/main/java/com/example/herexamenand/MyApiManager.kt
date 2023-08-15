package com.example.herexamenand

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.herexamenand.data.entities.Attendee
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef
import kotlinx.coroutines.*
import org.json.JSONObject

class MyApiManager(val context: Context) {

    suspend fun makeAPIGetCall(urlSuffix: String){
        GlobalScope.launch(Dispatchers.IO) {
            var res: String = " "
            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.11.55:8080/${urlSuffix}"

            val stringRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val updatedUser = apiToUser(response.toString())
                    GlobalScope.launch(Dispatchers.IO) {
                        MyApplication.database.UserDao().insert(updatedUser)
                    }
                },
                {})

            queue.add(stringRequest)
        }
    }

    suspend fun makeApiPostUserCall(user: User){

        GlobalScope.launch(Dispatchers.IO) {
            val userWithFriends = MyApplication.database.UserDao().getFriends(user.userId)
            val jsonObject = user.toJsonObject(userWithFriends)


            var res: String = " "
            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.11.55:8080/create/user"

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                {},
                {})

            queue.add(stringRequest)
        }
    }

    private fun apiToUser(userJson: String): User {
        val jsonObject = JSONObject(userJson)
        val id = jsonObject.getLong("id")
        val username = jsonObject.getString("username")
        val friendListJson = jsonObject.getJSONArray("friendList")


        GlobalScope.launch(Dispatchers.IO){
            MyApplication.database.FriendsCrossRefDao().clearUsersFriends(id)
            if(friendListJson.length() >0) {
                for (i in 0 until friendListJson.length()) {
                    val friendId = friendListJson.getJSONObject(i).getLong("id")
                    MyApplication.database.FriendsCrossRefDao()
                        .insert(FriendsCrossRef(id, friendId))
                    MyApplication.database.FriendsCrossRefDao()
                        .insert(FriendsCrossRef(friendId, id))
                }
            }
        }



        return User(id, username)
    }

    suspend fun makeApiPostInviteCall(invite: Invite): Long {
        return withContext(Dispatchers.IO) {
            val userWithFriends = MyApplication.database.UserDao().getFriends(invite.userId)
            val jsonObject = invite.toJsonObject(userWithFriends)

            val deferredId = CompletableDeferred<Long>()
            var res: String = " "
            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.11.55:8080/create/invite"

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                {response ->
                    deferredId.complete(response.getLong("id"))},
                {error ->
                    deferredId.complete(0)
                })

            queue.add(stringRequest)
            deferredId.await()
        }
    }

    suspend fun makeApiPostEventCall(event: Event): Long{
        return withContext(Dispatchers.IO) {
            val userWithFriends = MyApplication.database.UserDao().getFriends(event.userId)
            val jsonObject = event.toJsonObject(userWithFriends)

            val deferredId = CompletableDeferred<Long>()
            var res: String = " "
            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.11.55:8080/create/event"

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                {response ->
                    deferredId.complete(response.getLong("id"))
                },
                {error ->
                    deferredId.complete(0)
                })

            queue.add(stringRequest)
            deferredId.await()
        }
    }

    suspend fun makeApiPostAttendeeCall(attendee: Attendee): Long {
        return withContext(Dispatchers.IO) {
            val userWithFriends = MyApplication.database.UserDao().getFriends(attendee.userId)
            val event = MyApplication.database.EventDao().find(attendee.eventId)
            val hostWithFriends = MyApplication.database.UserDao().getFriends(event.userId)
            val jsonObject = attendee.toJsonObject(event, userWithFriends, hostWithFriends)

            val deferredId = CompletableDeferred<Long>()
            var res: String = " "
            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.11.55:8080/create/attendee"

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                {response ->
                    deferredId.complete(response.getLong("id"))},
                {error ->
                    deferredId.complete(0)
                })

            queue.add(stringRequest)
            deferredId.await()
        }
    }
}