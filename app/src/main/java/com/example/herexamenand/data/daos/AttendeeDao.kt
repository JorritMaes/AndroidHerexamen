package com.example.herexamenand.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.herexamenand.data.entities.Attendee

@Dao
interface AttendeeDao {
    @Insert
    suspend fun insert(attendee: Attendee)

    @Query("SELECT * FROM attendees WHERE eventId= :eventId")
    suspend fun getAttendeesOfEvent(eventId: Long): List<Attendee>

    @Query("SELECT * FROM attendees")
    suspend fun getAllEntities(): List<Attendee>
}