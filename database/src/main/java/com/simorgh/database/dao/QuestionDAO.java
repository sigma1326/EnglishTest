package com.simorgh.database.dao;

import com.simorgh.database.model.Question;

import java.util.List;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("select * from questions where major=:major and year_question =:year order by question_number")
    LiveData<List<Question>> getQuestionsLiveData(final int major, final int year);

    @Query("delete from questions")
    void deleteAll();
}
