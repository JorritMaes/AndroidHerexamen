package com.example.herexamenand.data

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.herexamenand.data.daos.AttendeeDao
import com.example.herexamenand.data.daos.EventDao
import com.example.herexamenand.data.daos.InviteDao
import com.example.herexamenand.data.daos.UserDao
import com.example.herexamenand.data.entities.Attendee
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.EventAttendeesCrossRef
import com.example.herexamenand.data.entities.relations.tables.UserInviteCrossRef

@Database(entities = [User::class, Invite::class, Event::class, Attendee::class, EventAttendeesCrossRef::class, UserInviteCrossRef::class], version=17)
abstract class Database : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun InviteDao(): InviteDao
    abstract fun EventDao(): EventDao
    abstract fun AttendeeDao(): AttendeeDao

}