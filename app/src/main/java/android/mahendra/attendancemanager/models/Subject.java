package android.mahendra.attendancemanager.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subject_table")
public class Subject {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "classes_attended")
    private int mAttendedClasses;

    @ColumnInfo(name = "classes_missed")
    private int mMissedClasses;

    @ColumnInfo(name = "classes_cancelled")
    private int mCancelledClasses;

    @Ignore
    public Subject() {

    }

    public Subject(String title) {
        mTitle = title;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getAttendedClasses() {
        return mAttendedClasses;
    }

    public void setAttendedClasses(int attendedClasses) {
        mAttendedClasses = attendedClasses;
    }

    public int getMissedClasses() {
        return mMissedClasses;
    }

    public void setMissedClasses(int missedClasses) {
        mMissedClasses = missedClasses;
    }

    public int getCancelledClasses() {
        return mCancelledClasses;
    }

    public void setCancelledClasses(int cancelledClasses) {
        mCancelledClasses = cancelledClasses;
    }

    public int getTotalClasses() {
        return mAttendedClasses + mMissedClasses;
    }

    public void incrementClassesAttended() {
        mAttendedClasses += 1;

    }

    public void incrementClassesMissed() {
        mMissedClasses += 1;

    }

    public void incrementClassesCancelled() {
        mCancelledClasses += 1;
    }

    @NonNull
    @Override
    public String toString() {
        return "title " + getTitle() + "\n"
                + "attended " + mAttendedClasses + "\n"
                + "missed " + mMissedClasses + "\n"
                + "cancelled " + mMissedClasses + "\n";
    }
}
