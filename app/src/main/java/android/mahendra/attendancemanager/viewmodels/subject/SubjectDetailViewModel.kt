package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.BR
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.util.*

class SubjectDetailViewModel (
        private val repository: SubjectRepository
) : ViewModel() {

    var subject: Subject? = null

    val title: String
        get() = subject!!.title

    val attendanceStat: String
        get() = "${subject!!.attendedClasses} / ${subject!!.totalClasses}"

    val attendancePercentage: String
        get() {
        if (subject!!.totalClasses == 0) {
            return "0.0%"
        }
        val percentage = (subject!!.attendedClasses.toDouble() / subject!!.totalClasses) * 100
        return String.format(Locale.ENGLISH, "%.1f%%", percentage)
    }

    val attendanceProgress: Int
        get() {
        if (subject!!.totalClasses == 0) {
            return 0
        }
        return (subject!!.attendedClasses * 100) / subject!!.totalClasses
    }

    fun onAttended() = viewModelScope.launch {
        repository.incrementAttendedClasses(subject!!.title)
    }

    fun onMissed() = viewModelScope.launch {
        repository.incrementMissedClasses(subject!!.title)
    }
}