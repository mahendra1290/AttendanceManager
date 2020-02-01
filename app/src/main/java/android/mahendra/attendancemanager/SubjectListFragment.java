package android.mahendra.attendancemanager;

import android.mahendra.attendancemanager.databinding.FragmentSubjectListBinding;
import android.os.Binder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class SubjectListFragment extends Fragment {

    public static SubjectListFragment newInstance() {
        return new SubjectListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSubjectListBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_subject_list, container, false
        );
        return binding.getRoot();
    }
}
