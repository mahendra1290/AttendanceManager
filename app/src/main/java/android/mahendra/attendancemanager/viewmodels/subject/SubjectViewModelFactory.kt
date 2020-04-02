package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.IllegalArgumentException

class SubjectViewModelFactory(
        private val repository: SubjectRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectViewModel::class.java)) {
            return SubjectViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}