package android.mahendra.attendancemanager.utilities

import android.content.Context
import android.mahendra.attendancemanager.data.database.MainDatabase
import android.mahendra.attendancemanager.activities.main.viewmodels.SubjectDetailViewModelFactory
import android.mahendra.attendancemanager.data.source.repositories.PeriodRepository
import android.mahendra.attendancemanager.data.source.repositories.SubjectRepository
import android.mahendra.attendancemanager.activities.timetable.viewmodels.PeriodListViewModelFactory
import android.mahendra.attendancemanager.activities.main.viewmodels.SubjectViewModelFactory
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

    fun provideSubjectListViewModelFactory(activity: FragmentActivity): SubjectViewModelFactory {
        val repository = getSubjectRepository(activity)
        return SubjectViewModelFactory(repository)
    }

    fun provideSubjectDetailViewModelFactory(subjectTitle: String, activity: FragmentActivity): SubjectDetailViewModelFactory {
        val repository = getSubjectRepository(activity)
        return SubjectDetailViewModelFactory(subjectTitle, repository)
    }

    fun providePeriodListViewModelFactory(activity: FragmentActivity): PeriodListViewModelFactory {
        val repository = getPeriodRepository(activity)
        return PeriodListViewModelFactory(repository)
    }
}
