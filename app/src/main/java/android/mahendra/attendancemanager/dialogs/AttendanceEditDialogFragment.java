package android.mahendra.attendancemanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.mahendra.attendancemanager.R;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AttendanceEditDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(R.layout.dialog_edit_attendance)
                .setTitle("Edit Attendance")
                .setPositiveButton("Edit", null)
                .setNegativeButton("Cancel", null);
        return builder.create();
    }
}
