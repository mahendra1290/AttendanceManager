package android.mahendra.attendancemanager

import android.content.Context
import android.content.Intent
import android.mahendra.attendancemanager.databinding.ActivityMarkAttendanceBinding
import android.mahendra.attendancemanager.fragments.PeriodFragment.Companion.newInstance
import android.mahendra.attendancemanager.models.Period
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.viewmodels.PeriodListViewModel
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import java.util.*

class MarkAttendanceActivity : AppCompatActivity() {

    private var weekDay = 0
    private lateinit var periodListViewModel: PeriodListViewModel
    private lateinit var periodViewPager: ViewPager2
    private lateinit var pagerAdapter: PeriodPagerAdapter
    private lateinit var binding: ActivityMarkAttendanceBinding
    private val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mark_attendance)

        periodViewPager = binding.periodViewpager
        pagerAdapter = PeriodPagerAdapter(this)
        periodViewPager.adapter = pagerAdapter
        periodListViewModel = ViewModelProvider(this).get(PeriodListViewModel::class.java)
        periodListViewModel.getAllSubjectsOn(weekDay).observe(this, Observer { subjects: List<Subject> ->
            for ((title) in subjects) {
                Log.i(TAG, "title -> $title")
            }
        })
        periodListViewModel.getAllPeriodsOn(weekDay).observe(this, Observer { periods: List<Period> ->
            for (period in periods) {
                Log.i(TAG, "period " + period.subjectTitle + "weekday " + period.weekDay)
            }
            pagerAdapter.setPeriods(periods)
        })
    }

    private inner class PeriodPagerAdapter(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
        private var periods: List<Period> = ArrayList()
        override fun createFragment(position: Int): Fragment {
            return newInstance(periods[position])
        }

        override fun getItemCount(): Int {
            return periods.size
        }

        fun setPeriods(periods: List<Period>) {
            this.periods = periods
            notifyDataSetChanged()
        }
    }

    companion object {
        private const val TAG = "MarkAttendanceActivity"
        private const val EXTRA_WEEKDAY = "MarkAttendanceActivity.EXTRA_WEEKDAY"
        fun newIntent(context: Context?, weekDay: Int): Intent {
            val i = Intent(context, MarkAttendanceActivity::class.java)
            i.putExtra(EXTRA_WEEKDAY, weekDay)
            return i
        }
    }
}