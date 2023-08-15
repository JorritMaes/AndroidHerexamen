package com.example.herexamenand.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.herexamenand.data.entities.relations.entities.UserWithFriends
import org.json.JSONObject

@Entity(tableName = "attendees")
data class Attendee(
    @PrimaryKey var attendeeId: Long,
    var userId: Long,
    var eventId: Long,
    var presence: Presence,
){
    fun toJsonObject(event: Event,userWithFriends: UserWithFriends, host: UserWithFriends): JSONObject {
        return JSONObject( "{ \"presence\":\"${this.presence.toString()}\", \"user\":${userWithFriends.toJsonObjectWithId()}, \"event\":${event.toJsonObjectWithId(host)} }")
    }
}
