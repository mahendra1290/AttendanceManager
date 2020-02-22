package android.mahendra.attendancemanager.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.mahendra.attendancemanager.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class AttendanceEditDialogFragment : DialogFragment() {
    private lateinit var attendedTextView: EditText
    private lateinit var totalTextView: EditText
    private lateinit var mDialog: AlertDialog

    override fun onStart() {
        super.onStart()
        updatePositiveButton()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_edit_attendance, null)
        val builder = AlertDialog.Builder(activity)
                .setView(view)
                .setTitle("Edit Attendance ")
                .setPositiveButton("Edit") { dialog: DialogInterface?, which: Int -> sendResult(Activity.RESULT_OK) }
                .setNegativeButton("Cancel", null)
        mDialog = builder.create()
        attendedTextView = view.findViewById(R.id.editText_attended)
        totalTextView = view.findViewById(R.id.editText_total)
        attendedTextView.setText(arguments!!.getInt(ARG_ATTENDED_CLASSES).toString())
        totalTextView.setText(arguments!!.getInt(ARG_TOTAL_CLASSES).toString())
        attendedTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updatePositiveButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        totalTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updatePositiveButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        return mDialog
    }

    private fun updatePositiveButton() {
        Log.i(TAG, "updatePositiveButton: $mDialog")
        val attended: Int
        val total: Int
        try {
            attended = Integer.valueOf(attendedTextView.text.toString())
            total = Integer.valueOf(totalTextView.text.toString())
        } catch (ex: NumberFormatException) {
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
            return
        }
        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = total >= attended
    }

    private fun sendResult(resultCode: Int) {
        if (targetFragment == null) {
            Log.i(TAG, "sendResult: int attendance edit")
            return
        }
        val attended = Integer.valueOf(attendedTextView.text.toString())
        val total = Integer.valueOf(totalTextView.text.toString())
        val i = Intent()
        i.putExtra(EXTRA_ATTENDED, attended)
        i.putExtra(EXTRA_TOTAL, total)
        i.putExtra(EXTRA_SUBJECT_TITLE, arguments!!.getString(ARG_SUBJECT_TITLE))
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, i)
    }

    companion object {
        private const val TAG = "AttendanceEditDialogFra"
        private const val ARG_SUBJECT_TITLE = "subject_title"
        private const val ARG_ATTENDED_CLASSES = "com.android.mahendra.attendancemanager.subject.attendedClasses"
        private const val ARG_TOTAL_CLASSES = "com.android.mahendra.attendancemanager.subject.totalClasses"
        const val EXTRA_SUBJECT_TITLE = "subject_title"
        const val EXTRA_ATTENDED = "attended"
        const val EXTRA_TOTAL = "total"
        fun newInstance(subjectTitle: String?, attendedClasses: Int, totalClasses: Int): AttendanceEditDialogFragment {
            val args = Bundle()
            args.putInt(ARG_ATTENDED_CLASSES, attendedClasses)
            args.putInt(ARG_TOTAL_CLASSES, totalClasses)
            args.putString(ARG_SUBJECT_TITLE, subjectTitle)
            val fragment = AttendanceEditDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}