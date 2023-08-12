package com.example.herexamenand.data.entities.relations.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.UserInviteCrossRef

data class InviteWithUsers(
    @Embedded val invite: Invite,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId",
        associateBy = Junction(UserInviteCrossRef::class)
    ) val users: List<User>
)
