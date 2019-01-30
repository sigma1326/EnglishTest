package com.simorgh.database.dao;

import com.simorgh.database.model.Reading;

import java.util.List;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
@Keep
public interface ReadingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Reading reading);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Reading> reading);

    @Query("select * from readings where id=:readingID")
    Single<Reading> getReading(final int readingID);

    @Query("select * from readings where id=:readingID")
    LiveData<Reading> getReadingLive(final int readingID);

    @Query("select * from readings where major=:major and year =:year order by pnum")
    List<Reading> getReadings(final int major, final int year);


    @Query("select * from readings order by pnum")
    List<Reading> getReadings();

    @Query("select * from readings where major=:major and year =:year order by pnum")
    LiveData<List<Reading>> getReadingsLiveData(final int major, final int year);

    @Query("delete from readings")
    void deleteAll();
}
