package com.example.herexamenand.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.relations.entities.EventWithAttendees

@Dao
interface EventDao {
    @Insert
    suspend fun insert(event: Event): Long

    @Transaction
    @Query("SELECT * FROM events")
    suspend fun getAllEntities(): List<EventWithAttendees>
}