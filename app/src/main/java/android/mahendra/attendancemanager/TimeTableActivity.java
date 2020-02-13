package android.mahendra.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.mahendra.attendancemanager.models.Period;
import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.viewmodels.PeriodListViewModel;
import android.mahendra.attendancemanager.viewmodels.SubjectListViewModel;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TimeTableActivity extends AppCompatActivity implements
        DayScheduleFragment.AddPeriodCallback,
        AddPeriodDialogFragment.PeriodCallback,
        AddPeriodDialogFragment.SubjectCallback {
    private static final String TAG = "TimeTableActivity";

    public static SparseArray<String> WEEK_DAYS;

    private ViewPager2 mViewPager;

    private SubjectListViewModel mSubjectListViewModel;
    private PeriodListViewModel mPeriodListViewModel;

    private List<Integer> mWeekDays;
    private List<Subject> mSubjects;

    private int mWeekDay = -1;
    private int mWeekDayOffSet = -1;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, TimeTableActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        createWeekDayHash();
        mWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        mSubjectListViewModel = new ViewModelProvider(this).get(SubjectListViewModel.class);
        mSubjectListViewModel.getAllSubjects().observe(this, subjects -> {
            mSubjects = subjects;
        });

        mPeriodListViewModel = new ViewModelProvider(this).get(PeriodListViewModel.class);
        mViewPager = findViewById(R.id.day_schedule_viewpager);
        mViewPager.setAdapter(new DayScheduleAdapter(this));
        mViewPager.setCurrentItem(mWeekDay - mWeekDayOffSet);
        TabLayout tabLayout = findViewById(R.id.tab_layout_weekday);
        new TabLayoutMediator(tabLayout, mViewPager,
                (tab, position) -> tab.setText(WEEK_DAYS.get(position + mWeekDayOffSet))
         ).attach();
    }

    private class DayScheduleAdapter extends FragmentStateAdapter {
        List<Integer> weekDays = new ArrayList<>();

        public DayScheduleAdapter(FragmentActivity activity) {
            super(activity);
            for (int i = 1; i < 7; i++) {
                weekDays.add(i + 1);
            }
            mWeekDayOffSet = weekDays.get(0);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.i(TAG, "createFragment: " + position);
            return DayScheduleFragment.newInstance(weekDays.get(position));
        }

        @Override
        public int getItemCount() {
            return weekDays.size();
        }
    }

    public void openAddPeriodDialog(int periodNumber) {
        AddPeriodDialogFragment dialogFragment = AddPeriodDialogFragment.newInstance(periodNumber);
        FragmentManager fm = getSupportFragmentManager();
        dialogFragment.show(fm, "period");
    }

    @Override
    public void addPeriod(int periodNumber, int weekDay) {
        openAddPeriodDialog(periodNumber);
        this.mWeekDay = weekDay;
    }

    @Override
    public List<Subject> getSubjects() {
        return mSubjects;
    }

    @Override
    public void onNewPeriod(int periodNumber, String periodTitle) {
        mPeriodListViewModel.insert(new Period(periodTitle, periodNumber, mWeekDay));
    }

    private void createWeekDayHash() {
        WEEK_DAYS = new SparseArray<>();
        WEEK_DAYS.put(1, "sunday");
        WEEK_DAYS.put(2, "monday");
        WEEK_DAYS.put(3, "tuesday");
        WEEK_DAYS.put(4, "wednesday");
        WEEK_DAYS.put(5, "thursday");
        WEEK_DAYS.put(6, "friday");
        WEEK_DAYS.put(7, "saturday");
    }
}
