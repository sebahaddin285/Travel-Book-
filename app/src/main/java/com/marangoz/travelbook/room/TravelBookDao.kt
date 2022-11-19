package com.marangoz.travelbook.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.marangoz.travelbook.model.TravelBook

@Dao
interface TravelBookDao {

    @Query("select * from travel_book_table")
    suspend fun allTravelBook(): List<TravelBook>

    @Query("select * from travel_book_table WHERE title LIKE :title")
    suspend fun byTitle(title : String) : List<TravelBook>

    @Delete
    suspend fun deleteTravelBook(travelBook: TravelBook)

    @Insert
    suspend fun insertTravelBook(travelBook: TravelBook)

}