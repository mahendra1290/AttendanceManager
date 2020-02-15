package android.mahendra.attendancemanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.mahendra.attendancemanager.R;
import android.mahendra.attendancemanager.models.Period;
import android.mahendra.attendancemanager.models.Subject;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class AddPeriodDialogFragment extends DialogFragment {
    private static final String TAG = "AddPeriodDialogFragment";
    private static final String ARG_PERIOD_NUMBER = "period number";
    private static final String ARG_PERIOD_TITLE = "period title";
    private static final String ARG_PERIOD_WEEK = "period week";
    private static final String KEY_SUBJECT_TITLES = "subject title";

    private static final int RESULT_NEW_PERIOD = 0;
    private static final int RESULT_MODIFY_PERIOD = 1;
    private static final int RESULT_DELETE_PERIOD = 2;

    private ArrayList<String> mSubjectTitles = new ArrayList<>();
    private Callbacks mCallback;

    public interface Callbacks {
        ArrayList<String> getSubjectTitles();
        void onPeriodSelected(String title, int periodNumber, int weekday);
        void onDeletePeriod(String title);
    }

    public static AddPeriodDialogFragment newInstance(String periodTitle, int periodNumber, int weeDay) {
        Bundle args = new Bundle();
        args.putString(ARG_PERIOD_TITLE, periodTitle);
        args.putInt(ARG_PERIOD_NUMBER, periodNumber);
        args.putInt(ARG_PERIOD_WEEK, weeDay);
        AddPeriodDialogFragment fragment = new AddPeriodDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_SUBJECT_TITLES, mSubjectTitles);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callbacks) context;
        }
        catch (ClassCastException ex) {
            Log.e(TAG, "onAttach: " + ex.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSubjectTitles = savedInstanceState.getStringArrayList(KEY_SUBJECT_TITLES);
        }
        else {
            mSubjectTitles = mCallback.getSubjectTitles();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog: created dialog");
        String periodTitle = getArguments().getString(ARG_PERIOD_TITLE, null);
        int periodNumber = getArguments().getInt(ARG_PERIOD_NUMBER, -1);
        int weekDay = getArguments().getInt(ARG_PERIOD_WEEK, -1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_choose_period, null);

        ImageView clearPeriod = v.findViewById(R.id.clear_period);
        clearPeriod.setOnClickListener(v1 -> {
            mCallback.onDeletePeriod(periodTitle);
            dismiss();
        });

        if (periodTitle != null) {
            clearPeriod.setVisibility(View.VISIBLE);
        }
        else {
            clearPeriod.setVisibility(View.GONE);
        }

        Spinner spinner = v.findViewById(R.id.spinner_select_period);
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapter.addAll(mSubjectTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setTitle(R.string.select_subject);
        builder.setView(v);

        if (periodTitle != null) {
            int position = adapter.getPosition(periodTitle);
            spinner.setSelection(position);
        }

        builder.setPositiveButton(R.string.add, (dialog, which) -> {
            mCallback.onNewPeriod(spinner.getSelectedItem().toString(), periodNumber, weekDay);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

        });
        return builder.create();
    }
}
