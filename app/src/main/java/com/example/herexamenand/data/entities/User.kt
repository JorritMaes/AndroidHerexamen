package com.example.herexamenand.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.herexamenand.data.entities.relations.entities.UserWithFriends
import org.json.JSONObject

@Entity(tableName = "users")
data class User(
    @PrimaryKey var userId: Long,
    var username: String) {
    fun toJsonObject(userWithFriends: UserWithFriends): JSONObject {
        var friendList = "["
        userWithFriends.friendList.forEach { e -> friendList =  friendList.plus("{\"username\": \"${e.username}\", \"id\": \"${e.userId}\"},") }
        friendList = friendList.plus("]")
        return JSONObject( "{ \"username\":\"${this.username}\", \"id\":\"${this.userId}\", \"friendList\":${friendList} }")
    }

}
