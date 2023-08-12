package com.example.herexamenand.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendees")
data class Attendee(
    @PrimaryKey(autoGenerate = true)val attendeeId: Long,
    val userId: Long,
    val eventId: Long,
    val presence: Presence,
){}
