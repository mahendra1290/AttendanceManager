package android.mahendra.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.mahendra.attendancemanager.models.Period;
import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.viewmodels.PeriodListViewModel;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MarkAttendaceActivity extends AppCompatActivity {
    private static final String TAG = "MarkAttendaceActivity";

    public static final String EXTRA_WEEKDAY =
            "MarkAttendaceActivity.EXTRA_WEEKDAY";

    private int mWeekDay;
    private PeriodListViewModel mViewModel;
    private ViewPager2 mPeriodViewPager;
    private PeriodPagerAdapter mPagerAdapter;

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
        mPeriodViewPager = findViewById(R.id.period_viewpager);
        mPagerAdapter = new PeriodPagerAdapter(this);
        mPeriodViewPager.setAdapter(mPagerAdapter);
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
            mPagerAdapter.setPeriods(periods);
        });
    }

    private class PeriodPagerAdapter extends FragmentStateAdapter {
        List<Period> mPeriods = new ArrayList<>();

        public PeriodPagerAdapter(FragmentActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return PeriodFragment.newInstance(mPeriods.get(position));
        }

        @Override
        public int getItemCount() {
            return mPeriods.size();
        }

        public void setPeriods(List<Period> periods) {
            mPeriods = periods;
            notifyDataSetChanged();
        }
    }
}
