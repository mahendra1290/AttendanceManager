package android.mahendra.attendancemanager.viewmodels

import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.repositories.SubjectRepository

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

import kotlin.collections.List
import java.util.Locale
import kotlin.collections.ArrayList

class SubjectViewModel(application: Application): AndroidViewModel(application) {

    private val mSubjectRepository: SubjectRepository = SubjectRepository(application)
    private val mAllSubjects: LiveData<List<Subject>> = mSubjectRepository.allSubjects

    fun getAllSubjects(): LiveData<List<Subject>> {
        return mAllSubjects
    }

    fun insert(subject: Subject) = viewModelScope.launch {
        mSubjectRepository.insert(subject)
    }

    fun update(subject: Subject) = viewModelScope.launch {
        mSubjectRepository.update(subject)
    }

    fun delete(subject: Subject) = viewModelScope.launch {
        mSubjectRepository.delete(subject)
    }

    fun onResetAttendance(subject: Subject) = viewModelScope.launch {
        subject.missedClasses = 0
        subject.attendedClasses = 0
        mSubjectRepository.update(subject)
    }

    private fun extractTitles(subjects: List<Subject>): List<String> {
        val subjectTitles = ArrayList<String>()
        for (subject in subjects) {
            subjectTitles.add(subject.title)
        }
        return subjectTitles
    }

    fun onUpdateTitle(oldTitle : String, newTitle : String) = viewModelScope.launch {
        mSubjectRepository.updateTitle(oldTitle, newTitle)
    }

    fun getSubjectTitles() : LiveData<List<String>> {
        return Transformations.map(mAllSubjects, this::extractTitles)
    }

    fun onDeleteSubjectWith(subjectTitle: String) = viewModelScope.launch {
        val subject = mSubjectRepository.getSubject(subjectTitle)
        mSubjectRepository.delete(subject)
    }

    fun onResetAttendance(subjectTitle: String) = viewModelScope.launch {
        var subject = mSubjectRepository.getSubject(subjectTitle)
        subject?.attendedClasses = 0
        subject?.missedClasses = 0
        mSubjectRepository.update(subject)
    }

    fun getSubject(subjectTitle: String): Subject? {
        return mSubjectRepository.getSubject(subjectTitle)
    }

    fun getTitle(subject: Subject): String {
        return subject.title
    }

    fun getAttendanceStat(subject: Subject) = "${subject.attendedClasses} / ${subject.totalClasses}"

    fun getAttendancePercentage(subject: Subject): String {
        if (subject.totalClasses == 0) {
            return "0.0%"
        }
        val percentage = (subject.attendedClasses.toDouble() / subject.totalClasses) * 100
        return String.format(Locale.ENGLISH, "%.1f%%", percentage);
    }

    fun getAttendanceProgress(subject: Subject): Int {
        if (subject.totalClasses == 0) {
            return 0
        }
        return (subject.attendedClasses * 100) / subject.totalClasses
    }

    fun onAttended(subject: Subject) {
        subject.incrementClassesAttended()
        update(subject)
    }

    fun onMissed(subject: Subject) {
        subject.incrementClassesMissed()
        update(subject)
    }
}