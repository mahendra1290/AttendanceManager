package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SubjectDetailViewModel(
        private val subjectListViewModel: SubjectListViewModel
) {

    var subject: Subject? = null

    val title: String
        get() = subject!!.title

    val attendanceStat: String
        get() = "${subject!!.attendedClasses} / ${subject!!.totalClasses}"

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

    fun onAttended() {
        subject!!.incrementClassesAttended()
        subjectListViewModel.update(subject!!)
    }

    fun onMissed() {
        subject!!.incrementClassesMissed()
        subjectListViewModel.update(subject!!)
    }
}