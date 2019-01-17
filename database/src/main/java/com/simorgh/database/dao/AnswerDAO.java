package com.simorgh.database.dao;

import com.simorgh.database.Date;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;

import java.util.List;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
@Keep
public interface AnswerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Answer answer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Answer> answers);

    @Query("select * from answers where question_id=:questionID")
    Answer getAnswer(final int questionID);
//    Answer getAnswer(final int major, final int year, final Date testDate, final int questionID);

    @Query("select * from answers where question_id=:questionID")
    LiveData<Answer> getAnswerLive(final int questionID);
//    LiveData<Answer> getAnswerLive(final int major, final int year, final Date testDate, final int questionID);

    @Query("select * from answers where date =:testDate and question_id in (select id from questions where major=:major and year_question =:year order by question_number)")
    List<Answer> getAnswers(final int major, final int year, final Date testDate);

    @Query("select * from answers where date =:testDate and question_id in (select id from questions where major=:major and year_question =:year order by question_number)")
    LiveData<List<Answer>> getAnswersLiveData(final int major, final int year, final Date testDate);

    @Query("delete from answers")
    void deleteAll();

    @Query("select * from answers where date =:testDate order by fakeID")
    List<Answer> getAnswers(final Date testDate);


    @RawQuery(observedEntities = Answer.class)
    List<Long> getAnswerDates(SupportSQLiteQuery rawQuery);
}
