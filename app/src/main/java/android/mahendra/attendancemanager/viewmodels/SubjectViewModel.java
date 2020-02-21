package android.mahendra.attendancemanager.viewmodels;

import android.mahendra.attendancemanager.models.Subject;
import android.mahendra.attendancemanager.repositories.SubjectRepository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubjectViewModel extends AndroidViewModel {

    private SubjectRepository mSubjectRepository;
    private LiveData<List<Subject>> mAllSubjects;

    public SubjectViewModel(Application application) {
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

    public void delete(Subject subject) { mSubjectRepository.delete(subject); }

    public void resetAttendance(Subject subject) {
        subject.setMissedClasses(0);
        subject.setAttendedClasses(0);
        mSubjectRepository.update(subject);
    }

    private List<String> extractTitles(List<Subject> subjects) {
        List<String> subjectTitles = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectTitles.add(subject.getTitle());
        }
        return subjectTitles;
    }

    public void onUpdateTitle(String oldTitle, String newTitle) {
        mSubjectRepository.updateTitle(oldTitle, newTitle);
    }

    public LiveData<List<String>> getSubjectTitles() {
        return Transformations.map(mAllSubjects, this::extractTitles);
    }

    public void onDeleteSubjectWith(String subjectTitle) {
        Subject subject = mSubjectRepository.getSubject(subjectTitle);
        mSubjectRepository.delete(subject);
    }

    public void onResetAttendance(String subjectTitle) {
        Subject subject = mSubjectRepository.getSubject(subjectTitle);
        subject.setAttendedClasses(0);
        subject.setMissedClasses(0);
        mSubjectRepository.update(subject);
    }

    public String getTitle(Subject subject) {
        return subject.getTitle();
    }

    public String getAttendanceStat(Subject subject) {
        String stat = String.
                format(Locale.ENGLISH,"%d / %d", subject.getAttendedClasses(), subject.getTotalClasses());
        return stat;
    }

    public String getAttendancePercentage(Subject subject) {
        if (subject.getTotalClasses() == 0) {
            return "0.0%";
        }
        double percentage = ((double) subject.getAttendedClasses() / subject.getTotalClasses()) * 100;
        return String.format(Locale.ENGLISH, "%.1f%%", percentage);
    }

    public int getAttendanceProgress(Subject subject) {
        if (subject.getTotalClasses() == 0) {
            return 0;
        }
        return (subject.getAttendedClasses() * 100) / subject.getTotalClasses();
    }

    public void onAttended(Subject subject) {
        subject.incrementClassesAttended();
        update(subject);
    }

    public void onMissed(Subject subject) {
        subject.incrementClassesMissed();
        update(subject);
    }
}
