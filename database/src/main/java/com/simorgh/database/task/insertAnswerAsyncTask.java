package com.simorgh.database.task;

import com.simorgh.database.dao.AnswerDAO;
import com.simorgh.database.model.Answer;

import java.util.List;

public final class insertAnswerAsyncTask extends android.os.AsyncTask<List<Answer>, Void, Void> {

    private AnswerDAO mAsyncTaskDao;

    public insertAnswerAsyncTask(AnswerDAO dao) {
        mAsyncTaskDao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(List<Answer>... lists) {
        mAsyncTaskDao.insert(lists[0]);
        return null;
    }
}

