package com.example.herexamenand

import android.app.Application
import androidx.room.Room
import com.example.herexamenand.data.Database
import com.example.herexamenand.data.entities.User
import kotlinx.coroutines.*

class MyApplication : Application() {
    companion object {
        lateinit var database: Database
        lateinit var currentUser: User
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, Database::class.java, "herexamen_db")
            .fallbackToDestructiveMigration()
            .build()
        GlobalScope.launch(Dispatchers.Main){
            currentUser = database.UserDao().find(1L)
        }
        GlobalScope.launch(Dispatchers.IO) {

            var allUsers = database.UserDao().getAllEntities()

            val apiManager: MyApiManager = MyApiManager(applicationContext)
            for ( user in allUsers){
                apiManager.makeAPIGetCall("user/${user.userId}")
            }



            /*database.UserDao().insert(User(0, "testuser"))
            database.UserDao().insert(User(0,"testuser2"))
            database.FriendsCrossRefDao().insert(FriendsCrossRef(1,2))*/
        }



    }


}
