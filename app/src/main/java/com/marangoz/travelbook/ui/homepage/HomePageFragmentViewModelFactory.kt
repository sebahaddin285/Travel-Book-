package com.marangoz.travelbook.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marangoz.travelbook.repository.Repository
import com.marangoz.travelbook.ui.savepage.SaveFragmentViewModel

class HomePageFragmentViewModelFactory (private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomePageFragmentViewModel(repository) as T
    }

}