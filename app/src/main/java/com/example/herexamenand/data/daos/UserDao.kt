package com.example.herexamenand.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.entities.UserWithFriends

@Dao
interface UserDao {
    @Insert
    suspend fun insert(entity: User)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun find(userId: Long): User

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun findByName(username: String): User

    @Query("SELECT * FROM users")
    suspend fun getAllEntities():List<User>

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getFriends(userId: Long): UserWithFriends
}
