package com.simorgh.database.model;

import androidx.annotation.Keep;

@Keep
public class YearMajorData {
    private int year;
    private int major;
    private int questionCount;

    public YearMajorData(int year, int major, int questionCount) {
        this.year = year;
        this.major = major;
        this.questionCount = questionCount;
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

    public static String getMajorType(final int major) {
        switch (major) {
            case 0:
                return "ریاضی فنی";
            case 1:
                return " علوم تجربی";
            case 2:
                return "علوم انسانی";
            case 3:
                return "زبان";
            case 4:
                return "هنر";
            default:
                return "";
        }
    }
    public static int getMajorTime(final int major) {
        switch (major) {
            case 0:
            case 1:
            case 2:
            case 4:
                return 20;
            case 3:
                return 105;
            default:
                return -1;
        }
    }

}
