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
    private val periodRepository: PeriodRepository = PeriodRepository(application)
    private val allPeriods: LiveData<List<Period>> = periodRepository.mAllPeriods

    fun insert(period: Period) = viewModelScope.launch {
        periodRepository.insert(period)
    }

    fun update(period: Period) = viewModelScope.launch {
        periodRepository.update(period)
    }

    fun getAllPeriods(): LiveData<List<Period>> {
        return allPeriods
    }

    fun getAllPeriodsOn(weekDay: Int): LiveData<List<Period>> {
        return periodRepository.getAllPeriodsOn(weekDay)
    }

    fun getAllSubjectsOn(weekDay: Int): LiveData<List<Subject>> {
        return periodRepository.getAllSubjectsOn(weekDay)
    }

    fun deletePeriod(periodNumber: Int, weekDay: Int) = viewModelScope.launch {
        periodRepository.deletePeriod(periodNumber, weekDay)
    }
}
