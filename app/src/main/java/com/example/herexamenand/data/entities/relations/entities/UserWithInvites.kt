package com.example.herexamenand.data.entities.relations.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.UserInviteCrossRef

data class UserWithInvites(
    @Embedded val user : User,
    @Relation(
        parentColumn = "inviteId",
        entityColumn = "userId",
        associateBy = Junction(UserInviteCrossRef::class),
    )
    val invites: List<Invite>,
) {

}