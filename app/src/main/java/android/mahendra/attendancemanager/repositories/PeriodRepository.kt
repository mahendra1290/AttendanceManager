package android.mahendra.attendancemanager.repositories

import android.app.Application
import android.mahendra.attendancemanager.daos.PeriodDao
import android.mahendra.attendancemanager.daos.SubjectPeriodDao
import android.mahendra.attendancemanager.database.MainDatabase
import android.mahendra.attendancemanager.models.Period
import android.mahendra.attendancemanager.models.Subject

import androidx.lifecycle.LiveData

class PeriodRepository(application: Application) {
    private val mPeriodDao: PeriodDao
    private val mSubjectPeriodDao: SubjectPeriodDao
    val mAllPeriods: LiveData<List<Period>>

    init {
        val db = MainDatabase.getDatabase(application)
        mPeriodDao = db!!.mPeriodDao()
        mSubjectPeriodDao = db.mSubjectPeriodDao()
        mAllPeriods = mPeriodDao.getAllPeriods()
    }

    suspend fun insert(period: Period) {
            mPeriodDao.insert(period)
    }

    suspend fun update(period: Period) {
            mPeriodDao.update(period)
    }

    suspend fun delete(period: Period) {
            mPeriodDao.delete(period)
    }

    fun getAllPeriodsOn(weekDay: Int): LiveData<List<Period>> {
        return mPeriodDao.getAllPeriodsOn(weekDay)
    }

    fun getAllPeriodsOfSubject(subjectTitle: String): LiveData<List<Period>> {
        return mPeriodDao.getAllPeriodsOfSubject(subjectTitle)
    }

    fun getAllSubjectsOn(weekDay: Int): LiveData<List<Subject>> {
        return mSubjectPeriodDao.getAllSubjectsOn(weekDay)
    }

    suspend fun deletePeriod(periodNumber: Int, weekDay: Int) {
        mPeriodDao.deletePeriod(periodNumber, weekDay)
    }
}
