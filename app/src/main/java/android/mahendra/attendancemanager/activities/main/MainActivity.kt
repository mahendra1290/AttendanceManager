package android.mahendra.attendancemanager.activities.main

import android.mahendra.attendancemanager.activities.SingleFragmentActivity
import android.mahendra.attendancemanager.activities.main.fragments.SubjectListFragment
import androidx.fragment.app.Fragment

class MainActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment? {
        return SubjectListFragment.newInstance()
    }
}