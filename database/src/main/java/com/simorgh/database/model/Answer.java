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

    @ColumnInfo(name = "true_answer")
    private int trueAnswer;

    @ColumnInfo(name = "question_number")
    private int questionNumber;

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

    public int getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(int trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Answer(int questionId, int answer, int trueAnswer, int questionNumber, Date date) {
        this.questionId = questionId;
        this.answer = answer;
        this.trueAnswer = trueAnswer;
        this.questionNumber = questionNumber;
        this.date = date;
    }

    public boolean isCorrect() {
        return answer == trueAnswer;
    }
}
