package android.mahendra.attendancemanager.repositories;

import android.app.Application;
import android.mahendra.attendancemanager.daos.PeriodDao;
import android.mahendra.attendancemanager.database.MainDatabase;
import android.mahendra.attendancemanager.models.Period;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PeriodRepository {
    private PeriodDao mPeriodDao;
    private LiveData<List<Period>> mAllPeriods;

    public PeriodRepository(Application application) {
        MainDatabase db = MainDatabase.getDatabase(application);
        mPeriodDao = db.mPeriodDao();
        mAllPeriods = mPeriodDao.getAllPeriods();
    }

    public void insert(Period period) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            mPeriodDao.insert(period);
        });
    }

    public void update(Period period) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            mPeriodDao.update(period);
        });
    }

    public void delete(Period period) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            mPeriodDao.delete(period);
        });
    }

    public LiveData<List<Period>> getAllPeriods() {
        return mAllPeriods;
    }

    public LiveData<List<Period>> getAllPeriodsOn(int weekDay) {
        return mPeriodDao.getAllPeriodsOn(weekDay);
    }

    public LiveData<List<Period>> getAllPeriodsOfSubject(String subjectTitle) {
        return mPeriodDao.getAllPeriodsOfSubject(subjectTitle);
    }
}
