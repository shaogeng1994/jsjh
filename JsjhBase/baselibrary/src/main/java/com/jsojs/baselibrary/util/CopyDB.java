package com.jsojs.baselibrary.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CopyDB {
    private static String packageName;// xml中配置的:"com.ttdevs.citydata"
    private static String dbName = "jsdb.db";
    private static String dbPath = null;

    public static void copyDataBase(int where,Context context) {

        // 每个应用都有一个数据库目录，他位于 /data/data/yourpackagename/databases/目录下
        packageName =context.getPackageName();
        if (where == 1) { // sdcard
            dbPath = Environment.getExternalStorageDirectory() + File.separator + dbName;
        } else { // local
            dbPath = "/data/data/" + packageName + "/databases/" + dbName;
        }

        if (where == 2) {
            new File("/data/data/" + packageName + "/databases/").mkdirs();
        }

        if (where == 1 && !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return; // 未挂载外部存储，拷贝到内部不用判断
        }

        File dbFile = new File(dbPath);
        if (dbFile.exists()) {

        }
        try {
            dbFile.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        try {
            InputStream is = context.getResources().getAssets().open(dbName);
            OutputStream os = new FileOutputStream(dbPath);

            byte[] buffer = new byte[4000];
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("shao","拷贝成功");
    }

    public static List<City> getData(Context context, String upid) {

        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            Toast.makeText(context, "找不到数据包", Toast.LENGTH_LONG).show();
            return null;
        }

        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        StringBuffer sb = new StringBuffer();
        Cursor cursor;
        if(upid.equals("0")){
            cursor = db.rawQuery("select * from jsjh_district where level='"+1+"'", null);
        }else {
            cursor = db.rawQuery("select * from jsjh_district where upid='"+upid+"'", null);
        }
        List<City> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            City city = new City();
            city.setId(cursor.getInt(0)+"");
            city.setName(cursor.getString(1));
            city.setLevel(cursor.getInt(2)+"");
            city.setUid(cursor.getString(4));
            list.add(city);
        }
        return list;
    }

    public static List<City> getCitys(Context context , String id){
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            Toast.makeText(context, "找不到数据包", Toast.LENGTH_LONG).show();
            return null;
        }

        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        StringBuffer sb = new StringBuffer();
        Cursor cursor;
        cursor = db.rawQuery("select * from jsjh_district where id='"+id+"'", null);
        List<City> list = new ArrayList<>();
        cursor.moveToFirst();
        while(true){
            City city = new City();
            city.setId(cursor.getInt(0)+"");
            city.setName(cursor.getString(1));
            city.setLevel(cursor.getInt(2)+"");
            city.setUid(cursor.getString(4));
            list.add(city);
            if(city.getLevel().equals("1")){
                break;
            }
            cursor = db.rawQuery("select * from jsjh_district where id='"+city.getUid()+"'", null);
            cursor.moveToFirst();
        }
        return list;
    }
}
