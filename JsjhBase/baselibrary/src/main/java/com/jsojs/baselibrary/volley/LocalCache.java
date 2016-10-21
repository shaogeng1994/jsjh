package com.jsojs.baselibrary.volley;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/12.
 */
public class LocalCache implements ImageLoader.ImageCache {
    private File file;
    @Override
    public Bitmap getBitmap(String url) {
        String[] strings = url.split("/");
        url = strings[strings.length-1];
        file = new File(Environment.getDataDirectory().getPath()+"cache/"+url);
        if(file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                return BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String[] strings = url.split("/");
        url = strings[strings.length-1];
        file = new File(Environment.getDataDirectory().getPath()+"/data/com.jsojs.single.app/cache/"+url);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
