package com.example.herexamenand

import android.app.Application
import androidx.room.Room
import com.example.herexamenand.data.Database
import com.example.herexamenand.data.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyApplication : Application() {
    companion object {
        lateinit var database: Database
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, Database::class.java, "herexamen_db")
            .build()
        GlobalScope.launch(Dispatchers.IO) {
            database.UserDao().insert(User(0, "testuser"))
        }

    }
}