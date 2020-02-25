package android.mahendra.attendancemanager.viewmodels

import android.mahendra.attendancemanager.models.Period

import androidx.databinding.BaseObservable

class PeriodViewModel(private var mPeriod: Period? = null) : BaseObservable() {

    fun setPeriod(period: Period?) {
        mPeriod = period
    }

    val title: String?
        get() = mPeriod!!.subjectTitle

    val periodNumber: String
        get() = mPeriod!!.periodNumber.toString()
}
