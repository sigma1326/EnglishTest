package com.simorgh.literaturetest.model;

import com.simorgh.database.Date;


public class Answer2 {
    private int questionId;

    private int currentAnswer;

    private int prevAnswer;

    private int trueAnswer;

    private int questionNumber;

    private Date date;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer(int currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public int getPrevAnswer() {
        return prevAnswer;
    }

    public void setPrevAnswer(int prevAnswer) {
        this.prevAnswer = prevAnswer;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
