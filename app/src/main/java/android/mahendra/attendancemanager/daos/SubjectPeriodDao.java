package android.mahendra.attendancemanager.daos;

import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.models.SubjectWithPeriods;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface SubjectPeriodDao {

    @Transaction
    @Query("SELECT * FROM subject_table")
    public LiveData<List<SubjectWithPeriods>> getSubjectsWithPeriod();

    @Transaction
    @Query("SELECT * FROM subject_table join period_table on title = subject_title where week_day=:weekDay")
    public LiveData<List<Subject>> getAllSubjectsOn(int weekDay);
}
