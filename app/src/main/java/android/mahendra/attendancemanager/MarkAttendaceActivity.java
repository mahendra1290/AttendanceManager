package android.mahendra.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.mahendra.attendancemanager.models.Period;
import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.viewmodels.PeriodListViewModel;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MarkAttendaceActivity extends AppCompatActivity {
    private static final String TAG = "MarkAttendaceActivity";

    public static final String EXTRA_WEEKDAY =
            "MarkAttendaceActivity.EXTRA_WEEKDAY";

    private int mWeekDay;
    private PeriodListViewModel mViewModel;

    public static Intent newIntent(Context context, int weekDay) {
        Intent i = new Intent(context, MarkAttendaceActivity.class);
        i.putExtra(EXTRA_WEEKDAY, weekDay);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeekDay = getIntent().getIntExtra(EXTRA_WEEKDAY, 1);
        setContentView(R.layout.activity_mark_attendance);

        mViewModel = new ViewModelProvider(this).get(PeriodListViewModel.class);
        mViewModel.getAllSubjectsOn(mWeekDay).observe(this, subjects -> {
            for (Subject subject : subjects) {
                Log.i(TAG, "title -> " + subject.getTitle());
            }
        });

        mViewModel.getAllPeriodsOn(mWeekDay).observe(this, periods -> {
            for (Period period : periods) {
                Log.i(TAG, "period " + period.getSubjectTitle() + "weekday " + period.getWeekDay());
            }
        });
    }
}
