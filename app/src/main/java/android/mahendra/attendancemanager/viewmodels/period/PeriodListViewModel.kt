package android.mahendra.attendancemanager.viewmodels.period

import android.app.Application
import android.mahendra.attendancemanager.models.Period
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.PeriodRepository
import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.lifecycle.*

import kotlinx.coroutines.launch

import kotlin.collections.List

class PeriodListViewModel internal constructor(
        private val periodRepository: PeriodRepository
) : ViewModel() {
    private val allPeriods: LiveData<List<Period>> = periodRepository.allPeriods

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
