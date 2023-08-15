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
            database.UserDao().insert(User(1, "testuser"))
            database.UserDao().insert(User(2,"testuser2"))
            database.FriendsCrossRefDao().insert(FriendsCrossRef(1,2))
            currentUser = database.UserDao().find(1L)
        }

        GlobalScope.launch(Dispatchers.IO) {

            var allUsers = database.UserDao().getAllEntities()

            val apiManager: MyApiManager = MyApiManager(applicationContext)
            for ( user in allUsers){
                apiManager.makeAPIGetCall("user/${user.userId}")
            }

        }



    }


}
