package android.mahendra.attendancemanager.dialogs;

import android.content.Context;
import android.mahendra.attendancemanager.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SubjectOptionBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String ARG_TITLE = "subject title";

    private SubjectOptionListener mSubjectOptionListener;

    public static SubjectOptionBottomSheetDialog newInstance(@NonNull String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        SubjectOptionBottomSheetDialog fragment = new SubjectOptionBottomSheetDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public interface SubjectOptionListener {
        void onDeleteSelected(String title);
        void onEditTitleSelected(String title);
        void onEditAttendanceSelected(String title);
        void onResetAttendanceSelected(String title);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mSubjectOptionListener = (SubjectOptionListener) getTargetFragment();
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(context.toString()
                    + "must implement SubjectOptionBottomSheetDialog.SubjectOptionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSubjectOptionListener = null;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_subject, container, false);
        TextView title = v.findViewById(R.id.subject_title_modal_sheet);
        title.setText(getArguments().getString(ARG_TITLE));
        v.findViewById(R.id.delete_subject_option).setOnClickListener(this);
        v.findViewById(R.id.edit_title_option).setOnClickListener(this);
        v.findViewById(R.id.edit_attendance_option).setOnClickListener(this);
        v.findViewById(R.id.reset_attendance_option).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        String subjectTitle = getArguments().getString(ARG_TITLE);
        switch (v.getId()) {
            case R.id.delete_subject_option:
                mSubjectOptionListener.onDeleteSelected(subjectTitle);
                break;
            case R.id.edit_title_option:
                mSubjectOptionListener.onEditTitleSelected(subjectTitle);
                break;
            case R.id.edit_attendance_option:
                mSubjectOptionListener.onEditAttendanceSelected(subjectTitle);
                break;
            case R.id.reset_attendance_option:
                mSubjectOptionListener.onResetAttendanceSelected(subjectTitle);
                break;
        }
        dismiss();
    }
}
