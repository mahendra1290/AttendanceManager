package android.mahendra.attendancemanager.repositories

import android.app.Application
import android.mahendra.attendancemanager.daos.SubjectDao
import android.mahendra.attendancemanager.database.MainDatabase.Companion.getDatabase
import android.mahendra.attendancemanager.models.Subject
import androidx.lifecycle.LiveData

class SubjectRepository(application: Application?) {
    private val mSubjectDao: SubjectDao
    val allSubjects: LiveData<List<Subject>>

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

    init {
        val db = getDatabase(application!!)
        mSubjectDao = db!!.subjectDao()
        allSubjects = mSubjectDao.getAllSubjects()
    }
}