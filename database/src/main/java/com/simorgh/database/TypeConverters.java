package com.simorgh.database;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

public class TypeConverters {
    @Nullable
    @TypeConverter
    public static Date toDate(@Nullable Long value) {
        if (value == null) {
            return null;
        } else {
            if (String.valueOf(value).length() < 8) {
                return new Date(1970, 0, 1, 0, 0,0);
            }
            return new Date(value);
        }
    }

    @TypeConverter
    public static long toMillis(@NonNull Date date) {
        return date.getDateLong();
    }
}
