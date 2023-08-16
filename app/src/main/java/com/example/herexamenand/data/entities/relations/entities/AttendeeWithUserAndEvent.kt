package com.example.herexamenand.data.entities.relations.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.herexamenand.data.entities.Attendee
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.User

data class AttendeeWithUserAndEvent(
    @Embedded val attendee: Attendee,
    @Relation(parentColumn = "userId", entityColumn = "userId")
    val user: User,
    @Relation(parentColumn = "eventId", entityColumn = "eventId")
    val event: Event,
)
