package android.mahendra.attendancemanager

import androidx.fragment.app.Fragment

class MainActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment? {
        return SubjectListFragment.newInstance()
    }
}