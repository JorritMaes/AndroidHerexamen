package com.example.herexamenand.data.daos

import androidx.room.*
import com.example.herexamenand.data.entities.relations.entities.UserWithFriends
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef

@Dao
interface FriendsCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friendsCrossRef: FriendsCrossRef)

    @Query("DELETE FROM friendsCrossRef WHERE uId = :userId OR fId = :userId")
    suspend fun clearUsersFriends(userId: Long)

    @Delete
    suspend fun delete(friendsCrossRef: FriendsCrossRef)

}
