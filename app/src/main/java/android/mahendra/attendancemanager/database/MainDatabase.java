package android.mahendra.attendancemanager.database;

import android.content.Context;
import android.mahendra.attendancemanager.Daos.PeriodDao;
import android.mahendra.attendancemanager.Daos.SubjectDao;
import android.mahendra.attendancemanager.models.Period;
import android.mahendra.attendancemanager.models.Subject;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Subject.class, Period.class}, version = 1, exportSchema = false)
public abstract class MainDatabase extends RoomDatabase {

    public abstract SubjectDao subjectDao();

    public abstract PeriodDao mPeriodDao();

    private static volatile MainDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MainDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MainDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MainDatabase.class, "main_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}