package com.example.herexamenand.data.daos

import androidx.room.*
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.entities.EventWithAttendees

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun find(eventId: Long): Event

    @Transaction
    @Query("SELECT * FROM events")
    suspend fun getAllEntities(): List<EventWithAttendees>
}