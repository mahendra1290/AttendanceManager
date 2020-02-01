package android.mahendra.attendancemanager.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "period_table")
public class Period {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long periodId;

    @ColumnInfo(name = "period_number")
    private int periodNumber;

    @ColumnInfo(name = "week_day")
    private int weekDay;

    @ColumnInfo(name = "subject_title")
    private String subjectTitle;

    public Period(String subjectTitle, int periodNumber, int weekDay) {
        this.periodNumber = periodNumber;
        this.subjectTitle = subjectTitle;
        this.weekDay = weekDay;
    }

    public void setPeriodId(long periodId) {
        this.periodId = periodId;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }
}
