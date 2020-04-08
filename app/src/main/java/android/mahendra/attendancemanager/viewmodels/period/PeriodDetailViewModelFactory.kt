package android.mahendra.attendancemanager.viewmodels.period

import android.app.Application
import android.mahendra.attendancemanager.repositories.PeriodRepository
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PeriodDetailViewModelFactory(
        val periodId: Long,
        val periodRepository: PeriodRepository,
        val application: Application
) : ViewModelProvider.NewInstanceFactory() {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return PeriodDetailViewModel(PeriodRepository(application), periodId) as T
//    }

    init {
        val period = periodRepository.getPeriod(periodId)
    }
}