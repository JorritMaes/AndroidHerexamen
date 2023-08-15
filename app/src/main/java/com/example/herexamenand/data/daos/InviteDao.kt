package com.example.herexamenand.data.daos

import androidx.room.*
import com.example.herexamenand.data.entities.Invite

@Dao
interface InviteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Invite)

    @Query("SELECT * FROM invites")
    suspend fun getAllEntities():List<Invite>

    @Delete
    suspend fun remove(invite: Invite)

}
