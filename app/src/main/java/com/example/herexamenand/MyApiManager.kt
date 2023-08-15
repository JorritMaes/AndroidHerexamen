package com.example.herexamenand

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
}