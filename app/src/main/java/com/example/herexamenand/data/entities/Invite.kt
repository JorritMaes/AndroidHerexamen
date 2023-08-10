package com.example.herexamenand.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invites")
data class Invite(
    @PrimaryKey(autoGenerate = true) var inviteId: Long,
    var date: String,
    var times: String,
    var name: String,
    var userId: Long)
{


}
