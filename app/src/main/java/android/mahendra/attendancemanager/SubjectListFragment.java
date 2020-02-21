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
    private static final int REQUEST_EDIT_ATTENDANCE = 4;

    private SubjectViewModel mSubjectViewModel;

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
        binding.addSubjectFloatingButton.setOnClickListener(v -> openAddSubjectDialog());
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
            // when a new subject is added
            String subjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_SUBJECT_TITLE);
            Subject subject = new Subject();
            subject.setTitle(subjectTitle);
            mSubjectViewModel.insert(subject);
        }
        if (requestCode == REQUEST_SUBJECT_TITLE_EDIT) {
            // when a subject's title is changed
            String oldSubjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_OLD_SUBJECT_TITLE);
            String newSubjectTitle = data.getStringExtra(SubjectTitleEditDialogFragment.EXTRA_SUBJECT_TITLE);
            mSubjectViewModel.onUpdateTitle(oldSubjectTitle, newSubjectTitle);
        }
        if (requestCode == REQUEST_SUBJECT_DELETE) {
            // on subject delete confirmation
            String title = data.getStringExtra(ConfirmationDialogFragment.EXTRA_SUBJECT_TITLE);
            mSubjectViewModel.onDeleteSubjectWith(title);
            showSubjectDeleteToast(title);
        }
        if (requestCode == REQUEST_RESET_ATTENDANCE) {
            // on reset attendance confirmation
            String title = data.getStringExtra(ConfirmationDialogFragment.EXTRA_SUBJECT_TITLE);
            mSubjectViewModel.onResetAttendance(title);
            showResetAttendanceToast(title);
        }
        if (requestCode == REQUEST_EDIT_ATTENDANCE) {
            String title = data.getStringExtra(AttendanceEditDialogFragment.EXTRA_SUBJECT_TITLE);
            int attended = data.getIntExtra(AttendanceEditDialogFragment.EXTRA_ATTENDED, 0);
            int total = data.getIntExtra(AttendanceEditDialogFragment.EXTRA_TOTAL, 0);
            Log.i(TAG, "onActivityResult: attended " + attended + "total " + total);
            Subject subject = mSubjectViewModel.getSubject(title);
            subject.setAttendedClasses(attended);
            subject.setMissedClasses(total-attended);
            mSubjectViewModel.update(subject);
        }
    }

    /**
     * view holder for subject item with data binding
     */
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

    /**
     * subject list adapter which provides subject view holder to recycler view
     */
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

        /**
         * set subject list to new list not a good way to update, need to be refactored
         * @param subjects list of all subjects in database
         */
        private void setSubjects(List<Subject> subjects) {
            this.subjects = subjects;
            notifyDataSetChanged();
        }
    }

    /**
     * uses SubjectTitleEditDialogFragment for taking new subject title input from user
     * old title is passed as null, as there is no subject
     */
    private void openAddSubjectDialog() {
        SubjectTitleEditDialogFragment dialogFragment = SubjectTitleEditDialogFragment.newInstance(null);
        dialogFragment.setTargetFragment(SubjectListFragment.this, REQUEST_NEW_SUBJECT);
        dialogFragment.show(getParentFragmentManager(), "subject");
    }

    /**
     * option dialog which will be opened when user clicks option menu icon on view holder
     * and the subject title is passed so that it can be used by listener to take actions
     * @param title subject title which is used by dialog, for listener methods
     */
    private void openSubjectOptionDialog(String title) {
        SubjectOptionBottomSheetDialog dialog = SubjectOptionBottomSheetDialog.newInstance(title);
        dialog.setTargetFragment(this, 0);
        dialog.show(getParentFragmentManager(), "option");
    }

    /**
     * when delete option is selected from subjectOptionDialog
     * @param subjectTitle title of subject for which this option was selected
     */
    @Override
    public void onDeleteSelected(String subjectTitle) {
        ConfirmationDialogFragment dialogFragment = ConfirmationDialogFragment.
                newInstance(subjectTitle, getString(R.string.delete_subject, subjectTitle), getString(R.string.warning_subject_delete),
                        "cancel", "delete");
        dialogFragment.setTargetFragment(this, REQUEST_SUBJECT_DELETE);
        dialogFragment.show(getParentFragmentManager(), "confirmation delete subject");
    }

    /**
     * when edit title option in selected from subjectOptionDialog
     * @param subjectTitle title of subject for which this option was selected
     */
    @Override
    public void onEditTitleSelected(String subjectTitle) {
        SubjectTitleEditDialogFragment dialogFragment = SubjectTitleEditDialogFragment.newInstance(subjectTitle);
        dialogFragment.setTargetFragment(SubjectListFragment.this, REQUEST_SUBJECT_TITLE_EDIT);
        dialogFragment.show(getParentFragmentManager(), "subject");
    }

    /**
     * when edit attendance option in selected from subjectOptionDialog
     * @param subjectTitle title of subject for which this option was selected
     */
    @Override
    public void onEditAttendanceSelected(String subjectTitle) {
        Subject subject = mSubjectViewModel.getSubject(subjectTitle);
        AttendanceEditDialogFragment dialogFragment = AttendanceEditDialogFragment.newInstance(
                subjectTitle,
                subject.getAttendedClasses(), subject.getTotalClasses()
        );
        dialogFragment.setTargetFragment(this, REQUEST_EDIT_ATTENDANCE);
        dialogFragment.show(getParentFragmentManager(), "attendance");
    }

    /**
     * when reset attendance option is selected from subjectOptionDialog
     * @param subjectTitle title of subject for which this option was selected
     */
    @Override
    public void onResetAttendanceSelected(String subjectTitle) {
        ConfirmationDialogFragment dialogFragment = ConfirmationDialogFragment.
                newInstance(subjectTitle, getString(R.string.reset_attendance),
                        getString(R.string.warning_reset_attendance, subjectTitle),
                        "cancel", "reset");
        dialogFragment.setTargetFragment(this, REQUEST_RESET_ATTENDANCE);
        dialogFragment.show(getParentFragmentManager(), "confirmation reset attendance");
    }

    /**
     * toast shown when subject is deleted
     * @param subjectTitle used in message
     */
    private void showSubjectDeleteToast(String subjectTitle) {
        Toast.makeText(getActivity(),
                "successfully deleted " + subjectTitle, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast shown when subject attendance is reset
     * @param subjectTitle used in message
     */
    private void showResetAttendanceToast(String subjectTitle) {
        Toast.makeText(getActivity(),
                "attendance reset " + subjectTitle, Toast.LENGTH_SHORT).show();
    }
}


