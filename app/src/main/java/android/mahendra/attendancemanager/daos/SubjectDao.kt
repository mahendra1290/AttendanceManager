package android.mahendra.attendancemanager.daos

import android.mahendra.attendancemanager.models.Subject

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlin.collections.List

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(subject: Subject)

    @Update
    suspend fun update(subject: Subject)

    @Delete
    suspend fun delete(subject: Subject)

    @Query("SELECT * FROM subject_table ORDER BY title")
    fun getAllSubjects(): LiveData<List<Subject>>

    @Query("UPDATE subject_table SET title = :newTitle WHERE title = :oldTitle")
    suspend fun updateTitle(oldTitle: String, newTitle: String)
}
