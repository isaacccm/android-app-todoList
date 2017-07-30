package com.isaacchoi.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by isaac on 7/29/17.
 */

public class TodoDbHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "TODO_DB";
    public static final int DB_VER = 1;
    public static final String DB_TABLE = "Task";
    public static final String DB_COLUMN = "TodoName";


    public TodoDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create db table
        String query = String.format("CREATE TABLE %s " +
                                     "( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                     "  %s TEXT NOT NULL);"
                                     ,DB_TABLE
                                     ,DB_COLUMN);

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {

        //Delete the current table
        String query = String.format("DELETE TABLE IF EXISTS %s" ,DB_TABLE);
        sqLiteDatabase.execSQL(query);
        //Create new table
        onCreate(sqLiteDatabase);
    }

    public void createNewTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);
        db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public void deleteNewTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN +" = ?", new String[]{task});
        db.close();
    }

    public ArrayList<String> getTaskList(){
        ArrayList<String> taskList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN}, null, null, null, null, null );
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }
}
