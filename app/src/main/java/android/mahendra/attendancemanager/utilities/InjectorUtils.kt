package android.mahendra.attendancemanager.utilities

import android.content.Context
import android.mahendra.attendancemanager.database.MainDatabase
import android.mahendra.attendancemanager.repositories.SubjectRepository
import android.mahendra.attendancemanager.viewmodels.SubjectDetailViewModel
import android.mahendra.attendancemanager.viewmodels.SubjectListViewModelFactory
import androidx.fragment.app.Fragment

object InjectorUtils {
    fun getSubjectRepository(context: Context): SubjectRepository {
        return SubjectRepository.getInstance(
                MainDatabase.getDatabase(context.applicationContext).subjectDao()
        )
    }

    fun provideSubjectListViewModelFactory(fragment: Fragment): SubjectListViewModelFactory {
        val repository = getSubjectRepository(fragment.requireContext())
        return SubjectListViewModelFactory(repository, fragment)
    }

    fun provideSubjectDetailViewModel(fragment: Fragment): SubjectDetailViewModel {
        return SubjectDetailViewModel(getSubjectRepository(fragment.requireContext()))
    }
}