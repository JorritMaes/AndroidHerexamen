package com.example.herexamenand.data.daos

import androidx.room.*
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.entities.EventWithAttendees
import com.example.herexamenand.data.entities.relations.entities.EventWithHost

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun find(eventId: Long): Event

    @Transaction
    @Query("SELECT * FROM events")
    suspend fun getAllEntities(): List<Event>

    @Transaction
    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun findEventWithAttendees(eventId: Long): EventWithAttendees

    @Delete
    suspend fun remove(event: Event)
//    @Transaction
//    @Query("SELECT * FROM events JOIN attendees ON attendees.userId = :userId  WHERE ")
//    suspend fun getMyEvents(userId: Long): List<EventWithAttendees>
}