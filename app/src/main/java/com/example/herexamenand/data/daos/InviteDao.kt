package com.example.herexamenand.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.herexamenand.data.entities.Invite

@Dao
interface InviteDao {
    @Insert
    suspend fun insert(entity: Invite)

    @Query("SELECT * FROM invites")
    suspend fun getAllEntities():List<Invite>
}
