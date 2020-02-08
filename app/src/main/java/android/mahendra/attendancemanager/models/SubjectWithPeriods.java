package android.mahendra.attendancemanager.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SubjectWithPeriods {
    @Embedded
    private Subject mSubject;

    @Relation(
            parentColumn = "title",
            entityColumn = "subject_title"
    )
    private List<Period> mPeriods;

    public Subject getSubject() {
        return mSubject;
    }

    public void setSubject(Subject subject) {
        mSubject = subject;
    }

    public List<Period> getPeriods() {
        return mPeriods;
    }

    public void setPeriods(List<Period> periods) {
        mPeriods = periods;
    }
}
