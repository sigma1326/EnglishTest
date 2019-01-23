package com.simorgh.database.model;

import com.simorgh.database.Date;

import java.util.Calendar;

import androidx.annotation.Keep;

@Keep
public class TestLog {
    private int year;
    private int major;
    private Date date;
    private int questionCount;
    private int blankCount;
    private int wrongCount;


    public TestLog(int year, int major, Date date, int questionCount, int blankCount, int wrongCount) {
        this.year = year;
        this.major = major;
        this.date = date;
        this.questionCount = questionCount;
        this.blankCount = blankCount;
        this.wrongCount = wrongCount;
    }

    public TestLog(int year, int major, Long date, int questionCount, int blankCount, int wrongCount) {
        this.year = year;
        this.major = major;
        this.date = new Date(date);
        this.questionCount = questionCount;
        this.blankCount = blankCount;
        this.wrongCount = wrongCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getBlankCount() {
        return blankCount;
    }

    public void setBlankCount(int blankCount) {
        this.blankCount = blankCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public Date getDate() {
        return date;
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, date.getHour());
        calendar.set(Calendar.MINUTE, date.getMinute());
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public int getCorrectCount() {
        return questionCount - blankCount - wrongCount;
    }

    public float getPercent() {
        int correct = getCorrectCount();
        if (correct <= 0) {
            return 0f;
        }
        return ((correct * 3f - wrongCount) / (questionCount * 3f)) * 100;
    }

}
