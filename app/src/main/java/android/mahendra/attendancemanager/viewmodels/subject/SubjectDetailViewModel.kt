package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import java.util.*

class SubjectDetailViewModel(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    var subject: Subject? = null

    fun getTitle(): String {
        return subject!!.title
    }

    fun getAttendanceStat() = "${subject!!.attendedClasses} / ${subject!!.totalClasses}"

    fun getAttendancePercentage(): String {
        if (subject!!.totalClasses == 0) {
            return "0.0%"
        }
        val percentage = (subject!!.attendedClasses.toDouble() / subject!!.totalClasses) * 100
        return String.format(Locale.ENGLISH, "%.1f%%", percentage)
    }

    fun getAttendanceProgress(): Int {
        if (subject!!.totalClasses == 0) {
            return 0
        }
        return (subject!!.attendedClasses * 100) / subject!!.totalClasses
    }

    fun onAttended() = viewModelScope.launch {
        subject!!.incrementClassesAttended()
        subjectRepository.update(subject)
    }

    fun onMissed() = viewModelScope.launch {
        subject!!.incrementClassesMissed()
        subjectRepository.update(subject)
    }
}