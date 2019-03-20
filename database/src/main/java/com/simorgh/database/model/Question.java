package com.simorgh.database.model;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
@Keep
public class Question {
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "year_question")
    private int yearQuestion;

    @ColumnInfo(name = "major")
    private int major;

    @ColumnInfo(name = "question")
    private String question;


    @ColumnInfo(name = "answer1")
    private String answer1;

    @ColumnInfo(name = "answer2")
    private String answer2;

    @ColumnInfo(name = "answer3")
    private String answer3;


    @ColumnInfo(name = "answer4")
    private String answer4;

    @ColumnInfo(name = "true_answer")
    private int trueAnswer;

    @ColumnInfo(name = "question_number")
    private int questionNumber;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYearQuestion() {
        return yearQuestion;
    }

    public void setYearQuestion(int yearQuestion) {
        this.yearQuestion = yearQuestion;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
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

}
