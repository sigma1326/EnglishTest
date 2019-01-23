package com.simorgh.database;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public class Date implements Parcelable {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;


    public Date(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Date(final long milli) {
        year = Integer.parseInt(String.valueOf(milli).substring(0, 4));
        month = Integer.parseInt(String.valueOf(milli).substring(4, 6));
        day = Integer.parseInt(String.valueOf(milli).substring(6, 8));
        hour = Integer.parseInt(String.valueOf(milli).substring(8, 10));
        minute = Integer.parseInt(String.valueOf(milli).substring(10, 12));
    }

    public Date(final Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    public static final Creator<Date> CREATOR = new Creator<Date>() {
        @Override
        public Date createFromParcel(@NonNull Parcel in) {
            return new Date(in);
        }

        @Override
        public Date[] newArray(int size) {
            return new Date[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Date(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public long getMilli() {
        long ret;
        ret = year;
        ret = ret * 100 + month;
        ret = ret * 100 + day;
        ret = ret * 100 + hour;
        ret = ret * 100 + minute;
        return ret;
    }

    @SuppressLint("DefaultLocale")
    @NotNull
    public String toString() {
        return String.format("date = {%04d/%02d/%02d - %02d:%02d}", year, month, day, hour, minute);
    }
}
