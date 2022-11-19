package com.marangoz.travelbook.ui.savepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marangoz.travelbook.model.TravelBook
import com.marangoz.travelbook.repository.Repository
import kotlinx.coroutines.launch

class SaveFragmentViewModel(private val repository: Repository) : ViewModel() {

    fun insertTravelBook(travelBook: TravelBook) {
        viewModelScope.launch {
            repository.insertTravelBook(travelBook)
        }
    }


}