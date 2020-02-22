package android.mahendra.attendancemanager

import android.mahendra.attendancemanager.models.Period
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PeriodFragment(private val mPeriod: Period) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_attendance, container, false)
        val mTitle = v.findViewById<TextView>(R.id.period_title)
        val mPeriodNumber = v.findViewById<TextView>(R.id.period_number)
        mTitle.text = mPeriod.subjectTitle
        mPeriodNumber.text = mPeriod.periodNumber.toString()
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(period: Period): PeriodFragment {
            val args = Bundle()
            val fragment = PeriodFragment(period)
            fragment.arguments = args
            return fragment
        }
    }

}