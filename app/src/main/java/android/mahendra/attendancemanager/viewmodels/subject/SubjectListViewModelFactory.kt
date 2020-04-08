package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.repositories.SubjectRepository
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import kotlin.IllegalArgumentException

class SubjectListViewModelFactory(
        private val repository: SubjectRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectListViewModel::class.java)) {
            return SubjectListViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}