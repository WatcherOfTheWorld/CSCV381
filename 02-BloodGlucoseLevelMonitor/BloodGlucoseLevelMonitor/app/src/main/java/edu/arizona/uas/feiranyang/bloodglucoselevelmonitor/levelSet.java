package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.database.BaseHelper;
import edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.database.DbSchema;
import edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.database.DbSchema.Table.Cols;
import edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.database.LevelCursorWrapper;


/*
  this obj store all blood glucose level data
 */
public class levelSet{
    private static levelSet sSet;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static levelSet get(Context context){
        if(sSet == null){
            sSet = new levelSet(context);
        }
        return sSet;
    }
    public levelSet(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new BaseHelper(mContext).getWritableDatabase();
        // create two hard coded data set
//        bloodGlucoseLevel level1 = new bloodGlucoseLevel(new GregorianCalendar(2001,7,20));
//        bloodGlucoseLevel level2 = new bloodGlucoseLevel(new GregorianCalendar(2011,7,20));
//        level1.setFasting(114);
//        level1.setBf(514);
//        level1.setLunch(810);
//        level1.setDinner(931);
//        level1.setNote("いいよ、来いよ");
//        level2.setFasting(99);
//        level2.setBf(99);
//        level2.setLunch(99);
//        level2.setDinner(99);
//        level2.setNote("早くしろよ");
//        add(level1);
//        add(level2);
//
//        bloodGlucoseLevel level3 = new bloodGlucoseLevel(Calendar.getInstance());
//        add(level3);
    }

    public void add(bloodGlucoseLevel level){
        // check if date is already exist. if do, remove the old one
        List<bloodGlucoseLevel> levels = getList();
        for(bloodGlucoseLevel item: levels){
            if(item.getDate().equals(level.getDate())){
                remove(item);
                break;
            }
        }
        ContentValues values = getContentValues(level);
        mDatabase.insert(DbSchema.Table.NAME,null,values);
    }

    public void remove(bloodGlucoseLevel level){
        String uuidString  = level.getID().toString();
        mDatabase.delete(DbSchema.Table.NAME,Cols.UUID+" = ?", new String[] {uuidString});
    }

    public void addToList(List<bloodGlucoseLevel> set, bloodGlucoseLevel level){
        for(int i = 0; i < set.size(); i++){
            if(set.get(i).compareTo(level)>0){
                set.add(0,level);
                return;
            } else if(set.get(i).compareTo(level) == 0){
                set.set(i,level);
                return;
            }else if(i+1< set.size()){
                if(set.get(i).compareTo(level)<0&& (set.get(i+1).compareTo(level)>0)){
                    set.add(i+1,level);
                    return;
                }
            }
        }
        set.add(level);
    }

    public void updateLevel(bloodGlucoseLevel level){
        String uuidString  = level.getID().toString();
        ContentValues values = getContentValues(level);
        mDatabase.update(DbSchema.Table.NAME, values, Cols.UUID+" = ?", new String[] {uuidString});
    }

    private static ContentValues getContentValues(bloodGlucoseLevel level){
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, level.getID().toString());
        values.put(Cols.TITLE, level.note);
        values.put(Cols.DATE, level.date.getTime().getTime());
        values.put(Cols.FASTING, level.fasting);
        values.put(Cols.BF, level.bf);
        values.put(Cols.LUNCH, level.lunch);
        values.put(Cols.DINNER, level.dinner);
        return  values;
    }


    public String toString(){
        String string = "" ;
        for(bloodGlucoseLevel level: getList()){
            string = string+level.toString()+";-=-=";
        }
        return string;
    }

    public bloodGlucoseLevel getLevel(UUID id){
        LevelCursorWrapper cursor = queryLevel(
                Cols.UUID+" + ?",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getLevel();
        }finally {
            cursor.close();
        }
    }

    public bloodGlucoseLevel checkDuplicate(String date){
//        Log.d("pre:",date);
        for (bloodGlucoseLevel level:getList()){
            //Log.d("ex",level.getDate());
            if(level.getDate().equals(date)){
                return level;
            }
        }
        return null;
    }

    public List<bloodGlucoseLevel> getList(){
        List<bloodGlucoseLevel> levels = new ArrayList<>();

        LevelCursorWrapper cursor = queryLevel(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                addToList(levels,cursor.getLevel());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }
        return levels;
    }

    private LevelCursorWrapper queryLevel(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                DbSchema.Table.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new LevelCursorWrapper(cursor);
    }

    public boolean getToday(){
        List<bloodGlucoseLevel> list = getList();
        Date date = Calendar.getInstance().getTime();
        String[] string = date.toString().split(" ");
        String today = string[1]+" "+string[2]+", "+string[5];

        for(bloodGlucoseLevel item: list){
            if(item.getDate().equals(today)){
                return checkInput(item);
            }
        }
        return false;
    }

    public boolean checkInput(bloodGlucoseLevel level){
        if(level.fasting == 0){
            return false;
        }else if(level.bf == 0){
            return false;
        }else if(level.lunch ==0){
            return false;
        }else if(level.dinner == 0){
            return false;
        }
        return true;
    }

}
