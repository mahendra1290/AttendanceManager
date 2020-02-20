package android.mahendra.attendancemanager.repositories;

import android.mahendra.attendancemanager.daos.SubjectDao;

import android.app.Application;
import android.mahendra.attendancemanager.database.MainDatabase;
import android.mahendra.attendancemanager.models.Subject;


import androidx.lifecycle.LiveData;

import java.util.List;

public class SubjectRepository {

    private SubjectDao mSubjectDao;

    private LiveData<List<Subject>> mAllSubjects;

    public SubjectRepository(Application application) {
        MainDatabase db = MainDatabase.getDatabase(application);
        mSubjectDao = db.subjectDao();
        mAllSubjects = mSubjectDao.getAllSubjects();

    }

    public LiveData<List<Subject>> getAllSubjects() {
        return mAllSubjects;
    }

    public void updateTitle(Subject subject, String newTitle) {
        MainDatabase.databaseWriteExecutor.execute(() -> mSubjectDao.updateTitle(subject.getTitle(), newTitle));
    }

    public void insert(Subject subject) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            mSubjectDao.insert(subject);
        });
    }

    public void update(Subject subject) {
        MainDatabase.databaseWriteExecutor.execute(() -> {
            mSubjectDao.update(subject);
        });
    }
}
