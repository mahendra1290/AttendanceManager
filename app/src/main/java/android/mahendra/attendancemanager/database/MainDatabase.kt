package android.mahendra.attendancemanager.database

import android.content.Context
import android.mahendra.attendancemanager.daos.PeriodDao
import android.mahendra.attendancemanager.daos.SubjectDao
import android.mahendra.attendancemanager.daos.SubjectPeriodDao
import android.mahendra.attendancemanager.models.Period
import android.mahendra.attendancemanager.models.Subject
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subject::class, Period::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun mPeriodDao(): PeriodDao
    abstract fun mSubjectPeriodDao(): SubjectPeriodDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                        MainDatabase::class.java, "main_database")
                        .build()
            }
        }
    }
}