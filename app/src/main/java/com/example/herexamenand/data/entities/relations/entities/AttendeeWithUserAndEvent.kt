package com.example.herexamenand.data.entities.relations.entities

import androidx.room.Embedded
import com.example.herexamenand.data.entities.Attendee
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.User

data class AttendeeWithUserAndEvent(
    @Embedded val attendee: Attendee,
    @Embedded val user: User,
    @Embedded val event: Event,
)
