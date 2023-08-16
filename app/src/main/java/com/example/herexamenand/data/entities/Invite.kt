package com.example.herexamenand.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.herexamenand.data.entities.relations.entities.EventWithHost
import com.example.herexamenand.data.entities.relations.entities.UserWithFriends
import org.json.JSONObject

@Entity(tableName = "invites")
data class Invite(
    @PrimaryKey var inviteId: Long,
    var date: String,
    var times: String,
    var name: String,
    var userId: Long,
    var eventId: Long)
{


    fun toJsonObject(userWithFriends: UserWithFriends, event: Event): JSONObject {
        return JSONObject( "{ \"date\":\"${this.date}\", \"name\":${this.name}, \"user\":${userWithFriends.toJsonObjectWithId()}, \"times\":\"${this.times}\", \"event\": {\"id\":${event.eventId}, \"user\": {\"id\":${event.userId}}} }")
    }
}
