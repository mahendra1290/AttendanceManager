package android.mahendra.attendancemanager.viewmodels;

import android.mahendra.attendancemanager.models.Period;

import androidx.databinding.BaseObservable;

public class PeriodViewModel extends BaseObservable {
    private Period mPeriod;

    public void setPeriod(Period period) {
        mPeriod = period;
    }

    public String getTitle() {
        if (mPeriod == null) {
            return "-";
        }
        return mPeriod.getSubjectTitle();
    }

    public String getPeriodNumber() {
        if (mPeriod == null) {
            return "-";
        }
        return String.valueOf(mPeriod.getPeriodNumber());
    }
}
