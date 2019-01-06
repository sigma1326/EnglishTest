package com.simorgh.database.model;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "readings")
public class Reading {
    @ColumnInfo(name = "id")
    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "passage")
    private String passage;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "major")
    private int major;

    @ColumnInfo(name = "pnum")
    private int pnum;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassage() {
        return passage;
    }

    public void setPassage(String passage) {
        this.passage = passage;
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

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }
}
