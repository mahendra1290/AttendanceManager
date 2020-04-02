package android.mahendra.attendancemanager.dialogs

import android.content.DialogInterface
import android.mahendra.attendancemanager.R
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ResetAttendanceConfirmationDialog(
        confirmationDialogListener: ConfirmationDialogListener
): ConfirmationDialogFragment(confirmationDialogListener) {
    override val dialogTitleRedId: Int
        get() = R.string.reset_attendance

    override val dialogMessageResId: Int
        get() = R.string.warning_reset_attendance

    override val dialogPositiveButtonTextResId: Int
        get() = R.string.reset

    override val dialogNegativeButtonTextResId: Int
        get() = R.string.cancel

    companion object {
        private const val ARG_SUBJECT_TITLE = "subjectTitle"

        fun newInstance(
                subjectTitle: String,
                confirmationDialogListener: ConfirmationDialogListener
        ): DialogFragment {
            val args = Bundle()
            args.putString(ARG_SUBJECT_TITLE, subjectTitle)
            val fragment = ResetAttendanceConfirmationDialog(confirmationDialogListener)
            fragment.arguments = args
            return fragment
        }
    }
}