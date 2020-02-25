package android.mahendra.attendancemanager.repositories

import android.mahendra.attendancemanager.daos.SubjectDao
import android.mahendra.attendancemanager.models.Subject
import androidx.lifecycle.LiveData

class SubjectRepository private constructor(
    private val mSubjectDao: SubjectDao
) {
    val allSubjects: LiveData<List<Subject>> = mSubjectDao.getAllSubjects()

    fun getSubject(title: String): Subject? {
        for (subject in allSubjects.value!!) {
            if (subject.title == title) {
                return subject
            }
        }
        return null
    }

    suspend fun updateTitle(oldTitle: String?, newTitle: String?) {
        mSubjectDao.updateTitle(oldTitle!!, newTitle!!)
    }

    suspend fun insert(subject: Subject?) {
        mSubjectDao.insert(subject!!)
    }

    suspend fun update(subject: Subject?) {
        mSubjectDao.update(subject!!)
    }

    suspend fun delete(subject: Subject?) {
        mSubjectDao.delete(subject!!)
    }

    companion object {

        @Volatile private var instance: SubjectRepository? = null

        fun getInstance(subjectDao: SubjectDao) =
                instance ?: synchronized(this) {
                    instance ?: SubjectRepository(subjectDao).also { instance = it }
                }
    }
}