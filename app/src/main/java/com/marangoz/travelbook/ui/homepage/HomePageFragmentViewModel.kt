package com.marangoz.travelbook.ui.homepage

import android.icu.text.CaseMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marangoz.travelbook.model.TravelBook
import com.marangoz.travelbook.repository.Repository
import kotlinx.coroutines.launch

class HomePageFragmentViewModel(private val repository: Repository) : ViewModel() {

    val travelBookList: MutableLiveData<List<TravelBook>> = MutableLiveData()

    fun allTravelBook() {
        viewModelScope.launch {
           travelBookList.value = repository.allTravelBook()
        }
    }

    fun deleteTravelBook(travelBook: TravelBook) {
        viewModelScope.launch {
            repository.deleteTravelBook(travelBook)
            travelBookList.value = repository.allTravelBook()
        }
    }
    fun byTitle(title: String) {
        viewModelScope.launch {
            travelBookList.value = repository.byTitle(title)
        }
    }


}