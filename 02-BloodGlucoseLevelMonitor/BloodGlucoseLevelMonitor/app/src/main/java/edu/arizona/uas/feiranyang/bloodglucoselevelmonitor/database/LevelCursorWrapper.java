package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.bloodGlucoseLevel;
import edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.database.DbSchema.Table;

public class LevelCursorWrapper extends CursorWrapper {
    public LevelCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public bloodGlucoseLevel getLevel(){
        String uuidString = getString(getColumnIndex(Table.Cols.UUID));
        String title = getString(getColumnIndex(Table.Cols.TITLE));
        long date = getLong(getColumnIndex(Table.Cols.DATE));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date));
        bloodGlucoseLevel level = new bloodGlucoseLevel(cal, UUID.fromString(uuidString));
        level.setNote(title);
        level.setFasting(getInt(getColumnIndex(Table.Cols.FASTING)));
        level.setBf(getInt(getColumnIndex(Table.Cols.BF)));
        level.setLunch(getInt(getColumnIndex(Table.Cols.LUNCH)));
        level.setDinner(getInt(getColumnIndex(Table.Cols.DINNER)));
        return level;
    }
}
