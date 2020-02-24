package android.mahendra.attendancemanager.dialogs

import android.content.Context
import android.mahendra.attendancemanager.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SubjectOptionBottomSheetDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private var mSubjectOptionListener: SubjectOptionListener? = null

    interface SubjectOptionListener {
        fun onDeleteSelected(subjectTitle: String)
        fun onEditTitleSelected(subjectTitle: String)
        fun onEditAttendanceSelected(subjectTitle: String)
        fun onResetAttendanceSelected(subjectTitle: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mSubjectOptionListener = try {
            targetFragment as SubjectOptionListener?
        } catch (ex: ClassCastException) {
            throw ClassCastException(context.toString()
                    + "must implement SubjectOptionBottomSheetDialog.SubjectOptionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mSubjectOptionListener = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_subject, container, false)
        val title = view.findViewById<TextView>(R.id.subject_title_modal_sheet)
        title.text = arguments!!.getString(ARG_SUBJECT_TITLE, " ")
        view.findViewById<View>(R.id.delete_subject_option).setOnClickListener(this)
        view.findViewById<View>(R.id.edit_title_option).setOnClickListener(this)
        view.findViewById<View>(R.id.edit_attendance_option).setOnClickListener(this)
        view.findViewById<View>(R.id.reset_attendance_option).setOnClickListener(this)
        return view
    }

    override fun onClick(v: View) {
        val subjectTitle = arguments!!.getString(ARG_SUBJECT_TITLE, "")
        when (v.id) {
            R.id.delete_subject_option -> mSubjectOptionListener!!.onDeleteSelected(subjectTitle)
            R.id.edit_title_option -> mSubjectOptionListener!!.onEditTitleSelected(subjectTitle)
            R.id.edit_attendance_option -> mSubjectOptionListener!!.onEditAttendanceSelected(subjectTitle)
            R.id.reset_attendance_option -> mSubjectOptionListener!!.onResetAttendanceSelected(subjectTitle)
        }
        dismiss()
    }

    companion object {
        private const val ARG_SUBJECT_TITLE = "subject title"
        fun newInstance(title: String): SubjectOptionBottomSheetDialog {
            val args = Bundle()
            args.putString(ARG_SUBJECT_TITLE, title)
            val fragment = SubjectOptionBottomSheetDialog()
            fragment.arguments = args
            return fragment
        }
    }
}