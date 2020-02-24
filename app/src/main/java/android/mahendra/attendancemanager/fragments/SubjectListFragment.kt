package android.mahendra.attendancemanager.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.mahendra.attendancemanager.MarkAttendanceActivity
import android.mahendra.attendancemanager.R
import android.mahendra.attendancemanager.TimeTableActivity
import android.mahendra.attendancemanager.databinding.FragmentSubjectListBinding
import android.mahendra.attendancemanager.databinding.ListItemSubjectBinding
import android.mahendra.attendancemanager.dialogs.AttendanceEditDialogFragment
import android.mahendra.attendancemanager.dialogs.ConfirmationDialogFragment
import android.mahendra.attendancemanager.dialogs.SubjectOptionBottomSheetDialog
import android.mahendra.attendancemanager.dialogs.SubjectOptionBottomSheetDialog.SubjectOptionListener
import android.mahendra.attendancemanager.dialogs.SubjectTitleEditDialogFragment
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.utilities.InjectorUtils
import android.mahendra.attendancemanager.viewmodels.SubjectDetailViewModel
import android.mahendra.attendancemanager.viewmodels.SubjectListViewModel
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SubjectListFragment : Fragment(), SubjectOptionListener {
    private val mSubjectListViewModel: SubjectListViewModel by viewModels {
        InjectorUtils.provideSubjectListViewModelFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSubjectListBinding>(
                inflater, R.layout.fragment_subject_list, container, false
        )
        val adapter = SubjectAdapter()
        mSubjectListViewModel.mAllSubjects.observe(
                viewLifecycleOwner, Observer {
            subjects: List<Subject> -> adapter.setSubjects(subjects)
            Log.i(TAG, "subject updated")
        })

        binding.subjectListRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.subjectListRecyclerView.adapter = adapter
        binding.addSubjectFloatingButton.setOnClickListener { v: View? -> openAddSubjectDialog() }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_subject_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_timetable -> {
                val intent = TimeTableActivity.newIntent(activity as Context)
                startActivity(intent)
                true
            }
            R.id.mark_attendance -> {
                val intent1 = MarkAttendanceActivity.newIntent(activity,
                        Calendar.getInstance()[Calendar.DAY_OF_WEEK])
                startActivity(intent1)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
                true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_NEW_SUBJECT) { // when a new subject is added
            val subjectTitle = data!!.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_SUBJECT_TITLE)
            val subject = Subject()
            subject.title = subjectTitle
            mSubjectListViewModel.insert(subject)
        }
        if (requestCode == REQUEST_SUBJECT_TITLE_EDIT) { // when a subject's title is changed
            val oldSubjectTitle = data!!.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_OLD_SUBJECT_TITLE)
            val newSubjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_SUBJECT_TITLE)
            mSubjectListViewModel.onUpdateTitle(oldSubjectTitle, newSubjectTitle)
        }
        if (requestCode == REQUEST_SUBJECT_DELETE) { // on subject delete confirmation
            val title = data!!.getStringExtra(ConfirmationDialogFragment.EXTRA_SUBJECT_TITLE)
            mSubjectListViewModel.onDeleteSubjectWith(title)
            showSubjectDeleteToast(title)
        }
        if (requestCode == REQUEST_RESET_ATTENDANCE) { // on reset attendance confirmation
            val title = data!!.getStringExtra(ConfirmationDialogFragment.EXTRA_SUBJECT_TITLE)
            mSubjectListViewModel.onResetAttendance(title)
            showResetAttendanceToast(title)
        }
        if (requestCode == REQUEST_EDIT_ATTENDANCE) {
            val title = data!!.getStringExtra(AttendanceEditDialogFragment.EXTRA_SUBJECT_TITLE)
            val attended = data.getIntExtra(AttendanceEditDialogFragment.EXTRA_ATTENDED, 0)
            val total = data.getIntExtra(AttendanceEditDialogFragment.EXTRA_TOTAL, 0)
            Log.i(TAG, "onActivityResult: attended " + attended + "total " + total)
            val subject = mSubjectListViewModel.getSubject(title)
            subject!!.attendedClasses = attended
            subject.missedClasses = total - attended
            mSubjectListViewModel.update(subject)
        }
    }

    /**
     * view holder for subject item with data binding
     */
    private inner class SubjectHolder(private val mBinding: ListItemSubjectBinding) : RecyclerView.ViewHolder(mBinding.root), View.OnClickListener {
        private var mSubject: Subject? = null
        fun bind(subject: Subject) {
            mSubject = subject
            mBinding.subjectDetailViewModel!!.subject = mSubject
            mBinding.invalidateAll()
            mBinding.notifyChange()
            mBinding.executePendingBindings()
        }

        override fun onClick(v: View) {
            openSubjectOptionDialog(mSubject!!.title)
        }

        init {
            val detailViewModel: SubjectDetailViewModel
                    = InjectorUtils.provideSubjectDetailViewModel(this@SubjectListFragment)
            mBinding.subjectDetailViewModel = detailViewModel
            mBinding.moreOptions.setOnClickListener(this)
        }
    }

    /**
     * subject list adapter which provides subject view holder to recycler view
     */
    private inner class SubjectAdapter : RecyclerView.Adapter<SubjectHolder>() {
        private var subjects = emptyList<Subject>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
            val inflater = layoutInflater
            val binding = DataBindingUtil.inflate<ListItemSubjectBinding>(
                    inflater, R.layout.list_item_subject, parent, false)
            return SubjectHolder(binding)
        }

        override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
            holder.bind(subjects[position])
        }

        override fun getItemCount(): Int {
            return subjects.size
        }

        internal fun setSubjects(subjects: List<Subject>) {
            this.subjects = subjects
            notifyDataSetChanged()
        }
    }

    /**
     * uses SubjectTitleEditDialogFragment for taking new subject title input from user
     * old title is passed as null, as there is no subject
     */
    private fun openAddSubjectDialog() {
        val dialogFragment = SubjectTitleEditDialogFragment.newInstance(null)
        dialogFragment.setTargetFragment(this@SubjectListFragment, REQUEST_NEW_SUBJECT)
        dialogFragment.show(parentFragmentManager, "subject")
    }

    /**
     * option dialog which will be opened when user clicks option menu icon on view holder
     * and the subject title is passed so that it can be used by listener to take actions
     * @param title subject title which is used by dialog, for listener methods
     */
    private fun openSubjectOptionDialog(title: String) {
        val dialog = SubjectOptionBottomSheetDialog.newInstance(title)
        dialog.setTargetFragment(this, 0)
        dialog.show(parentFragmentManager, "option")
    }

    /**
     * when delete option is selected from subjectOptionDialog
     * @param subjectTitle title of subject for which this option was selected
     */
    override fun onDeleteSelected(subjectTitle: String) {
        Log.i(TAG, "capitalize " + subjectTitle.capitalize())
        val dialogFragment = ConfirmationDialogFragment.newInstance(
                subjectTitle, getString(R.string.delete_subject, subjectTitle.capitalize()),
                getString(R.string.warning_subject_delete),
                "cancel", "delete")
        dialogFragment.setTargetFragment(this, REQUEST_SUBJECT_DELETE)
        dialogFragment.show(parentFragmentManager, "confirmation delete subject")
    }

    /**
     * when edit title option in selected from subjectOptionDialog
     * @param subjectTitle title of subject for which this option was selected
     */
    override fun onEditTitleSelected(subjectTitle: String) {
        val dialogFragment = SubjectTitleEditDialogFragment.newInstance(subjectTitle)
        dialogFragment.setTargetFragment(this@SubjectListFragment, REQUEST_SUBJECT_TITLE_EDIT)
        dialogFragment.show(parentFragmentManager, "subject")
    }

    /**
     * when edit attendance option in selected from subjectOptionDialog
     * @param subjectTitle title of subject for which this option was selected
     */
    override fun onEditAttendanceSelected(subjectTitle: String) {
        val subject = mSubjectListViewModel.getSubject(subjectTitle)
        val dialogFragment = AttendanceEditDialogFragment.newInstance(
                subjectTitle,
                subject!!.attendedClasses, subject.totalClasses
        )
        dialogFragment.setTargetFragment(this, REQUEST_EDIT_ATTENDANCE)
        dialogFragment.show(parentFragmentManager, "attendance")
    }

    /**
     * when reset attendance option is selected from subjectOptionDialog
     * @param subjectTitle title of subject for which this option was selected
     */
    override fun onResetAttendanceSelected(subjectTitle: String) {
        val dialogFragment = ConfirmationDialogFragment.newInstance(subjectTitle, getString(R.string.reset_attendance),
                getString(R.string.warning_reset_attendance, subjectTitle),
                "cancel", "reset")
        dialogFragment.setTargetFragment(this, REQUEST_RESET_ATTENDANCE)
        dialogFragment.show(parentFragmentManager, "confirmation reset attendance")
    }

    /**
     * toast shown when subject is deleted
     * @param subjectTitle used in message
     */
    private fun showSubjectDeleteToast(subjectTitle: String) {
        Toast.makeText(activity,
                "successfully deleted $subjectTitle", Toast.LENGTH_SHORT).show()
    }

    /**
     * toast shown when subject attendance is reset
     * @param subjectTitle used in message
     */
    private fun showResetAttendanceToast(subjectTitle: String) {
        Toast.makeText(activity,
                "attendance reset $subjectTitle", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "SubjectListFragment"
        private const val REQUEST_NEW_SUBJECT = 0
        private const val REQUEST_SUBJECT_TITLE_EDIT = 1
        private const val REQUEST_SUBJECT_DELETE = 2
        private const val REQUEST_RESET_ATTENDANCE = 3
        private const val REQUEST_EDIT_ATTENDANCE = 4
        fun newInstance(): SubjectListFragment {
            return SubjectListFragment()
        }
    }
}