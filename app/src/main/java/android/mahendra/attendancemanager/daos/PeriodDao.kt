package android.mahendra.attendancemanager.daos

import android.mahendra.attendancemanager.models.Period

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlin.collections.List

@Dao
interface PeriodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(period: Period)

    @Update
    suspend fun update(period: Period)

    @Delete
    suspend fun delete(period: Period)

    @Query("Delete from period_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM period_table")
    fun getAllPeriods(): LiveData<List<Period>>

    @Query("SELECT * FROM PERIOD_TABLE WHERE week_day=:weekDay ORDER BY period_number")
    fun getAllPeriodsOn(weekDay: Int): LiveData<List<Period>>

    @Query("SELECT * FROM PERIOD_TABLE WHERE subject_title=:subjectTitle")
    fun getAllPeriodsOfSubject(subjectTitle: String): LiveData<List<Period>>

    @Query("DELETE FROM PERIOD_TABLE WHERE week_day=:weekDay AND period_number=:periodNumber")
    suspend fun deletePeriod(periodNumber: Int, weekDay: Int)
}
