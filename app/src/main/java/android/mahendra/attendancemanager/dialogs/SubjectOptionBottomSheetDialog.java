package android.mahendra.attendancemanager.dialogs;

import android.mahendra.attendancemanager.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SubjectOptionBottomSheetDialog extends BottomSheetDialogFragment {
    private static final String ARG_TITLE = "subject title";

    public static SubjectOptionBottomSheetDialog newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        SubjectOptionBottomSheetDialog fragment = new SubjectOptionBottomSheetDialog();
        fragment.setArguments(args);
        return fragment;
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
        return v;
    }
}
