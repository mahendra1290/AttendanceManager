package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.util.*

class SubjectDetailViewModel (
        private val repository: SubjectRepository
) : BaseObservable() {
    private val viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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
        uiScope.launch {
            subject!!.incrementClassesAttended()
            update(subject!!)
            notifyChange()
        }
    }

    fun onMissed() {
        uiScope.launch {
            subject!!.incrementClassesMissed()
            update(subject!!)
            notifyChange()
        }
    }

    suspend fun update(subject: Subject) {
        withContext(Dispatchers.IO) {
            repository.update(subject)
        }
    }
}