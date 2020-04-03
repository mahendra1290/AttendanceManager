package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.Event
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.lifecycle.*

import kotlinx.coroutines.launch

import kotlin.collections.List
import kotlin.collections.ArrayList

class SubjectsViewModel internal constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    private val _optionSelectEvent = MutableLiveData<Event<String>>()
    val optionSelectEvent: LiveData<Event<String>>
         get() = _optionSelectEvent

    private val _subjectDeleteEvent = MutableLiveData<Event<String>>()
    val subjectDeleteEvent: LiveData<Event<String>>
        get() = _subjectDeleteEvent

    private val _resetAttendanceEvent = MutableLiveData<Event<String>>()
    val resetAttendanceEvent: LiveData<Event<String>>
        get() = _resetAttendanceEvent

    val allSubjects: LiveData<List<Subject>> = subjectRepository.allSubjects

    fun insert(subject: Subject) = viewModelScope.launch {
        subjectRepository.insert(subject)
    }

    fun update(subject: Subject) = viewModelScope.launch {
        subjectRepository.update(subject)
    }

    fun delete(subjectTitle: String) = viewModelScope.launch {
        subjectRepository.delete(subjectTitle)
    }

    fun resetAttendance(subjectTitle: String) = viewModelScope.launch {
        subjectRepository.resetAttendance(subjectTitle)
    }

    private fun extractTitles(subjects: List<Subject>): List<String> {
        val subjectTitles = ArrayList<String>()
        for (subject in subjects) {
            subjectTitles.add(subject.title)
        }
        return subjectTitles
    }

    fun onUpdateTitle(oldTitle: String, newTitle: String) = viewModelScope.launch {
        subjectRepository.updateTitle(oldTitle, newTitle)
    }

    val subjectTitles: LiveData<List<String>>
        get() = Transformations.map(allSubjects, this::extractTitles)

    fun onOptionSelected(subjectTitle: String) {
        _optionSelectEvent.value = Event(subjectTitle)
    }

    fun onDeleteSubject(subjectTitle: String) {
        _subjectDeleteEvent.value = Event(subjectTitle)
    }

    fun onResetAttendance(subjectTitle: String) {
        _resetAttendanceEvent.value = Event(subjectTitle)
    }

    fun getSubject(subjectTitle: String): Subject? {
        return subjectRepository.getSubject(subjectTitle)
    }

    fun onNewSubject(subjectTitle: String) = viewModelScope.launch {
        subjectRepository.insert(Subject(subjectTitle))
    }

    fun onUpdateAttendance(subjectTitle: String, attendedClasses: Int, totalClasses: Int) = viewModelScope.launch {
        val missedClasses = totalClasses - attendedClasses
        subjectRepository.updateAttendance(subjectTitle, attendedClasses, missedClasses)
    }
}
