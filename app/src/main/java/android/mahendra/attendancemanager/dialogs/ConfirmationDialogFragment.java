package android.mahendra.attendancemanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialogFragment extends DialogFragment {
    public static final String ARG_REQUEST_CODE = "requestCode";
    public static final String ARG_TITLE = "title";
    public static final String ARG_MESSAGE = "message";

    public static ConfirmationDialogFragment newInstance(String title, String message) {

        Bundle args = new Bundle();
//        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = getArguments().getString(ARG_TITLE);
        String message = getArguments().getString(ARG_MESSAGE);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", null);
        builder.setNegativeButton("No", null);

        return builder.create();
    }
}
