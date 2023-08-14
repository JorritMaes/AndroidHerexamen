package com.example.herexamenand.data

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.herexamenand.data.daos.*
import com.example.herexamenand.data.entities.Attendee
import com.example.herexamenand.data.entities.Event
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.EventAttendeesCrossRef
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef
import com.example.herexamenand.data.entities.relations.tables.UserInviteCrossRef

@Database(
    entities = [
    User::class, Invite::class,
    Event::class, Attendee::class,
    EventAttendeesCrossRef::class,
    UserInviteCrossRef::class,
    FriendsCrossRef::class
               ],
    version=1)
abstract class Database : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun InviteDao(): InviteDao
    abstract fun EventDao(): EventDao
    abstract fun AttendeeDao(): AttendeeDao
    abstract fun FriendsCrossRefDao(): FriendsCrossRefDao

}