package com.simorgh.database.dao;

import com.simorgh.database.model.User;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
@Keep
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("select * from users  where id=1")
    User getUser();

    @Query("select * from users  where id=1")
    LiveData<User> getUserLiveData();

}
