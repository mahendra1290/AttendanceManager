package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SubjectDetailViewModelFactory(
        private val repository: SubjectRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectDetailViewModel::class.java)) {
            return SubjectDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}