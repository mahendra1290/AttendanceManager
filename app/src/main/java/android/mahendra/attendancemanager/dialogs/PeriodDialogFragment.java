package android.mahendra.attendancemanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.mahendra.attendancemanager.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class PeriodDialogFragment extends DialogFragment {
    private static final String TAG = "PeriodDialogFragment";
    private static final String ARG_PERIOD_TITLE = "period title";
    private static final String KEY_TITLE_OPTIONS = "title options";

    private ArrayList<String> mSubjectTitles = new ArrayList<>();
    private Callbacks mCallback;

    public interface Callbacks {
        ArrayList<String> getSubjectTitles();
        void onPeriodSelected(String title);
        void onDeletePeriod(String title);
    }

    public static PeriodDialogFragment newInstance(String periodTitle) {
        Bundle args = new Bundle();
        args.putString(ARG_PERIOD_TITLE, periodTitle);
        PeriodDialogFragment fragment = new PeriodDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_TITLE_OPTIONS, mSubjectTitles);
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
            mSubjectTitles = savedInstanceState.getStringArrayList(KEY_TITLE_OPTIONS);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_choose_period, null);

        ImageView clearPeriod = v.findViewById(R.id.clear_period);
        clearPeriod.setOnClickListener(v1 -> {
            mCallback.onDeletePeriod(periodTitle);
            Toast.makeText(getActivity(), "Removed " + periodTitle, Toast.LENGTH_SHORT).show();
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
            mCallback.onPeriodSelected(spinner.getSelectedItem().toString());
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

        });
        return builder.create();
    }
}
