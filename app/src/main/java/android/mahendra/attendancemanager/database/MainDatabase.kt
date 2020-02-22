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
import java.util.concurrent.Executors

@Database(entities = [Subject::class, Period::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun mPeriodDao(): PeriodDao
    abstract fun mSubjectPeriodDao(): SubjectPeriodDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        @JvmField
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        @JvmStatic
        fun getDatabase(context: Context): MainDatabase? {
            if (INSTANCE == null) {
                synchronized(MainDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                MainDatabase::class.java, "main_database")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}