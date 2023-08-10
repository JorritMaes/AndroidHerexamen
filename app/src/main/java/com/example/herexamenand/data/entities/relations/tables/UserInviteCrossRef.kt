package com.example.herexamenand.data.entities.relations.tables

import androidx.room.Entity

@Entity(primaryKeys = ["inviteId", "userId"])
class UserInviteCrossRef (
    val userId: Long,
    val inviteId: Long,
){

}