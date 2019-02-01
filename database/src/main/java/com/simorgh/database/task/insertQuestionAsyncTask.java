package com.simorgh.database.task;

import com.simorgh.database.dao.QuestionDAO;
import com.simorgh.database.model.Question;

import java.util.List;

public final class insertQuestionAsyncTask extends android.os.AsyncTask<List<Question>, Void, Void> {

    private final QuestionDAO mAsyncTaskDao;

    public insertQuestionAsyncTask(QuestionDAO dao) {
        mAsyncTaskDao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(List<Question>... lists) {
        mAsyncTaskDao.insert(lists[0]);
        return null;
    }
}

