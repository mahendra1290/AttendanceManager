package android.mahendra.attendancemanager.viewmodels

import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.databinding.BaseObservable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO

import kotlinx.coroutines.launch
import java.util.*


class SubjectDetailViewModel(
        private val subjectRepository: SubjectRepository
) : BaseObservable() {

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

    fun onAttended() = CoroutineScope(IO).launch {
        subject!!.incrementClassesAttended()
        subjectRepository.update(subject)
    }

    fun onMissed() = CoroutineScope(IO).launch {
        subject!!.incrementClassesMissed()
        subjectRepository.update(subject)
    }

}