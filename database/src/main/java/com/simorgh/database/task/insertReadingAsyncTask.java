package com.simorgh.database.task;

import com.simorgh.database.dao.ReadingDAO;
import com.simorgh.database.model.Reading;

import java.util.List;

public final class insertReadingAsyncTask extends android.os.AsyncTask<List<Reading>, Void, Void> {

    private final ReadingDAO mAsyncTaskDao;

    public insertReadingAsyncTask(ReadingDAO dao) {
        mAsyncTaskDao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(List<Reading>... lists) {
        mAsyncTaskDao.insert(lists[0]);
        return null;
    }
}

