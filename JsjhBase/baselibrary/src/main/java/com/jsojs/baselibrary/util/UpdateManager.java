package com.jsojs.baselibrary.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by root on 16-10-28.
 */

public class UpdateManager {
    private static String firToken;
    private static UpdateManager instance;
    private static int oldVersion;
    private static String version;
    private static String url;
    private static String fileName;
    private Context context;
    private ProgressDialog pdialog;

    private UpdateManager(String firToken,int oldVersion,String fileName) {
        this.firToken =firToken;
        this.oldVersion = oldVersion;
        this.fileName = fileName;
        createDir();
    }

    public static UpdateManager getInstance(String firToken,int oldVersion,String fileName) {
        if(instance==null) {
            instance = new UpdateManager(firToken,oldVersion,fileName);
        }
        return instance;
    }

    public static UpdateManager getInstance() {
        return instance;
    }

    public static void checkUpdate() {
        if(firToken!=null&&oldVersion!=-1){
            FIR.checkForUpdateInFIR(firToken, new VersionCheckCallback() {
                @Override
                public void onSuccess(String versionJson) {
                    Log.i("shao","check from fir.im success! " + "\n" + versionJson);
                    try {
                        JSONObject jsonObject = new JSONObject(versionJson);
                        version = jsonObject.getString("version");
                        int intVersion = Integer.parseInt(version);
                        if(intVersion>oldVersion){
                            url = jsonObject.getString("installUrl");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(Exception exception) {
                    Log.i("shao", "check fir.im fail! " + "\n" + exception.getMessage());
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                }
            });
        }
    }

    public void Update(Context context){
        this.context = context;
        if(url!=null){
            if(pdialog==null){
                setPdialog();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示").setMessage("已有新版本，请下载更新");
            builder.setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new MyAsyncTask().execute(url);
                }
            });
            builder.setCancelable(false);
            builder.show();

        }
    }

    private void createDir() {
        File tmpFile = new File(Environment.getExternalStorageDirectory().getPath()+"/"+fileName+"/");
        File file = new File(tmpFile.getPath()+"/update/");
        Log.i("shao",tmpFile.getPath());
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        if(!file.exists()){
            file.mkdir();
        }
    }

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public class MyAsyncTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            /* 所下载文件的URL */
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
/* URL属性设置 */
                conn.setRequestMethod("GET");
/* URL建立连接 */
                conn.connect();
/* 下载文件的大小 */
                int fileOfLength = conn.getContentLength();
/* 每次下载的大小与总下载的大小 */
                int totallength = 0;
                int length = 0;
/* 输入流 */
                InputStream in = conn.getInputStream();
/* 输出流 */     final String fileName = "updata.apk";
                File tmpFile = new File(Environment.getExternalStorageDirectory().getPath()+"/"+UpdateManager.this.fileName+"/update/");
                File file = new File(tmpFile,fileName);
                FileOutputStream out = new FileOutputStream(file);
/* 缓存模式，下载文件 */
                byte[] buff = new byte[1024 * 1024];
                while ((length = in.read(buff)) > 0) {
                    totallength += length;
                    int newTotallength = totallength/1024;
                    int newFileOfLength = fileOfLength/1024;
                    String str1 = "" + (int) ((newTotallength * 100) / newFileOfLength);
                    publishProgress(str1);
                    out.write(buff, 0, length);
                }
                openFile(file);
/* 关闭输入输出流 */
                in.close();
                out.flush();
                out.close();


            } catch (MalformedURLException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }


        /* 预处理UI线程 */
        @Override
        protected void onPreExecute() {
            ((Activity)context).showDialog(0);
            super.onPreExecute();
        }


        /* 结束时的UI线程 */
        @Override
        protected void onPostExecute(String result) {
//            ((Activity)context).dismissDialog(0);
            super.onPostExecute(result);
        }


        /* 处理UI线程，会被多次调用,触发事件为publicProgress方法 */
        @Override
        protected void onProgressUpdate(String... values) {
/* 进度显示 */
            if(pdialog!=null)
            pdialog.setProgress(Integer.parseInt(values[0]));
        }

    }
    public Dialog getPdialog() {
        return pdialog;
    }

    public void setPdialog(){
            /* 实例化进度条对话框 */
        pdialog = new ProgressDialog(context);
/* 进度条对话框属性设置 */
        pdialog.setMessage("正在下载中...");
/* 进度值最大100 */
        pdialog.setMax(100);
/* 水平风格进度条 */
        pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
/* 无限循环模式 */
        pdialog.setIndeterminate(false);
/* 可取消 */
        pdialog.setCancelable(false);
/* 显示对话框 */
        pdialog.show();
    }
}
