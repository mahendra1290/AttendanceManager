package android.mahendra.attendancemanager.viewmodels;

import android.mahendra.attendancemanager.models.Subject;
import android.util.Log;

import androidx.databinding.BaseObservable;

import java.util.Locale;

public class SubjectViewModel extends BaseObservable {
    private static final String TAG = "SubjectViewModel";
    private Subject mSubject;

    public void setSubject(Subject subject) {
        mSubject = subject;
    }

    public String getTitle() {
        return mSubject.getTitle();
    }

    public String getAttendanceStat() {
        String stat = String.
                format(Locale.ENGLISH,"%d / %d", mSubject.getAttendedClasses(), mSubject.getTotalClasses());
        return stat;
    }

    public String getAttendancePercentage() {
        if (mSubject.getTotalClasses() == 0) {
            return "0.0%";
        }
        double percentage = ((double) mSubject.getAttendedClasses() / mSubject.getTotalClasses()) * 100;
        return String.format(Locale.ENGLISH, "%.1f%%", percentage);
    }

    public int getAttendanceProgress() {
        if (mSubject.getTotalClasses() == 0) {
            return 0;
        }
        return (mSubject.getAttendedClasses() * 100) / mSubject.getTotalClasses();
    }

    public void onAttended() {
        mSubject.incrementClassesAttended();
    }

    public void onMissed() {
        mSubject.incrementClassesMissed();
    }

    public void onCancelled() {
        mSubject.incrementClassesCancelled();
    }
}
