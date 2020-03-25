package android.mahendra.attendancemanager.utilities

import android.content.Context
import android.mahendra.attendancemanager.database.MainDatabase
import android.mahendra.attendancemanager.repositories.PeriodRepository
import android.mahendra.attendancemanager.repositories.SubjectRepository
import android.mahendra.attendancemanager.viewmodels.period.PeriodListViewModelFactory
import android.mahendra.attendancemanager.viewmodels.subject.SubjectDetailViewModel
import android.mahendra.attendancemanager.viewmodels.subject.SubjectDetailViewModelFactory
import android.mahendra.attendancemanager.viewmodels.subject.SubjectListViewModelFactory
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
object InjectorUtils {
    private fun getSubjectRepository(context: Context): SubjectRepository {
        return SubjectRepository.getInstance(
                MainDatabase.getDatabase(context.applicationContext).subjectDao()
        )
    }

    private fun getPeriodRepository(context: Context): PeriodRepository {
        return PeriodRepository.getInstance(
                MainDatabase.getDatabase(context.applicationContext).periodDao(),
                MainDatabase.getDatabase(context.applicationContext).subjectPeriodDao()
        )
    }

    fun provideSubjectListViewModelFactory(activity: FragmentActivity): SubjectListViewModelFactory {
        val repository = getSubjectRepository(activity)
        return SubjectListViewModelFactory(repository)
    }

    fun provideSubjectDetailViewModel(activity: FragmentActivity): SubjectDetailViewModel {
        return SubjectDetailViewModel(getSubjectRepository(activity))
    }

    fun providePeriodListViewModelFactory(activity: FragmentActivity): PeriodListViewModelFactory {
        val repository = getPeriodRepository(activity)
        return PeriodListViewModelFactory(repository)
    }
}
