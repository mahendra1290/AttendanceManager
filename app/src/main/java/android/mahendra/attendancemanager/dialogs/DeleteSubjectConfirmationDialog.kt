package android.mahendra.attendancemanager.dialogs

import android.mahendra.attendancemanager.R
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DeleteSubjectConfirmationDialog(
        confirmationDialogListener: ConfirmationDialogListener
): ConfirmationDialogFragment(confirmationDialogListener){

    override val dialogTitleRedId: Int
        get() = R.string.delete_subject

    override val dialogMessageResId: Int
        get() = R.string.warning_subject_delete

    override val dialogPositiveButtonTextResId: Int
        get() = R.string.delete

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
            val fragment = DeleteSubjectConfirmationDialog(confirmationDialogListener)
            fragment.arguments = args
            return fragment
        }
    }

}