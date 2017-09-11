package aavartan.pocketkharch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MY_PC on 31-Aug-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public  static final String DATABASE_NAME = "V1_0.db";
    public  static final String TABLE_NAME = "main_table";
    public  static final String COL_1 = "ID";
    public  static final String COL_2 = "DAY";
    public  static final String COL_3 = "MONTH";
    public  static final String COL_4 = "YEAR";
    public  static final String COL_5 = "CATEGORY";
    public  static final String COL_6 = "AMOUNT";
    public  static final String COL_7 = "DESCRIPTION";

    public  static final String TABLE_NAME_CAT = "cat_table";
    public  static final String COL_11 = "ID";
    public  static final String COL_12 = "CATEGORY";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table cat_table (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY TEXT)");
        sqLiteDatabase.execSQL("create table main_table (ID INTEGER PRIMARY KEY AUTOINCREMENT, DAY INTEGER,MONTH INTEGER,YEAR INTEGER, CATEGORY TEXT,AMOUNT REAL, DESCRIPTION TEXT)");
        sqLiteDatabase.execSQL("insert into cat_table(CATEGORY) values('FOOD');");
        sqLiteDatabase.execSQL("insert into cat_table(CATEGORY) values('TRANSPORTATION');");
        sqLiteDatabase.execSQL("insert into cat_table(CATEGORY) values('OTHERS');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME_CAT);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData_cat(String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_12,category);
        long result = db.insert(TABLE_NAME_CAT, null , contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getCat(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select category from "+TABLE_NAME_CAT,null);
        //db.execSQL("delete from cat_table where ID = 11");
        return res;
    }

    public Cursor getTotal(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select AMOUNT from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getTotalMonth(String month,String year){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select AMOUNT from "+TABLE_NAME+" where MONTH='"+month+"' and year='"+year+"';",null);
        return res;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getCatTotal(String Cat,String CurMonth,String CurYear){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select AMOUNT from "+TABLE_NAME+" where CATEGORY = '"+Cat+"' and MONTH = '"+CurMonth+"' and YEAR = '"+CurYear+"'",null);
        return res;
    }

    public boolean insertData(String DAY, String MONTH, String YEAR, String CATEGORY, String AMOUNT,String DESCRIPTION){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,DAY);
        contentValues.put(COL_3,MONTH);
        contentValues.put(COL_4,YEAR);
        contentValues.put(COL_5,CATEGORY);
        contentValues.put(COL_6,AMOUNT);
        contentValues.put(COL_7,DESCRIPTION);
        long result = db.insert(TABLE_NAME, null , contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void DeleteCat(String CatID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from cat_table where CATEGORY='"+CatID+"';");
    }

    public void UpdateCat(String CatID,String NewCatID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("update cat_table set CATEGORY='"+NewCatID+"' where CATEGORY='"+CatID+"';");
    }

    //Dump Query
    public void dataQuery(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
       // sqLiteDatabase.execSQL("ALTER TABLE main_table RENAME TO main_table_old;");
       // sqLiteDatabase.execSQL("create table main_table (ID INTEGER PRIMARY KEY AUTOINCREMENT, DAY INTEGER,MONTH INTEGER,YEAR INTEGER, CATEGORY TEXT,AMOUNT REAL, DESCRIPTION TEXT)");
    }

}

