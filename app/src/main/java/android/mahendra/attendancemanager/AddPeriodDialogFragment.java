package android.mahendra.attendancemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.mahendra.attendancemanager.models.Subject;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AddPeriodDialogFragment extends DialogFragment {
    private static final String TAG = "AddPeriodDialogFragment";
    private static final String ARG_PERIOD_NUMBER = "period number";

    private List<Subject> mSubjects = new ArrayList<>();
    private SubjectCallback mCallback;
    private PeriodCallback mPeriodCallback;

    public interface SubjectCallback {
        List<Subject> getSubjects();
    }

    public interface PeriodCallback {
        void onNewPeriod(int periodNumber, String periodTitle);
    }

    public static AddPeriodDialogFragment newInstance(int periodNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_PERIOD_NUMBER, periodNumber);
        AddPeriodDialogFragment fragment = new AddPeriodDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getTargetFragment() == null) {
            mCallback = (SubjectCallback) context;
            mPeriodCallback = (PeriodCallback) context;
        }
        else {
            mCallback = (SubjectCallback) getTargetFragment();
            mPeriodCallback = (PeriodCallback) getTargetFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        mPeriodCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubjects = mCallback.getSubjects();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog: created dialog");
        int periodNumber = getArguments().getInt(ARG_PERIOD_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_choose_period, null);
        Spinner spinner = v.findViewById(R.id.spinner_select_period);
        CharSequence[] subjectTitles = new CharSequence[mSubjects.size()];
        for (int i = 0; i < mSubjects.size(); i++) {
            subjectTitles[i] = mSubjects.get(i).getTitle();
            Log.i(TAG, "onCreateDialog: " + subjectTitles[i]);
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapter.addAll(subjectTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setTitle(R.string.select_subject);
        builder.setView(v);
        builder.setPositiveButton(R.string.add, (dialog, which) -> {
            mPeriodCallback.onNewPeriod(periodNumber, spinner.getSelectedItem().toString());
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

        });
        return builder.create();
    }
}
