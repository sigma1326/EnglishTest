package com.simorgh.database;

import com.simorgh.database.dao.QuestionDAO;
import com.simorgh.database.dao.ReadingDAO;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.Reading;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Question.class, Reading.class}, version = 2, exportSchema = false)
public abstract class ImportDataBase extends RoomDatabase {

    public abstract ReadingDAO readingDAO();

    public abstract QuestionDAO questionDAO();

}
