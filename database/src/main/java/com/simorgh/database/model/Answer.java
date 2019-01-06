package com.simorgh.database.model;

import com.simorgh.database.Date;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "answers")
@Keep
public class Answer {
    @PrimaryKey(autoGenerate = true)
    private long fakeID;

    @ColumnInfo(name = "question_id")
    private int questionId;

    @ColumnInfo(name = "answer")
    private int answer;

    @ColumnInfo(name = "date")
    private Date date;


    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getFakeID() {
        return fakeID;
    }

    public void setFakeID(long fakeID) {
        this.fakeID = fakeID;
    }
}
