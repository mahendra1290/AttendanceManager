package android.mahendra.attendancemanager.viewmodels;

import android.app.Application;
import android.mahendra.attendancemanager.models.Period;
import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.repositories.PeriodRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PeriodListViewModel extends AndroidViewModel {
    private PeriodRepository mPeriodRepository;
    private LiveData<List<Period>> mAllPeriods;

    public PeriodListViewModel(@NonNull Application application) {
        super(application);
        mPeriodRepository = new PeriodRepository(application);
        mAllPeriods = mPeriodRepository.getAllPeriods();
    }

    public void insert(Period period) {
        mPeriodRepository.insert(period);
    }

    public void update(Period period) {
        mPeriodRepository.update(period);
    }

    public LiveData<List<Period>> getAllPeriods() {
        return mAllPeriods;
    }

    public LiveData<List<Period>> getAllPeriodsOn(int weekDay) {
        return mPeriodRepository.getAllPeriodsOn(weekDay);
    }

    public LiveData<List<Subject>> getAllSubjectsOn(int weekDay) {
        return mPeriodRepository.getAllSubjectsOn(weekDay);
    }
}
