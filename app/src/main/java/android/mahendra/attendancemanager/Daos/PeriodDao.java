package android.mahendra.attendancemanager.Daos;

import android.mahendra.attendancemanager.models.Period;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PeriodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Period period);

    @Update
    void update(Period period);

    @Delete
    void delete(Period period);

    @Query("Delete from period_table")
    void deleteAll();

    @Query("SELECT * FROM PERIOD_TABLE WHERE week_day=:weekDay")
    LiveData<List<Period>> getAllPeriodsOn(final int weekDay);

    @Query("SELECT * FROM PERIOD_TABLE WHERE subject_title=:subjectTitle")
    LiveData<List<Period>> getAllPeriodsOfSubject(String subjectTitle);
}
