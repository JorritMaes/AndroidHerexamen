package com.example.herexamenand.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.herexamenand.data.entities.relations.entities.UserWithFriends
import org.json.JSONObject

@Entity(tableName = "events")
data class Event(
    @PrimaryKey var eventId: Long,
    var date: String,
    var times: String,
    var name: String,
    var userId: Long,
    ){
    fun toJsonObject(userWithFriends: UserWithFriends): JSONObject {
        return JSONObject( "{ \"date\":\"${this.date}\", \"name\":${this.name}, \"user\":${userWithFriends.toJsonObjectWithId()}, \"times\":\"${this.times}\" }")
    }
    fun toJsonObjectWithId(userWithFriends: UserWithFriends): JSONObject {
        return JSONObject( "{ \"id\": ${this.eventId}, date\":\"${this.date}\", \"name\":${this.name}, \"user\":${userWithFriends.toJsonObjectWithId()}, \"times\":\"${this.times}\" }")
    }
}
