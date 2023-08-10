package com.example.herexamenand.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(
    @PrimaryKey(autoGenerate = true) var userId: Long,
    var username: String) {
}