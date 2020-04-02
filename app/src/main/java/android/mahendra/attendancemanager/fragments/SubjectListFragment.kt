package android.mahendra.attendancemanager.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.mahendra.attendancemanager.EventObserver
import android.mahendra.attendancemanager.MarkAttendanceActivity
import android.mahendra.attendancemanager.R
import android.mahendra.attendancemanager.TimeTableActivity
import android.mahendra.attendancemanager.adapters.SubjectAdapter
import android.mahendra.attendancemanager.adapters.SubjectOptionClickListener
import android.mahendra.attendancemanager.databinding.FragmentSubjectListBinding
import android.mahendra.attendancemanager.dialogs.*
import android.mahendra.attendancemanager.models.Subject
import android.mahendra.attendancemanager.utilities.InjectorUtils
import android.mahendra.attendancemanager.viewmodels.subject.SubjectViewModel
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import timber.log.Timber
import java.util.*

class SubjectListFragment : Fragment(), SubjectOptionBottomSheetDialog.OptionClickListener {

    private val subjectViewModel: SubjectViewModel by viewModels {
        InjectorUtils.provideSubjectListViewModelFactory(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSubjectListBinding>(
                inflater, R.layout.fragment_subject_list, container, false
        )

        val adapter = SubjectAdapter(SubjectOptionClickListener { subjectTitle ->
            subjectViewModel.onOptionSelected(subjectTitle)
        })

        subjectViewModel.allSubjects.observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
            Timber.i("\n new list submitted \n$subjects")
            adapter.submitList(subjects)
        })

        subjectViewModel.openOptionDialog.observe(viewLifecycleOwner, EventObserver { subjectTitle ->
            openSubjectOptionDialog(subjectTitle)
        })

        val itemAnimator= binding.subjectListRecyclerView.itemAnimator as SimpleItemAnimator
        itemAnimator.supportsChangeAnimations = false
        binding.subjectListRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.subjectListRecyclerView.adapter = adapter
        binding.addSubjectFloatingButton.setOnClickListener { openAddSubjectDialog() }
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
        when (requestCode) {
            REQUEST_NEW_SUBJECT -> handleNewSubjectRequest(data!!)
            REQUEST_SUBJECT_TITLE_EDIT -> handleSubjectTitleEditRequest(data!!)
            REQUEST_EDIT_ATTENDANCE -> handleSubjectAttendanceEditRequest(data!!)
        }
    }

    private fun handleNewSubjectRequest(data: Intent) {
        val subjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_SUBJECT_TITLE)!!
        subjectViewModel.onNewSubject(subjectTitle)
    }

    private fun handleSubjectTitleEditRequest(data: Intent) {
        val oldSubjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_OLD_SUBJECT_TITLE)!!
        val newSubjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_SUBJECT_TITLE)!!
        subjectViewModel.onUpdateTitle(oldSubjectTitle, newSubjectTitle)
    }

    private fun handleSubjectAttendanceEditRequest(data: Intent) {
        val subjectTitle = data.getStringExtra(AttendanceEditDialogFragment.EXTRA_SUBJECT_TITLE)!!
        val attendedClasses = data.getIntExtra(AttendanceEditDialogFragment.EXTRA_ATTENDED_CLASSES, 0)
        val totalClasses = data.getIntExtra(AttendanceEditDialogFragment.EXTRA_TOTAL_CLASSES, 0)
        subjectViewModel.onUpdateAttendance(subjectTitle, attendedClasses, totalClasses)
    }

    private fun openAddSubjectDialog() {
        val dialogFragment = SubjectTitleEditDialogFragment.newInstance(null)
        dialogFragment.setTargetFragment(this@SubjectListFragment, REQUEST_NEW_SUBJECT)
        dialogFragment.show(parentFragmentManager, "subject")
    }

    private fun openSubjectOptionDialog(title: String) {
        val dialog = SubjectOptionBottomSheetDialog.newInstance(title)
        dialog.setTargetFragment(this, 0)
        dialog.show(parentFragmentManager, "option")
    }

    override fun onDelete(subjectTitle: String) {
        val listener = ConfirmationDialogListener(
                onPositiveClick = {
                    subjectViewModel.delete(subjectTitle)
                }
        )
        val dialog = DeleteSubjectConfirmationDialog.newInstance(subjectTitle, listener)
        dialog.show(parentFragmentManager, "delete subject")
    }

    override fun onEditTitle(subjectTitle: String) {
        SubjectTitleEditDialogFragment.newInstance(subjectTitle).also {
            it.setTargetFragment(this, REQUEST_SUBJECT_TITLE_EDIT)
            it.show(parentFragmentManager, "subject")
        }
    }

    override fun onEditAttendance(subjectTitle: String) {
        val subject = subjectViewModel.getSubject(subjectTitle)
        val dialogFragment = AttendanceEditDialogFragment.newInstance(
                subjectTitle = subjectTitle,
                attendedClasses = subject!!.attendedClasses,
                totalClasses = subject.totalClasses
        )
        dialogFragment.setTargetFragment(this, REQUEST_EDIT_ATTENDANCE)
        dialogFragment.show(parentFragmentManager, "attendance")
    }

    override fun onResetAttendance(subjectTitle: String) {
        val listener = ConfirmationDialogListener(
                onPositiveClick = {
                    subjectViewModel.onResetAttendance(subjectTitle)
                }
        )
        val dialog = ResetAttendanceConfirmationDialog.newInstance(subjectTitle, listener)
        dialog.show(parentFragmentManager, "reset subject attendance")
    }

    private fun showSubjectDeleteToast(subjectTitle: String) {
        Toast.makeText(activity,
                "successfully deleted $subjectTitle", Toast.LENGTH_SHORT).show()
    }

    private fun showResetAttendanceToast(subjectTitle: String) {
        Toast.makeText(activity,
                "attendance reset $subjectTitle", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_NEW_SUBJECT = 0
        private const val REQUEST_SUBJECT_TITLE_EDIT = 1
        private const val REQUEST_EDIT_ATTENDANCE = 4

        fun newInstance(): SubjectListFragment {
            return SubjectListFragment()
        }
    }
}