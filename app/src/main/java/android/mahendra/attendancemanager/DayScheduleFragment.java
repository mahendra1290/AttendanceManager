package android.mahendra.attendancemanager;

import android.content.Context;
import android.mahendra.attendancemanager.databinding.FragmentDayScheduleBinding;
import android.mahendra.attendancemanager.databinding.ListItemPeriodBinding;
import android.mahendra.attendancemanager.dialogs.AddPeriodDialogFragment;
import android.mahendra.attendancemanager.models.Period;
import android.mahendra.attendancemanager.viewmodels.PeriodListViewModel;
import android.mahendra.attendancemanager.viewmodels.PeriodViewModel;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DayScheduleFragment extends Fragment {
    private static final String TAG = "DayScheduleFragment";
    private static int MAX_PERIODS = 8;

    private int weekDay;
    private PeriodListViewModel mPeriodListViewModel;
    private Callbacks mCallback;

    public interface Callbacks {
        void onAddPeriod(int periodNumber, int weekDay);
        void onModifyPeriod(Period period);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public static DayScheduleFragment newInstance(int weekDay) {
        return new DayScheduleFragment(weekDay);
    }

    private DayScheduleFragment(int weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPeriodListViewModel = new ViewModelProvider(requireActivity()).get(PeriodListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDayScheduleBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_day_schedule, container, false);

        PeriodAdapter adapter = new PeriodAdapter();
        mPeriodListViewModel.getAllPeriodsOn(weekDay).observe(getViewLifecycleOwner(), adapter::setPeriodList);

        binding.periodListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.periodListRecyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    private class PeriodHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ListItemPeriodBinding mBinding;
        private Period mPeriod;

        public PeriodHolder(ListItemPeriodBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setPeriodViewModel(new PeriodViewModel());
            itemView.setOnClickListener(this);
        }

        public void bind(Period period) {
            mPeriod = period;
            mBinding.getPeriodViewModel().setPeriod(period);
            mBinding.getPeriodViewModel().notifyChange();
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mPeriod.getSubjectTitle().equals("-")) {
                mCallback.onAddPeriod(mPeriod.getPeriodNumber(), weekDay);
            }
            else {
                mCallback.onModifyPeriod(mPeriod);
            }
        }
    }

    private class PeriodAdapter extends RecyclerView.Adapter<PeriodHolder> {
        List<Period> mPeriodList = new ArrayList<>();

        public PeriodAdapter() {
            for (int i = 0; i < MAX_PERIODS; i++) {
                mPeriodList.add(new Period("-", i+1, weekDay));
            }
        }

        @NonNull
        @Override
        public PeriodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemPeriodBinding binding = DataBindingUtil.inflate(
                    getLayoutInflater(), R.layout.list_item_period, parent, false);
            return new PeriodHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PeriodHolder holder, int position) {
            holder.bind(mPeriodList.get(position));
        }

        @Override
        public int getItemCount() {
            return mPeriodList.size();
        }

        public void setPeriodList(List<Period> periods) {
            for (int i = 0; i < MAX_PERIODS; i++) {
                mPeriodList.get(i).setSubjectTitle("-");
                for (Period period : periods) {
                    if (mPeriodList.get(i).getPeriodNumber() == period.getPeriodNumber()) {
                        mPeriodList.set(i, period);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
