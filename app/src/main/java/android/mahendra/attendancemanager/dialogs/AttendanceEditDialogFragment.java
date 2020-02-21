package android.mahendra.attendancemanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.mahendra.attendancemanager.R;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AttendanceEditDialogFragment extends DialogFragment {
    private static final String TAG = "AttendanceEditDialogFra";

    private static final String ARG_SUBJECT_TITLE = "subject_title";
    private static final String ARG_ATTENDED_CLASSES =
            "com.android.mahendra.attendancemanager.subject.attendedClasses";

    private static final String ARG_TOTAL_CLASSES =
            "com.android.mahendra.attendancemanager.subject.totalClasses";

    public static final String EXTRA_SUBJECT_TITLE = "subject_title";
    public static final String EXTRA_ATTENDED = "attended";
    public static final String EXTRA_TOTAL = "total";

    EditText attendedTextView;
    EditText totalTextView;

    AlertDialog mDialog;

    public static AttendanceEditDialogFragment newInstance(String subjectTitle, int attendedClasses, int totalClasses) {
        Bundle args = new Bundle();
        args.putInt(ARG_ATTENDED_CLASSES, attendedClasses);
        args.putInt(ARG_TOTAL_CLASSES, totalClasses);
        args.putString(ARG_SUBJECT_TITLE, subjectTitle);
        AttendanceEditDialogFragment fragment = new AttendanceEditDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        updatePositiveButton();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = requireActivity().getLayoutInflater().inflate(R.layout.dialog_edit_attendance, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Edit Attendance ")
                .setPositiveButton("Edit", ((dialog, which) -> sendResult(Activity.RESULT_OK)))
                .setNegativeButton("Cancel", null);
        mDialog = builder.create();

        attendedTextView = v.findViewById(R.id.editText_attended);
        totalTextView = v.findViewById(R.id.editText_total);
        attendedTextView.setText( "" + getArguments().getInt(ARG_ATTENDED_CLASSES));
        totalTextView.setText("" + getArguments().getInt(ARG_TOTAL_CLASSES));

        attendedTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePositiveButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        totalTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePositiveButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.i(TAG, "onCreateDialog: attended " + getArguments().getInt(ARG_ATTENDED_CLASSES));
        Log.i(TAG, "onCreateDialog: total " + getArguments().getInt(ARG_TOTAL_CLASSES));
        return mDialog;
    }

    private void updatePositiveButton() {
        Log.i(TAG, "updatePositiveButton: " + mDialog);
        int attended;
        int total;
        try {
            attended = Integer.valueOf(attendedTextView.getText().toString());
            total = Integer.valueOf(totalTextView.getText().toString());
        }
        catch (NumberFormatException ex) {
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            return;
        }
        if (total < attended) {
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }
        else {
            mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            Log.i(TAG, "sendResult: int attendance edit");
            return;
        }
        int attended = Integer.valueOf(attendedTextView.getText().toString());
        int total = Integer.valueOf(totalTextView.getText().toString());
        Intent i = new Intent();
        i.putExtra(EXTRA_ATTENDED, attended);
        i.putExtra(EXTRA_TOTAL, total);
        i.putExtra(EXTRA_SUBJECT_TITLE, getArguments().getString(ARG_SUBJECT_TITLE));

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
