package android.mahendra.attendancemanager.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes

import androidx.fragment.app.DialogFragment

abstract class ConfirmationDialogFragment(
        private val confirmationDialogListener: ConfirmationDialogListener
): DialogFragment() {

    @get:StringRes
    abstract val dialogTitleRedId: Int

    @get:StringRes
    abstract val dialogMessageResId: Int

    @get:StringRes
    abstract val dialogPositiveButtonTextResId: Int

    @get:StringRes
    abstract val dialogNegativeButtonTextResId: Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle(dialogTitleRedId)
        builder.setMessage(dialogMessageResId)
        builder.setPositiveButton(dialogPositiveButtonTextResId) { _, _ ->
            confirmationDialogListener.onPositive()
            dismiss()
        }

        builder.setNegativeButton(dialogNegativeButtonTextResId) { _, _ ->
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
