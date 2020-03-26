package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.BR
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
) : BaseObservable() {

    var subject: Subject? = null

    val title: String
        @Bindable get() = subject!!.title

    val attendanceStat: String
        @Bindable get() = "${subject!!.attendedClasses} / ${subject!!.totalClasses}"

    val attendancePercentage: String
        @Bindable get() {
        if (subject!!.totalClasses == 0) {
            return "0.0%"
        }
        val percentage = (subject!!.attendedClasses.toDouble() / subject!!.totalClasses) * 100
        return String.format(Locale.ENGLISH, "%.1f%%", percentage)
    }

    val attendanceProgress: Int
        @Bindable get() {
        if (subject!!.totalClasses == 0) {
            return 0
        }
        return (subject!!.attendedClasses * 100) / subject!!.totalClasses
    }

    fun onAttended() {
        subject!!.incrementClassesAttended()
        notifyChange()
        subjectListViewModel.update(subject!!)
    }

    fun onMissed() {
        subject!!.incrementClassesMissed()
        notifyChange()
        subjectListViewModel.update(subject!!)
    }
}