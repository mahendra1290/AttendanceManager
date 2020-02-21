package android.mahendra.attendancemanager;

import android.app.Activity;
import android.content.Intent;
import android.mahendra.attendancemanager.databinding.FragmentSubjectListBinding;
import android.mahendra.attendancemanager.databinding.ListItemSubjectBinding;
import android.mahendra.attendancemanager.dialogs.AttendanceEditDialogFragment;
import android.mahendra.attendancemanager.dialogs.SubjectTitleEditDialogFragment;
import android.mahendra.attendancemanager.dialogs.ConfirmationDialogFragment;
import android.mahendra.attendancemanager.dialogs.SubjectOptionBottomSheetDialog;
import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.viewmodels.SubjectViewModel;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SubjectListFragment extends Fragment implements SubjectOptionBottomSheetDialog.SubjectOptionListener {

    private static final String TAG = "SubjectListFragment";
    private static final int REQUEST_NEW_SUBJECT = 0;
    private static final int REQUEST_SUBJECT_TITLE_EDIT = 1;
    private static final int REQUEST_SUBJECT_DELETE = 2;
    private static final int REQUEST_RESET_ATTENDANCE = 3;


    private SubjectViewModel mSubjectViewModel;

    private List<Subject> mSubjects;

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

        SubjectAdapter adapter = new SubjectAdapter();
        mSubjectViewModel = new ViewModelProvider(requireActivity()).get(SubjectViewModel.class);
        mSubjectViewModel.getAllSubjects().observe(getViewLifecycleOwner(), adapter::setSubjects);

        binding.subjectListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            case R.id.mark_attendance:
                Intent intent1 = MarkAttendaceActivity.newIntent(getActivity(),
                        Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                startActivity(intent1);
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
        if (requestCode == REQUEST_NEW_SUBJECT) {
            String subjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_SUBJECT_TITLE);
            Subject subject = new Subject();
            subject.setTitle(subjectTitle);
            mSubjectViewModel.insert(subject);
        }
        if (requestCode == REQUEST_SUBJECT_TITLE_EDIT) {
            String oldSubjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_OLD_SUBJECT_TITLE);
            String newSubjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_SUBJECT_TITLE);
            mSubjectViewModel.onUpdateTitle(oldSubjectTitle, newSubjectTitle);
        }
        if (requestCode == REQUEST_SUBJECT_DELETE) {
            String title = data.getStringExtra(ConfirmationDialogFragment.EXTRA_SUBJECT_TITLE);
            mSubjectViewModel.onDeleteSubjectWith(title);
            showSubjectDeleteToast(title);
        }
        if (requestCode == REQUEST_RESET_ATTENDANCE) {
            String title = data.getStringExtra(ConfirmationDialogFragment.EXTRA_SUBJECT_TITLE);
            mSubjectViewModel.onResetAttendance(title);
            showResetAttendanceToast(title);
        }
    }

    private class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ListItemSubjectBinding mBinding;
        private Subject mSubject;

        public SubjectHolder(ListItemSubjectBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setSubjectViewModel(mSubjectViewModel);
            mBinding.moreOptions.setOnClickListener(this);
        }

        public void bind(Subject subject) {
            mSubject = subject;
            mBinding.setSubject(subject);
        }

        @Override
        public void onClick(View v) {
            openSubjectOptionDialog(mSubject.getTitle());
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
            mSubjects = subjects;
            notifyDataSetChanged();
        }
    }

    private void openAddSubjectDialog() {
        SubjectTitleEditDialogFragment dialogFragment = SubjectTitleEditDialogFragment.newInstance(null);
        dialogFragment.setTargetFragment(SubjectListFragment.this, REQUEST_NEW_SUBJECT);
        dialogFragment.show(getParentFragmentManager(), "subject");
    }

    private void openSubjectOptionDialog(String title) {
        SubjectOptionBottomSheetDialog dialog = SubjectOptionBottomSheetDialog.newInstance(title);
        dialog.setTargetFragment(this, 0);
        dialog.show(getParentFragmentManager(), "option");
    }

    @Override
    public void onDeleteSelected(String title) {
        ConfirmationDialogFragment dialogFragment = ConfirmationDialogFragment.
                newInstance(title, getString(R.string.delete_subject, title), getString(R.string.warning_subject_delete),
                        "cancel", "delete");
        dialogFragment.setTargetFragment(this, REQUEST_SUBJECT_DELETE);
        dialogFragment.show(getParentFragmentManager(), "confirmation delete subject");
    }

    @Override
    public void onEditTitleSelected(String title) {
        SubjectTitleEditDialogFragment dialogFragment = SubjectTitleEditDialogFragment.newInstance(title);
        dialogFragment.setTargetFragment(SubjectListFragment.this, REQUEST_SUBJECT_TITLE_EDIT);
        dialogFragment.show(getParentFragmentManager(), "subject");
    }

    @Override
    public void onEditAttendanceSelected() {
        AttendanceEditDialogFragment dialogFragment = new AttendanceEditDialogFragment();
        dialogFragment.show(getParentFragmentManager(), "attendance");
    }

    @Override
    public void onResetAttendanceSelected(String title) {
        ConfirmationDialogFragment dialogFragment = ConfirmationDialogFragment.
                newInstance(title, getString(R.string.reset_attendance), getString(R.string.warning_reset_attendance, title),
                        "cancel", "reset");
        dialogFragment.setTargetFragment(this, REQUEST_RESET_ATTENDANCE);
        dialogFragment.show(getParentFragmentManager(), "confirmation reset attendance");
    }

    private void showSubjectDeleteToast(String subjectTitle) {
        Toast.makeText(getActivity(),
                "successfully deleted " + subjectTitle, Toast.LENGTH_SHORT).show();
    }

    private void showResetAttendanceToast(String subjectTitle) {
        Toast.makeText(getActivity(),
                "attendance reset " + subjectTitle, Toast.LENGTH_SHORT).show();
    }
}


