package android.mahendra.attendancemanager

import android.content.Context
import android.content.Intent
import android.mahendra.attendancemanager.dialogs.PeriodDialogFragment
import android.mahendra.attendancemanager.fragments.DayScheduleFragment
import android.mahendra.attendancemanager.models.Period
import android.mahendra.attendancemanager.viewmodels.PeriodListViewModel
import android.mahendra.attendancemanager.viewmodels.SubjectViewModel
import android.os.Bundle
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_timetable.*

import java.util.Calendar
import kotlin.collections.ArrayList
import kotlin.collections.List

class TimeTableActivity : AppCompatActivity(),
        DayScheduleFragment.Callbacks, PeriodDialogFragment.Callbacks {
    private lateinit var mViewPager: ViewPager2

    private lateinit var mSubjectViewModel: SubjectViewModel
    private lateinit var mPeriodListViewModel: PeriodListViewModel

    private lateinit var mSubjectsTitles: List<String>

    override val subjectTitles: List<String>
        get() = mSubjectsTitles

    private var mWeekDay = -1
    private var mWeekDayOffSet = -1

    private var mTempPeriod: Period? = null
    private var addNewPeriod: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)
        createWeekDayHash()
        mWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        mSubjectViewModel = ViewModelProvider(this).get(SubjectViewModel::class.java)

        mSubjectViewModel.getSubjectTitles().observe(this,
                Observer { subjectTitles: List<String> -> mSubjectsTitles = subjectTitles })

        mPeriodListViewModel = ViewModelProvider(this).get(PeriodListViewModel::class.java)
        mViewPager = day_schedule_viewpager
        mViewPager.adapter = DayScheduleAdapter(this)
        mViewPager.currentItem = mWeekDay - mWeekDayOffSet
        val tabLayout: TabLayout = tab_layout_weekday
        TabLayoutMediator(tabLayout, mViewPager
        ) { tab, position -> tab.text = WEEK_DAYS.get(position + mWeekDayOffSet)}.attach()
    }

    private inner class DayScheduleAdapter(fragmentActivity: FragmentActivity)
        : FragmentStateAdapter(fragmentActivity) {

        var weekDays: MutableList<Int> = ArrayList()
        init {
            for (i in 2..7) {
                weekDays.add(i)
            }
            mWeekDayOffSet = weekDays[0]
        }

        override fun createFragment(position: Int): Fragment {
            return DayScheduleFragment.newInstance(weekDays[position])
        }

        override fun getItemCount(): Int {
            return weekDays.size
        }
    }

    private fun openAddPeriodDialog(periodTitle: String?) {
        val dialogFragment = PeriodDialogFragment.newInstance(periodTitle)
        val fm = supportFragmentManager
        dialogFragment.show(fm, "period")
    }

    override fun onAddPeriod(periodNumber: Int, weekDay: Int) {
        addNewPeriod = true
        mTempPeriod = Period(null, periodNumber, weekDay)
        openAddPeriodDialog(mTempPeriod!!.subjectTitle)
    }

    override fun onModifyPeriod(period: Period) {
        addNewPeriod = false
        mTempPeriod = period
        openAddPeriodDialog(period.subjectTitle)
    }

    override fun onDeletePeriod(title: String?) {
        mPeriodListViewModel.deletePeriod(mTempPeriod!!.periodNumber, mTempPeriod!!.weekDay)
    }

    override fun onPeriodSelected(title: String?) {
        mTempPeriod!!.subjectTitle = title
        if (addNewPeriod) {
            mPeriodListViewModel.insert(mTempPeriod!!)
        }
        else {
            mPeriodListViewModel.update(mTempPeriod!!)
        }
        mTempPeriod = null
        addNewPeriod = false
    }

    companion object {
        private const val TAG = "TimeTableActivity"
        lateinit var WEEK_DAYS: SparseArray<String>

        fun newIntent(context: Context): Intent {
            return Intent(context, TimeTableActivity::class.java)
        }

        private fun createWeekDayHash() {
            WEEK_DAYS = SparseArray()
            WEEK_DAYS.put(1, "sunday")
            WEEK_DAYS.put(2, "monday")
            WEEK_DAYS.put(3, "tuesday")
            WEEK_DAYS.put(4, "wednesday")
            WEEK_DAYS.put(5, "thursday")
            WEEK_DAYS.put(6, "friday")
            WEEK_DAYS.put(7, "saturday")
        }
    }
}
