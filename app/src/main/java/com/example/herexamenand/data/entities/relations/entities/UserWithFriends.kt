package com.example.herexamenand.data.entities.relations.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef


class UserWithFriends (
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId",
        associateBy = Junction(FriendsCrossRef::class, parentColumn = "uId", entityColumn = "fId")
    ) val friendList: List<User>,
)
