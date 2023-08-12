package com.example.herexamenand.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) var eventId: Long,
    var date: String,
    var times: String,
    var name: String,
    var userId: Long,
    ){}
