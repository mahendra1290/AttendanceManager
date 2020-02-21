package android.mahendra.attendancemanager.daos;

import android.mahendra.attendancemanager.models.Subject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Subject subject);

    @Update
    void update(Subject subject);

    @Delete
    void delete(Subject subject);

    @Query("SELECT * FROM subject_table ORDER BY title")
    LiveData<List<Subject>> getAllSubjects();

    @Query("UPDATE subject_table SET title = :newTitle WHERE title = :oldTitle")
    void updateTitle(String oldTitle, String newTitle);
}
