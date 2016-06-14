package com.fuck.wechatmsg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper{

    public DBOpenHelper(Context context,String dbfile){
        super(context,dbfile,null,1);
    }

    public List<Message> getAllMessage(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select content,attrBuf from SnsInfo",null);
        List<Message> list = new ArrayList<>();
        while(cursor.moveToNext()){
            byte[] content = cursor.getBlob(cursor.getColumnIndex("content"));
            byte[] attr = cursor.getBlob(cursor.getColumnIndex("attrBuf"));
            Message msg = Message.parse(content,attr);
            list.add(msg);
        }
        db.close();
        return list;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
