package com.example.herexamenand.data.entities.relations.tables

import androidx.room.Entity

@Entity(primaryKeys = ["eventId", "attendeeId"])
data class EventAttendeeCrossRef(
    val eventId: Long,
    val attendeeId: Long,
)
