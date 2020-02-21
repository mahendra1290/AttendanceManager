package android.mahendra.attendancemanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.mahendra.attendancemanager.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class SubjectTitleEditDialogFragment extends DialogFragment {
    private static final String TAG = "AddSubjectDialogFragmen";
    private static final String ARG_SUBJECT_TITLE = "subject_title";

    public static final String EXTRA_OLD_SUBJECT_TITLE = "" +
            "com.android.mahendra.attendancemanager.subject.old_title";
    public static final String EXTRA_SUBJECT_TITLE =
            "com.android.mahendra.attendancemanager.subject.title";

    private EditText mSubjectTitleTextView;

    public static SubjectTitleEditDialogFragment newInstance(String subjectTitle) {

        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT_TITLE, subjectTitle);

        SubjectTitleEditDialogFragment fragment = new SubjectTitleEditDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = requireActivity().getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        String subjectTitle = getArguments().getString(ARG_SUBJECT_TITLE);

        mSubjectTitleTextView = v.findViewById(R.id.subjectTitleEditText);
        mSubjectTitleTextView.requestFocus();
        if (subjectTitle != null) {
            mSubjectTitleTextView.setText(subjectTitle);
            mSubjectTitleTextView.setSelection(0, subjectTitle.length());
        }
        builder.setTitle(R.string.add_subject);
        builder.setView(v);
        builder.setPositiveButton(R.string.add, (dialog, which) -> {
            sendResult(Activity.RESULT_OK, mSubjectTitleTextView.getText().toString().trim());
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: resumed");
        if (mSubjectTitleTextView != null) {
            mSubjectTitleTextView.requestFocus();
        }
    }

    private void sendResult(int resultCode, String subjectTitle) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent i = new Intent();
        i.putExtra(EXTRA_OLD_SUBJECT_TITLE, getArguments().getString(ARG_SUBJECT_TITLE));
        i.putExtra(EXTRA_SUBJECT_TITLE, subjectTitle);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
