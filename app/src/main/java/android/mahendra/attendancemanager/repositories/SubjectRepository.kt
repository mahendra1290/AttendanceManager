package android.mahendra.attendancemanager.repositories

import android.mahendra.attendancemanager.daos.SubjectDao
import android.mahendra.attendancemanager.models.Subject
import androidx.lifecycle.LiveData

class SubjectRepository private constructor(
    private val subjectDao: SubjectDao
) {
    val allSubjects: LiveData<List<Subject>> = subjectDao.getAllSubjects()

    fun getSubject(title: String): Subject? {
        for (subject in allSubjects.value!!) {
            if (subject.title == title) {
                return subject
            }
        }
        return null
    }

    suspend fun updateTitle(oldTitle: String?, newTitle: String?) {
        subjectDao.updateTitle(oldTitle!!, newTitle!!)
    }

    suspend fun insert(subject: Subject?) {
        subjectDao.insert(subject!!)
    }

    suspend fun update(subject: Subject?) {
        subjectDao.update(subject!!)
    }

    suspend fun incrementAttendedClasses(title: String) {
        subjectDao.incrementAttendedClasses(title)
    }

    suspend fun incrementMissedClasses(title: String) {
        subjectDao.incrementMissedClasses(title)
    }

    suspend fun delete(subject: Subject?) {
        subjectDao.delete(subject!!)
    }

    suspend fun updateAttendance(title: String, attendedClasses: Int, missedClasses: Int) {
        subjectDao.updateAttendance(title, attendedClasses, missedClasses)
    }

    suspend fun resetAttendance(title: String) {
        updateAttendance(title, 0, 0)
    }

    companion object {

        @Volatile private var instance: SubjectRepository? = null

        fun getInstance(subjectDao: SubjectDao) =
                instance ?: synchronized(this) {
                    instance ?: SubjectRepository(subjectDao).also { instance = it }
                }
    }
}