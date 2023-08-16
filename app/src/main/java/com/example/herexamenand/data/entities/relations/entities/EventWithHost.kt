package com.example.herexamenand.data.entities.relations.entities

import androidx.room.Embedded
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.User

data class EventWithHost(
    @Embedded val event: Event,
    @Embedded val host: User,
)
