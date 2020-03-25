package android.mahendra.attendancemanager.repositories

import android.app.Application
import android.mahendra.attendancemanager.daos.PeriodDao
import android.mahendra.attendancemanager.daos.SubjectDao
import android.mahendra.attendancemanager.daos.SubjectPeriodDao
import android.mahendra.attendancemanager.database.MainDatabase
import android.mahendra.attendancemanager.models.Period
import android.mahendra.attendancemanager.models.Subject

import androidx.lifecycle.LiveData

class PeriodRepository private constructor(
        private val periodDao: PeriodDao,
        private val subjectPeriodDao: SubjectPeriodDao
) {
    val allPeriods: LiveData<List<Period>> = periodDao.getAllPeriods()

    suspend fun insert(period: Period) {
            periodDao.insert(period)
    }

    suspend fun update(period: Period) {
            periodDao.update(period)
    }

    suspend fun delete(period: Period) {
            periodDao.delete(period)
    }

    fun getAllPeriodsOn(weekDay: Int): LiveData<List<Period>> {
        return periodDao.getAllPeriodsOn(weekDay)
    }

    fun getAllPeriodsOfSubject(subjectTitle: String): LiveData<List<Period>> {
        return periodDao.getAllPeriodsOfSubject(subjectTitle)
    }

    fun getAllSubjectsOn(weekDay: Int): LiveData<List<Subject>> {
        return subjectPeriodDao.getAllSubjectsOn(weekDay)
    }

    fun getPeriod(periodId: Long): LiveData<Period> {
        return periodDao.getPeriod(periodId)
    }

    suspend fun deletePeriod(periodNumber: Int, weekDay: Int) {
        periodDao.deletePeriod(periodNumber, weekDay)
    }

    companion object {

        @Volatile private var instance: PeriodRepository? = null

        fun getInstance(periodDao: PeriodDao, subjectPeriodDao: SubjectPeriodDao) =
                instance ?: synchronized(this) {
                    instance ?: PeriodRepository(periodDao, subjectPeriodDao).also { instance = it }
                }
    }
}
