package com.example.herexamenand.data.entities.relations.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef
import org.json.JSONObject


class UserWithFriends (
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId",
        associateBy = Junction(FriendsCrossRef::class, parentColumn = "uId", entityColumn = "fId")
    ) val friendList: List<User>,
){
    fun toJsonObjectWithId(): JSONObject {
        var friends = "["
        this.friendList.forEach { e -> friends =  friends.plus("{\"username\": \"${e.username}\", \"id\": \"${e.userId}\"},") }
        friends = friends.plus("]")
        return JSONObject( "{ \"username\":\"${this.user.username}\", \"id\":\"${this.user.userId}\", \"friendList\":${friends} }")
    }
}
