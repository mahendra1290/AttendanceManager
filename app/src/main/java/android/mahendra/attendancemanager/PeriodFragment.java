package android.mahendra.attendancemanager;

import android.mahendra.attendancemanager.models.Period;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PeriodFragment extends Fragment {

    private Period mPeriod;

    public static PeriodFragment newInstance(Period period) {

        Bundle args = new Bundle();

        PeriodFragment fragment = new PeriodFragment(period);
        fragment.setArguments(args);
        return fragment;
    }

    public PeriodFragment(Period period) {
        mPeriod = period;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendance, container, false);
        TextView mTitle = v.findViewById(R.id.period_title);
        TextView mPeriodNuber = v.findViewById(R.id.period_number);
        mTitle.setText(mPeriod.getSubjectTitle());
        mPeriodNuber.setText(mPeriod.getPeriodNumber() + " th");
        return v;
    }
}
