package android.mahendra.attendancemanager

import android.mahendra.attendancemanager.fragments.SubjectListFragment
import androidx.fragment.app.Fragment

class MainActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment? {
        return SubjectListFragment.newInstance()
    }
}