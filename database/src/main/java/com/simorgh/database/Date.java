package com.simorgh.database;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@Keep
public class Date implements Parcelable {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int seconds;


    public Date(int year, int month, int day, int hour, int minute, int seconds) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.seconds = seconds;
    }

    public Date(long dateLong) {
        int length = String.valueOf(dateLong).length();
        if (length < 14) {
            if (length == 13) {
                dateLong *= 10;
            } else if (length == 12) {
                dateLong *= 100;
            } else if (length == 11) {
                dateLong *= 1000;
            } else if (length == 10) {
                dateLong *= 10000;
            }
        }
        year = Integer.parseInt(String.valueOf(dateLong).substring(0, 4));
        month = Integer.parseInt(String.valueOf(dateLong).substring(4, 6));
        day = Integer.parseInt(String.valueOf(dateLong).substring(6, 8));
        hour = Integer.parseInt(String.valueOf(dateLong).substring(8, 10));
        minute = Integer.parseInt(String.valueOf(dateLong).substring(10, 12));
        seconds = Integer.parseInt(String.valueOf(dateLong).substring(12, 14));
    }

    public Date(Calendar calendar, boolean clearHourMinuteSecond) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        seconds = calendar.get(Calendar.SECOND);
        if (clearHourMinuteSecond) {
            clearHourMinuteSeconds();
        }
    }

    public void setDateLong(long dateLong) {
        int length = String.valueOf(dateLong).length();
        if (length < 14) {
            if (length == 13) {
                dateLong *= 10;
            } else if (length == 12) {
                dateLong *= 100;
            } else if (length == 11) {
                dateLong *= 1000;
            } else if (length == 10) {
                dateLong *= 10000;
            }
        }
        year = Integer.parseInt(String.valueOf(dateLong).substring(0, 4));
        month = Integer.parseInt(String.valueOf(dateLong).substring(4, 6));
        day = Integer.parseInt(String.valueOf(dateLong).substring(6, 8));
        hour = Integer.parseInt(String.valueOf(dateLong).substring(8, 10));
        minute = Integer.parseInt(String.valueOf(dateLong).substring(10, 12));
        seconds = Integer.parseInt(String.valueOf(dateLong).substring(12, 14));
    }

    public void clearHourMinuteSeconds() {
        hour = 0;
        minute = 0;
        seconds = 0;
    }

    public void setCalendar(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        seconds = calendar.get(Calendar.SECOND);
    }

    public Date(final Calendar calendar) {
        if (calendar != null) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
        }
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
        seconds = in.readInt();
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
        dest.writeInt(seconds);
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

    public long getDateLong() {
        long ret;
        ret = year;
        ret = ret * 100 + month;
        ret = ret * 100 + day;
        ret = ret * 100 + hour;
        ret = ret * 100 + minute;
        ret = ret * 100 + seconds;
        return ret;
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, seconds);
        return calendar;
    }

    @SuppressLint("DefaultLocale")
    @NotNull
    public String toString() {
        return String.format("date = {%04d/%02d/%02d - %02d:%02d:%02d}", year, month, day, hour, minute, seconds);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        return getDateLong() == ((Date) Objects.requireNonNull(obj)).getDateLong();
    }

    public void setYearMonthDay(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
        hour = 0;
        minute = 0;
        seconds = 0;
    }

    public boolean isToday() {
        Date today = new Date(Calendar.getInstance(), true);
        return today.getDateLong() == getDateLong();
    }
}
