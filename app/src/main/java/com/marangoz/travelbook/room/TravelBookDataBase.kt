package com.marangoz.travelbook.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marangoz.travelbook.model.TravelBook

@Database(entities = [TravelBook::class], version = 1)
abstract class TravelBookDataBase :  RoomDatabase(){

    abstract fun getTravelBookDao() : TravelBookDao

    companion object {
        @Volatile
        var INSTANCE: TravelBookDataBase? = null

        fun accsessDatabase(context: Context): TravelBookDataBase? {
            if (INSTANCE == null) {
                synchronized(TravelBookDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TravelBookDataBase::class.java, "travel_database.sqlite")
                        .build()
                }
            }
            return INSTANCE
        }

    }


}