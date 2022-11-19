package com.marangoz.travelbook.repository

import androidx.lifecycle.MutableLiveData
import com.marangoz.travelbook.model.TravelBook
import com.marangoz.travelbook.room.TravelBookDao

class Repository(private val tDao : TravelBookDao) {


    suspend fun allTravelBook(): List<TravelBook>{
        return tDao.allTravelBook()
    }
    suspend fun byTitle(title : String): List<TravelBook>{
        return tDao.byTitle(title)
    }
    suspend fun deleteTravelBook(travelBook: TravelBook){
         tDao.deleteTravelBook(travelBook)
    }
    suspend fun insertTravelBook(travelBook: TravelBook){
         tDao.insertTravelBook(travelBook)
    }




}