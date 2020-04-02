package android.mahendra.attendancemanager.viewmodels.subject

import android.mahendra.attendancemanager.Event
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.SubjectRepository
import androidx.lifecycle.*

import kotlinx.coroutines.launch

import kotlin.collections.List
import kotlin.collections.ArrayList

class SubjectViewModel internal constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    private val _openOptionDialog = MutableLiveData<Event<String>>()
    val openOptionDialog: LiveData<Event<String>>
         get() = _openOptionDialog

    val allSubjects: LiveData<List<Subject>> = subjectRepository.allSubjects

    fun insert(subject: Subject) = viewModelScope.launch {
        subjectRepository.insert(subject)
    }

    fun update(subject: Subject) = viewModelScope.launch {
        subjectRepository.update(subject)
    }

    fun delete(subject: Subject) = viewModelScope.launch {
        subjectRepository.delete(subject)
    }

    fun onResetAttendance(subjectTitle: String) = viewModelScope.launch {
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

    fun getSubjectTitles(): LiveData<List<String>> {
        return Transformations.map(allSubjects, this::extractTitles)
    }

    fun onDeleteSubjectWith(subjectTitle: String) = viewModelScope.launch {
        val subject = subjectRepository.getSubject(subjectTitle)
        subjectRepository.delete(subject)
    }

    fun onOptionSelected(subjectTitle: String) {
        _openOptionDialog.value = Event(subjectTitle)
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
