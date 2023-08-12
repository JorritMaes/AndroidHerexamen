package com.example.herexamenand.data.entities.relations.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.herexamenand.data.entities.Attendee
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.relations.tables.EventAttendeeCrossRef

data class EventWithAttendees(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "attendeeId",
        associateBy = Junction(EventAttendeeCrossRef::class),
    ) val attendeeList: List<Attendee>

)
