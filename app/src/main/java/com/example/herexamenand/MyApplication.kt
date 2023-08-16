package com.example.herexamenand

import android.app.Application
import androidx.room.Room
import com.example.herexamenand.data.Database
import com.example.herexamenand.data.entities.User
import com.example.herexamenand.data.entities.relations.tables.FriendsCrossRef
import kotlinx.coroutines.*

class MyApplication : Application() {
    companion object {
        lateinit var database: Database
         var currentUser: User = User(0, "testUser")
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, Database::class.java, "herexamen_db")
            .fallbackToDestructiveMigration()
            .build()

        GlobalScope.launch(Dispatchers.Main){
            syncDatabases()
        }

        GlobalScope.launch(Dispatchers.IO) {
            val apiManager: MyApiManager = MyApiManager(applicationContext)
            currentUser = apiManager.getUser(1)
        }
    }

    private suspend fun syncDatabases() {
        val apiManager = MyApiManager(applicationContext)

        apiManager.syncUsers()
        apiManager.syncAttendees()
        apiManager.syncInvites()
        apiManager.syncEvents()

    }


}
