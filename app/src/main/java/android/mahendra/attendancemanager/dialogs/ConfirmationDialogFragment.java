package android.mahendra.attendancemanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.mahendra.attendancemanager.R;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialogFragment extends DialogFragment {
    public static final String ARG_REQUEST_CODE = "request_code";
    public static final String ARG_TITLE = "title";
    public static final String ARG_MESSAGE = "message";
    public static final String ARG_POSITIVE = "poistive";
    public static final String ARG_NEGATIVE = "negative";

    private ConfirmationDialogListener mListener;

    public interface ConfirmationDialogListener {
        void onPositiveClick(DialogFragment dialog);
        void onNegativeClick(DialogFragment dialog);
    }

    public static ConfirmationDialogFragment newInstance(
            String title, String message, String negative, String positive, int requestCode) {

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
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
        builder.setPositiveButton(getArguments().getString(ARG_POSITIVE), null);

        builder.setNegativeButton(getArguments().getString(ARG_NEGATIVE), null);

        return builder.create();
    }
}
