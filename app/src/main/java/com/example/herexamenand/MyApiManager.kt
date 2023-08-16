package com.example.herexamenand

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.herexamenand.data.entities.*
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class MyApiManager(val context: Context) {

    suspend fun getUser(userId: Long): User{
        return withContext(Dispatchers.Main){
            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.11.55:8080/user/${userId}"

            val user = CompletableDeferred<User>()
            val stringRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val updatedUser = apiToUser(response.toString())
                    GlobalScope.launch(Dispatchers.IO) {
                        MyApplication.database.UserDao().insert(updatedUser)
                    }
                    user.complete(updatedUser)
                },
                {})

            queue.add(stringRequest)
            user.await()
        }
    }

    fun syncUsers(){
        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.11.55:8080/user/all"

        val stringRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val remoteUsers = apiToUsers(response)
                GlobalScope.launch(Dispatchers.IO) {
                    remoteUsers.forEach { MyApplication.database.UserDao().insert(it) }
                    val allUsersInRoom = MyApplication.database.UserDao().getAllEntities()
                    val toBeDeletedUsers = allUsersInRoom.filter { user -> !remoteUsers.contains(user) }
                    toBeDeletedUsers.forEach { MyApplication.database.UserDao().remove(it) }
                }
            },
            {error ->
                val t = error
            })

        queue.add(stringRequest)

    }


    fun syncEvents(){
        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.11.55:8080/event/all"

        val stringRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val remoteEvents = apiToEvents(response)
                GlobalScope.launch(Dispatchers.IO) {
                    remoteEvents.forEach { MyApplication.database.EventDao().insert(it) }
                    val allEventsInRoom = MyApplication.database.EventDao().getAllEntities()
                    val toBeDeletedEvents = allEventsInRoom.filter { event -> !remoteEvents.contains(event) }
                    toBeDeletedEvents.forEach { MyApplication.database.EventDao().remove(it) }
                }
            },
            {})

        queue.add(stringRequest)

    }

    fun syncInvites(){
        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.11.55:8080/invite/all"

        val stringRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val remoteInvites = apiToInvites(response)
                GlobalScope.launch(Dispatchers.IO) {
                    remoteInvites.forEach { MyApplication.database.InviteDao().insert(it) }
                    val allInvitesInRoom = MyApplication.database.InviteDao().getAllEntities()
                    val toBeDeletedInvites = allInvitesInRoom.filter { invite -> !remoteInvites.contains(invite) }
                    toBeDeletedInvites.forEach { MyApplication.database.InviteDao().remove(it) }
                }
            },
            {})

        queue.add(stringRequest)

    }

    fun syncAttendees(){

        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.11.55:8080/attendee/all"

        val deferredId = CompletableDeferred<JSONObject>()
        val stringRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val remoteAttendees = apiToAttendees(response)
                GlobalScope.launch(Dispatchers.IO) {
                    remoteAttendees.forEach { MyApplication.database.AttendeeDao().insert(it) }
                    val allAttendeesInRoom = MyApplication.database.AttendeeDao().getAllEntities()
                    val toBeDeletedAttendees = allAttendeesInRoom.filter { attendee -> !remoteAttendees.contains(attendee) }
                    toBeDeletedAttendees.forEach { MyApplication.database.AttendeeDao().remove(it) }
                }
            },
            {})

        queue.add(stringRequest)

    }

    private fun apiToUsers(response: JSONArray): List<User> {
        val users = ArrayList<User>()
        for (i in 0 until response.length()) {
            users.add(apiToUser(response.getJSONObject(i).toString()))
        }
        return users.toList()
    }

    private fun apiToEvents(response: JSONArray): List<Event> {
        val events = ArrayList<Event>()
        for (i in 0 until response.length()) {
            val id = response.getJSONObject(i).getLong("id")
            val date = response.getJSONObject(i).getString("date")
            val times = response.getJSONObject(i).getString("times")
            val name = response.getJSONObject(i).getString("name")
            val userJson = response.getJSONObject(i).getJSONObject("user")
            val userId = userJson.getLong("id")
            events.add(Event(id,date,times,name,userId))
        }
        return events.toList()

    }

    private fun apiToInvites(response: JSONArray): List<Invite> {
        val invites = ArrayList<Invite>()
        for (i in 0 until response.length()) {
            val id = response.getJSONObject(i).getLong("id")
            val date = response.getJSONObject(i).getString("date")
            val times = response.getJSONObject(i).getString("times")
            val name = response.getJSONObject(i).getString("name")
            val userJson = response.getJSONObject(i).getJSONObject("user")
            val userId = userJson.getLong("id")
            val eventJson = response.getJSONObject(i).getJSONObject("event")
            val eventId = eventJson.getLong("id")

            invites.add(Invite(id, date, times, name, userId, eventId))
        }
        return invites.toList()

    }

    private fun apiToAttendees(response: JSONArray): List<Attendee> {
        val attendees = ArrayList<Attendee>()
        for (i in 0 until response.length()) {
            val id = response.getJSONObject(i).getLong("id")
            val presence = response.getJSONObject(i).getString("presence")
            val userJson = response.getJSONObject(i).getJSONObject("user")
            val userId = userJson.getLong("id")
            val eventJson = response.getJSONObject(i).getJSONObject("event")
            val eventId = eventJson.getLong("id")

            attendees.add(Attendee(id, userId, eventId, Presence.valueOf(presence)))
        }
        return attendees.toList()

    }

    suspend fun makeApiPostUserCall(user: User){
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
            val event = MyApplication.database.EventDao().find(invite.eventId)
            val jsonObject = invite.toJsonObject(userWithFriends, event)

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

    fun makeApiDeleteInviteCall(invite: Invite){
        GlobalScope.launch(Dispatchers.IO) {

            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.11.55:8080/invite/${invite.inviteId}"

            val stringRequest = JsonObjectRequest(
                Request.Method.DELETE, url, null,
                {},
                {})

            queue.add(stringRequest)
        }
    }
}