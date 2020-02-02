package android.mahendra.attendancemanager;

import android.app.Activity;
import android.content.Intent;
import android.mahendra.attendancemanager.databinding.FragmentSubjectListBinding;
import android.mahendra.attendancemanager.databinding.ListItemSubjectBinding;
import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.viewmodels.SubjectListViewModel;
import android.mahendra.attendancemanager.viewmodels.SubjectViewModel;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SubjectListFragment extends Fragment {
    private static final String TAG = "SubjectListFragment";
    public static final int REQUEST_SUBJECT = 0;

    private SubjectListViewModel subjectListViewModel;

    public static SubjectListFragment newInstance() {
        return new SubjectListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSubjectListBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_subject_list, container, false
        );

        binding.subjectListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SubjectAdapter adapter = new SubjectAdapter();
        subjectListViewModel = new ViewModelProvider(requireActivity()).get(SubjectListViewModel.class);
        subjectListViewModel.getAllSubjects().observe(getViewLifecycleOwner(), subjects -> {
            Log.i(TAG, "onCreateView: data updated");
            adapter.setSubjects(subjects);
        });
        Subject.setListner(subjectListViewModel);
        binding.subjectListRecyclerView.setAdapter(adapter);
        binding.addSubjectFloatingButton.setOnClickListener(v -> {
            openAddSubjectDialog();
        });
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_subject_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_timetable:
                Intent intent = TimeTableActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_SUBJECT) {
            String subjectTitle = data.getStringExtra(AddSubjectDialogFragment.EXTRA_SUBJECT_TITLE);
            Subject subject = new Subject();
            subject.setTitle(subjectTitle);
            subjectListViewModel.insert(subject);
//            mSubjectAdapter.notifyDataSetChanged();
        }
    }

    private class SubjectHolder extends RecyclerView.ViewHolder {
        private ListItemSubjectBinding mBinding;

        public SubjectHolder(ListItemSubjectBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setSubjectViewModel(new SubjectViewModel());
        }

        public void bind(Subject subject) {
            Log.i(TAG, "bind: " + subject.getTitle());
            mBinding.getSubjectViewModel().setSubject(subject);
            mBinding.getSubjectViewModel().notifyChange();
            mBinding.executePendingBindings();
        }
    }

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {
        List<Subject> subjects = new ArrayList<>();
        @NonNull
        @Override
        public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            ListItemSubjectBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.list_item_subject, parent, false);
            return new SubjectHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectHolder holder, int position) {
            holder.bind(subjects.get(position));
        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        public void setSubjects(List<Subject> subjects) {
            this.subjects = subjects;
            for (Subject subject : subjects) {
                Log.i(TAG, "setSubjects: " + subject.getTitle());
            }
            notifyDataSetChanged();
        }
    }

    private void openAddSubjectDialog() {
        AddSubjectDialogFragment dialogFragment = new AddSubjectDialogFragment();
        dialogFragment.setTargetFragment(SubjectListFragment.this, REQUEST_SUBJECT);
        dialogFragment.show(getFragmentManager(), "subject");
    }
}
