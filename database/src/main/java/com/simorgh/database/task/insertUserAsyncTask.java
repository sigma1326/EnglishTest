package com.simorgh.database.task;

import com.simorgh.database.dao.UserDAO;
import com.simorgh.database.model.User;

import androidx.annotation.Nullable;

public final class insertUserAsyncTask extends android.os.AsyncTask<User, Void, Void> {
    private UserDAO mAsyncTaskDao;

    public insertUserAsyncTask(UserDAO dao) {
        mAsyncTaskDao = dao;
    }

    @Nullable
    @Override
    protected Void doInBackground(final User... params) {
        mAsyncTaskDao.insert(params[0]);
        return null;
    }
}

