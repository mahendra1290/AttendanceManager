package android.mahendra.attendancemanager.utilities

import android.content.Context
import android.mahendra.attendancemanager.database.MainDatabase
import android.mahendra.attendancemanager.repositories.SubjectRepository
import android.mahendra.attendancemanager.viewmodels.SubjectDetailViewModel
import android.mahendra.attendancemanager.viewmodels.SubjectListViewModelFactory
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object InjectorUtils {
    private fun getSubjectRepository(context: Context): SubjectRepository {
        return SubjectRepository.getInstance(
                MainDatabase.getDatabase(context.applicationContext).subjectDao()
        )
    }

    fun provideSubjectListViewModelFactory(activity: FragmentActivity): SubjectListViewModelFactory {
        val repository = getSubjectRepository(activity)
        return SubjectListViewModelFactory(repository, activity)
    }

    fun provideSubjectDetailViewModel(fragment: Fragment): SubjectDetailViewModel {
        return SubjectDetailViewModel(getSubjectRepository(fragment.requireContext()))
    }
}