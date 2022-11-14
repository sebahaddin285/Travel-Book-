package com.marangoz.travelbook.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "travel_book_table")
data class TravelBook(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") @NotNull val id : Int,
    @ColumnInfo(name = "title") @NotNull val title : String,
    @ColumnInfo(name = "date") @NotNull val date : String,
    @ColumnInfo(name = "images") @NotNull val images : Bitmap,

)
