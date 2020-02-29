package android.mahendra.attendancemanager.viewmodels

import android.mahendra.attendancemanager.models.Period

import androidx.databinding.BaseObservable

class PeriodViewModel(private var period: Period? = null) : BaseObservable() {

    fun setPeriod(period: Period?) {
        this.period = period
    }

    val title: String?
        get() = period!!.subjectTitle

    val periodNumber: String
        get() = period!!.periodNumber.toString()
}
