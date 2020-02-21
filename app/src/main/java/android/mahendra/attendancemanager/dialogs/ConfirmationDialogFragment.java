package android.mahendra.attendancemanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.mahendra.attendancemanager.R;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialogFragment extends DialogFragment {
    public static final String ARG_REQUEST_CODE = "request_code";

    private static final String ARG_POSITIVE = "positive";
    private static final String ARG_SUBJECT_TITLE = "subjectTitle";
    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_NEGATIVE = "negative";

    public static final String EXTRA_SUBJECT_TITLE = "subject title";

    public static ConfirmationDialogFragment newInstance(
            String subjectTitle, String title, String message, String negative, String positive) {

        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT_TITLE, subjectTitle);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_POSITIVE, positive);
        args.putString(ARG_NEGATIVE, negative);
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = getArguments().getString(ARG_TITLE);
        String message = getArguments().getString(ARG_MESSAGE);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getArguments().getString(ARG_POSITIVE),
                (dialog, which) -> sendResult(Activity.RESULT_OK, getArguments().getString(ARG_SUBJECT_TITLE)));

        builder.setNegativeButton(getArguments().getString(ARG_NEGATIVE), (dialog, which) -> dismiss());

        return builder.create();
    }

    private void sendResult(int resultCode, String subjectTitle) {
        if (getTargetFragment() == null) {
            return;
        }
        else {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_SUBJECT_TITLE, subjectTitle);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }
}
