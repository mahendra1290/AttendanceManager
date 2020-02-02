package android.mahendra.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.mahendra.attendancemanager.models.Period;
import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.viewmodels.PeriodListViewModel;
import android.mahendra.attendancemanager.viewmodels.SubjectListViewModel;
import android.os.Bundle;
import android.util.AttributeSet;
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

import java.util.ArrayList;
import java.util.List;

public class TimeTableActivity extends AppCompatActivity implements
        DayScheduleFragment.AddPeriodCallback,
        AddPeriodDialogFragment.PeriodCallback,
        AddPeriodDialogFragment.SubjectCallback {

    private ViewPager2 mViewPager;
    private SubjectListViewModel mSubjectListViewModel;
    private PeriodListViewModel mPeriodListViewModel;
    private List<Integer> mWeekDays;
    private List<Subject> mSubjects;

    private int weekDay = -1;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, TimeTableActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        mSubjectListViewModel = new ViewModelProvider(this).get(SubjectListViewModel.class);
        mSubjectListViewModel.getAllSubjects().observe(this, subjects -> {
            mSubjects = subjects;
        });
        mPeriodListViewModel = new ViewModelProvider(this).get(PeriodListViewModel.class);
        mViewPager = findViewById(R.id.day_schedule_viewpager);
        mViewPager.setAdapter(new DayScheduleAdapter(this));
//        mViewPager.setCurrentItem(0);
    }

    private class DayScheduleAdapter extends FragmentStateAdapter {
        List<Integer> weekDays = new ArrayList<>();

        public DayScheduleAdapter(FragmentActivity activity) {
            super(activity);
            for (int i = 0; i < 7; i++) {
                weekDays.add(i + 1);
            }
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
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
        this.weekDay = weekDay;
    }

    @Override
    public List<Subject> getSubjects() {
        return mSubjects;
    }

    @Override
    public void onNewPeriod(int periodNumber, String periodTitle) {
        mPeriodListViewModel.insert(new Period(periodTitle, periodNumber, weekDay));
    }
}
