package com.simorgh.database.model;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "users")
public class User {
    @ColumnInfo(name = "id")
    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "show_timer")
    private boolean showTimer;

    @ColumnInfo(name = "font_size")
    private int fontSize;


    public User() {
        id = 1;
        showTimer = true;
        fontSize = 14;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isShowTimer() {
        return showTimer;
    }

    public void setShowTimer(boolean showTimer) {
        this.showTimer = showTimer;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
