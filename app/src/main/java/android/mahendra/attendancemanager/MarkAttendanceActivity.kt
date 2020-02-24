package android.mahendra.attendancemanager

import android.content.Context
import android.content.Intent
import android.mahendra.attendancemanager.fragments.PeriodFragment.Companion.newInstance
import android.mahendra.attendancemanager.models.Period
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.viewmodels.PeriodListViewModel
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import java.util.*

class MarkAttendanceActivity : AppCompatActivity() {

    private var mWeekDay = 0
    private lateinit var mViewModel: PeriodListViewModel
    private lateinit var mPeriodViewPager: ViewPager2
    private lateinit var mPagerAdapter: PeriodPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWeekDay = intent.getIntExtra(EXTRA_WEEKDAY, 1)
        setContentView(R.layout.activity_mark_attendance)
        mPeriodViewPager = findViewById(R.id.period_viewpager)
        mPagerAdapter = PeriodPagerAdapter(this)
        mPeriodViewPager.adapter = mPagerAdapter
        mViewModel = ViewModelProvider(this).get(PeriodListViewModel::class.java)
        mViewModel.getAllSubjectsOn(mWeekDay).observe(this, Observer { subjects: List<Subject> ->
            for ((title) in subjects) {
                Log.i(TAG, "title -> $title")
            }
        })
        mViewModel.getAllPeriodsOn(mWeekDay).observe(this, Observer { periods: List<Period> ->
            for (period in periods) {
                Log.i(TAG, "period " + period.subjectTitle + "weekday " + period.weekDay)
            }
            mPagerAdapter.setPeriods(periods)
        })
    }

    private inner class PeriodPagerAdapter(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
        var mPeriods: List<Period> = ArrayList()
        override fun createFragment(position: Int): Fragment {
            return newInstance(mPeriods[position])
        }

        override fun getItemCount(): Int {
            return mPeriods.size
        }

        fun setPeriods(periods: List<Period>) {
            mPeriods = periods
            notifyDataSetChanged()
        }
    }

    companion object {
        private const val TAG = "MarkAttendaceActivity"
        const val EXTRA_WEEKDAY = "MarkAttendaceActivity.EXTRA_WEEKDAY"
        fun newIntent(context: Context?, weekDay: Int): Intent {
            val i = Intent(context, MarkAttendanceActivity::class.java)
            i.putExtra(EXTRA_WEEKDAY, weekDay)
            return i
        }
    }
}