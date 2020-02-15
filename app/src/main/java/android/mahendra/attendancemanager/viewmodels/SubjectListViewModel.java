package android.mahendra.attendancemanager.viewmodels;

import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.repositories.SubjectRepository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

public class SubjectListViewModel extends AndroidViewModel implements Subject.Callback {
    private static final String TAG = "SubjectListViewModel";

    private SubjectRepository mSubjectRepository;
    private LiveData<List<Subject>> mAllSubjects;

    public SubjectListViewModel(Application application) {
        super(application);
        mSubjectRepository = new SubjectRepository(application);
        mAllSubjects = mSubjectRepository.getAllSubjects();
    }

    public LiveData<List<Subject>> getAllSubjects() {
        return mAllSubjects;
    }

    public void insert(Subject subject) {
        mSubjectRepository.insert(subject);
    }

    public void update(Subject subject) {
        mSubjectRepository.update(subject);
    }

    @Override
    public void onAttended(Subject subject) {
        update(subject);
    }

    @Override
    public void onMissed(Subject subject) {
        update(subject);
    }

    @Override
    public void onCancelled(Subject subject) {
        update(subject);
    }

    private List<String> extractTitles(List<Subject> subjects) {
        List<String> subjectTitles = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectTitles.add(subject.getTitle());
        }
        return subjectTitles;
    }

    public LiveData<List<String>> getSubjectTitles() {
        return Transformations.map(mAllSubjects, this::extractTitles);
    }
}
