package com.marangoz.travelbook.ui.savepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marangoz.travelbook.repository.Repository

class SaveFragmentViewModelFactory (private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SaveFragmentViewModel(repository) as T
    }

}