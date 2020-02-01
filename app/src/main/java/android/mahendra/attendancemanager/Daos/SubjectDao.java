package android.mahendra.attendancemanager.Daos;

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

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(Subject subject);

    @Update
    public void update(Subject subject);

    @Delete
    public void delete(Subject subject);

    @Query("SELECT * FROM subject_table ORDER BY title")
    LiveData<List<Subject>> getAllSubjects();

    @Query("SELECT * FROM subject_table WHERE title=:subjectTitle")
    LiveData<Subject> getSubject(String subjectTitle);

}
