package com.example.herexamenand.data

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.herexamenand.data.daos.InviteDao
import com.example.herexamenand.data.daos.UserDao
import com.example.herexamenand.data.entities.Invite
import com.example.herexamenand.data.entities.User

@Database(entities = [User::class, Invite::class], version=6)
abstract class Database : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun InviteDao(): InviteDao

}