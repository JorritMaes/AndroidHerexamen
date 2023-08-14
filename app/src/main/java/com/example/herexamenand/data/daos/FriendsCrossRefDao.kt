package com.example.herexamenand.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.herexamenand.data.entities.relations.entities.UserWithFriends
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef

@Dao
interface FriendsCrossRefDao {
    @Insert
    suspend fun insert(friendsCrossRef: FriendsCrossRef)

    @Delete
    suspend fun delete(friendsCrossRef: FriendsCrossRef)

}
