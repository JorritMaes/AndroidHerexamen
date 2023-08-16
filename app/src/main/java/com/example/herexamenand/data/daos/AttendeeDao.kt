package com.example.herexamenand.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.herexamenand.data.entities.Attendee
import com.example.herexamenand.data.entities.relations.entities.AttendeeWithUserAndEvent
import com.example.herexamenand.data.entities.relations.entities.EventWithAttendees

@Dao
interface AttendeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendee: Attendee)

    @Query("SELECT * FROM attendees WHERE eventId= :eventId")
    suspend fun getAttendeesOfEvent(eventId: Long): List<Attendee>

    @Query("SELECT * FROM attendees")
    suspend fun getAllEntities(): List<Attendee>

    @Query("SELECT * FROM attendees WHERE userId = :userId")
    suspend fun getMyAttendeesWithEventAndUser(userId: Long): List<AttendeeWithUserAndEvent>
}