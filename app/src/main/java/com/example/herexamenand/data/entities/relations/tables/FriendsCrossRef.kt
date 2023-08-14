package com.example.herexamenand.data.entities.relations.tables

import androidx.room.Entity

@Entity(primaryKeys = ["uId", "fId"])
data class FriendsCrossRef(
    val uId: Long,
    val fId: Long,
)
