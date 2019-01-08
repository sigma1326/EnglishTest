package com.simorgh.database.dao;

import com.simorgh.database.model.Question;
import com.simorgh.database.model.YearMajorData;

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
public interface QuestionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Question> questions);

    @Query("select * from questions where id=:questionID")
    Question getQuestion(final int questionID);

    @Query("select * from questions where id=:questionID")
    LiveData<Question> getQuestionLive(final int questionID);

    @Query("select * from questions where major=:major and year_question =:year order by question_number")
    List<Question> getQuestions(final int major, final int year);

    @Query("select * from questions order by question_number")
    List<Question> getQuestions();

    @RawQuery(observedEntities = Question.class)
    List<YearMajorData> getYearMajorData(SupportSQLiteQuery rawQuery);

    @RawQuery(observedEntities = Question.class)
    List<Integer> getYears(SupportSQLiteQuery rawQuery);

    @Query("select * from questions where major=:major and year_question =:year order by question_number")
    LiveData<List<Question>> getQuestionsLiveData(final int major, final int year);

    @Query("delete from questions")
    void deleteAll();
}
