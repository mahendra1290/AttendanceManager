package android.mahendra.attendancemanager.viewmodels

import android.app.Application
import android.mahendra.attendancemanager.models.Period
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.PeriodRepository

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import kotlin.collections.List

class PeriodListViewModel(application: Application) : AndroidViewModel(application) {
    private val mPeriodRepository: PeriodRepository = PeriodRepository(application)
    private val mAllPeriods: LiveData<List<Period>> = mPeriodRepository.mAllPeriods

    fun insert(period: Period) = viewModelScope.launch {
        mPeriodRepository.insert(period)
    }

    fun update(period: Period) = viewModelScope.launch {
        mPeriodRepository.update(period)
    }

    fun getAllPeriods(): LiveData<List<Period>> {
        return mAllPeriods
    }

    fun getAllPeriodsOn(weekDay: Int): LiveData<List<Period>>  {
        return mPeriodRepository.getAllPeriodsOn(weekDay)
    }

    fun getAllSubjectsOn(weekDay: Int): LiveData<List<Subject>> {
        return mPeriodRepository.getAllSubjectsOn(weekDay)
    }

    fun deletePeriod(periodNumber: Int, weekDay: Int) = viewModelScope.launch {
        mPeriodRepository.deletePeriod(periodNumber, weekDay)
    }
}
