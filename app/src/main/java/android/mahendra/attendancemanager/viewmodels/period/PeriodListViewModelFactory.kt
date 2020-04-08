package android.mahendra.attendancemanager.viewmodels.period

import android.mahendra.attendancemanager.repositories.PeriodRepository
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner

class PeriodListViewModelFactory(
        private val repository: PeriodRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeriodListViewModel::class.java)) {
            return PeriodListViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}