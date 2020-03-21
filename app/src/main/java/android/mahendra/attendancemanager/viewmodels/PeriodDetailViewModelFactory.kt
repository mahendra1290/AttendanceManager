package android.mahendra.attendancemanager.viewmodels

import android.mahendra.attendancemanager.repositories.PeriodRepository
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PeriodDetailViewModelFactory(
        val periodId: Long,
        val owner: LifecycleOwner
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PeriodDetailViewModel(PeriodRepository(owner), periodId) as T
    }
}