package android.mahendra.attendancemanager.dialogs

import android.app.Dialog
import android.mahendra.attendancemanager.R
import android.os.Bundle
import androidx.annotation.StringRes

import androidx.fragment.app.DialogFragment

abstract class ConfirmationDialogFragment(
        private val confirmationDialogListener: ConfirmationDialogListener
): DialogFragment() {

    abstract val dialogTitle: String

    abstract val dialogMessage: String

    abstract val dialogPositiveButtonText: String

    open val dialogNegativeButtonText: String
        get() = getString(R.string.cancel)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.setPositiveButton(dialogPositiveButtonText) { _, _ ->
            confirmationDialogListener.onPositive()
            dismiss()
        }

        builder.setNegativeButton(dialogNegativeButtonText) { _, _ ->
            confirmationDialogListener.onNegative()
            dismiss()
        }
        return builder.create()
    }
}

class ConfirmationDialogListener(
        private val onPositiveClick: () -> Unit,
        private val onNegativeClick: () -> Unit = {}
) {
    fun onPositive() {
        onPositiveClick()
    }

    fun onNegative() {
        onNegativeClick()
    }
}
