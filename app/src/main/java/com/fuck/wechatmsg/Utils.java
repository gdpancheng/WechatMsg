package com.fuck.wechatmsg;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public final static String DEST_DIR = "/data/data/com.fuck.wechatmsg";

    public static List<Message> getAllMessage(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DEST_DIR+"/SnsMicroMsg.db",null,0);
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

    public static void mvDb() throws Exception{
        String dataDir = Environment.getDataDirectory().getAbsolutePath();

        Process su = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(su.getOutputStream());
        os.writeBytes("mount -o remount,rw " + dataDir + "\n");
        os.writeBytes("cd " +  "/data/data/com.tencent.mm/MicroMsg\n");
        os.writeBytes("ls | while read line; do cp ${line}/SnsMicroMsg.db " + DEST_DIR + "/ ; done \n");
        os.writeBytes("sleep 1\n");
        os.writeBytes("chmod 777 " + DEST_DIR + "/SnsMicroMsg.db\n");
        os.writeBytes("exit\n");
        os.flush();
        os.close();
        Thread.sleep(1000);
    }

    public static void clearDb() throws  Exception{
        String dataDir = Environment.getDataDirectory().getAbsolutePath();

        Process su = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(su.getOutputStream());
        os.writeBytes("mount -o remount,rw " + dataDir + "\n");
        os.writeBytes("cd /data/data/com.tencent.mm/MicroMsg\n");
        os.writeBytes("ls | while read line; do rm -rf ${line}/SnsMicroMsg.db*; done\n");
        os.writeBytes("exit\n");
        os.flush();
        os.close();
        Thread.sleep(1000);
    }

}
