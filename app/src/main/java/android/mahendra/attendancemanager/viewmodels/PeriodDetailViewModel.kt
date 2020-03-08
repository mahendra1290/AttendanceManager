package android.mahendra.attendancemanager.viewmodels

import android.mahendra.attendancemanager.repositories.PeriodRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.time.Period

class PeriodDetailViewModel(
        periodRepository: PeriodRepository,
        private val periodId: Long
) : ViewModel() {
    val period = periodRepository.getPeriod(periodId)

    val title: LiveData<String>
        get() = Transformations.map(period) {
            it.subjectTitle
        }
}