package android.mahendra.attendancemanager.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.DialogFragment

class ConfirmationDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogTitle = arguments?.getString(ARG_DIALOG_TITLE)
        val dialogMessage = arguments?.getString(ARG_DIALOG_MESSAGE)
        val subjectTitle = arguments?.getString(ARG_SUBJECT_TITLE)
        val dialogPositiveResponse = arguments?.getString(ARG_POSITIVE_RESPONSE)
        val dialogNegativeResponse = arguments?.getString(ARG_NEGATIVE_RESPONSE)

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.setPositiveButton(dialogPositiveResponse) { dialog, which -> sendResult(Activity.RESULT_OK, subjectTitle) }

        builder.setNegativeButton(dialogNegativeResponse) { dialog, which -> sendResult(Activity.RESULT_CANCELED, subjectTitle) }

        return builder.create()
    }

    private fun sendResult(resultCode: Int, subjectTitle: String?) {
        if (targetFragment != null) {
            val intent = Intent()
            intent.putExtra(EXTRA_SUBJECT_TITLE, subjectTitle)
            targetFragment?.onActivityResult(targetRequestCode, resultCode, intent)
        }
    }

    companion object {
        private const val ARG_POSITIVE_RESPONSE = "positive"
        private const val ARG_SUBJECT_TITLE = "subjectTitle"
        private const val ARG_DIALOG_TITLE = "title"
        private const val ARG_DIALOG_MESSAGE = "message"
        private const val ARG_NEGATIVE_RESPONSE = "negative"

        const val EXTRA_SUBJECT_TITLE = "subject title"

        fun newInstance(
            subjectTitle: String,
            dialogTitle: String,
            dialogMessage: String,
            negativeResponse: String,
            positiveResponse: String
        ): ConfirmationDialogFragment {

            val args = Bundle()
            args.putString(ARG_SUBJECT_TITLE, subjectTitle)
            args.putString(ARG_DIALOG_TITLE, dialogTitle)
            args.putString(ARG_DIALOG_MESSAGE, dialogMessage)
            args.putString(ARG_POSITIVE_RESPONSE, positiveResponse)
            args.putString(ARG_NEGATIVE_RESPONSE, negativeResponse)
            val fragment = ConfirmationDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
