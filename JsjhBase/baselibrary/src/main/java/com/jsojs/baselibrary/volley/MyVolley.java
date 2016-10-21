package com.jsojs.baselibrary.volley;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jsojs.baselibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/7/9.
 */
public  class MyVolley {
    private static MyVolley mInstance;
    private static View view;
    private RequestQueue mRequestQueue;
    private static Context context;
    private MyVolley(Context context){
        this.context = context;
        if (mRequestQueue == null){
            mRequestQueue= Volley.newRequestQueue(context);
        }
    }

    public static MyVolley getInstance(Context context){
        if(mInstance==null){
            mInstance = new MyVolley(context);
        }
        return mInstance;
    }
    public void addRequest(String url, final Map<String,String> map, final GetResponseLintener getResponseLintener){
        final HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("sysversion","2");
        //hashMap.put("supplier_id","1160");
        hashMap.put("appversion","1.0.4");
        hashMap.put("source","1");
        hashMap.putAll(map);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("shao",s);
                try {
                    s = s.replace("null","\"\"");
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.has("code")){
                        String code = jsonObject.getString("code");
                        if(!code.equals("101")&&!code.equals("102")&&!code.equals("103")){
                            getResponseLintener.getResponse(new JSONObject(s));
                        }else{
                            Intent intent = new Intent("tokenError");
                            intent.putExtra("isTokenOut",true);
                            context.sendBroadcast(intent);
                        }
                    }else{
                        getResponseLintener.getResponse(new JSONObject(s));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("shao",volleyError.getMessage(),volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000, 1, 1.0f));
        mRequestQueue.add(stringRequest);
    }

    public void addRequest(String url, final Map<String,String> map, final GetStringResponseLintener getStringResponseLintener){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                getStringResponseLintener.getResponse(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("shao",volleyError.getMessage(),volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000, 1, 1.0f));
        mRequestQueue.add(stringRequest);
    }

    public void addJSONRequest(String url, JSONObject jsonObject, final GetResponseLintener getResponseLintener){
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                getResponseLintener.getResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("shao",volleyError.getMessage(),volleyError);
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonRequest);
    }

    public interface GetResponseLintener{
        public void getResponse(JSONObject jsonObject);
    }

    public interface GetStringResponseLintener{
        public void getResponse(String s);
    }

    public void loadImageByVolley(ImageView imageView, String imageUrl,boolean hasDefault){
        final BitmapCache lruCache=new BitmapCache();
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.putBitmap(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.getBitmap(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, imageCache);
        ImageLoader.ImageListener listener;
        if(hasDefault)
            listener = ImageLoader.getImageListener(
                imageView, R.color.colorGray,0);
        else
            listener = ImageLoader.getImageListener(
                    imageView, 0,0);
        if(imageUrl!=null)
        imageLoader.get(imageUrl, listener);
    }

    public void loadImageByVolley(ImageView imageView, String imageUrl){
        loadImageByVolley(imageView,imageUrl,false);
    }

    public ImageLoader getImageLoader(){
        ImageLoader imageLoader = new ImageLoader(mRequestQueue,new BitmapCache());
        return imageLoader;
    }


    public  void loadNetworkImage(String url, final GetImgLintener getImgLintener){
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        getImgLintener.getImg(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("shao",error.getMessage(),error);
            }
        });
        mRequestQueue.add(imageRequest);

    }

    public interface GetImgLintener{
        public void getImg(Bitmap bitmap);
    }

    public void displayMyImage(final String imgurl, final ImageView imgView, int defaultImg, final boolean isSave){
        final BitmapCache bitmapCache = new BitmapCache();
        final LocalCache localCache = new LocalCache();
        if(bitmapCache.getBitmap(imgurl)!=null){
            imgView.setImageBitmap(bitmapCache.getBitmap(imgurl));
        }else if(localCache.getBitmap(imgurl)!=null){
            imgView.setImageBitmap(localCache.getBitmap(imgurl));
            bitmapCache.putBitmap(imgurl,localCache.getBitmap(imgurl));
        }else {
            ImageLoader imageLoader = new ImageLoader(mRequestQueue,bitmapCache);
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imgView,defaultImg,0);
            imageLoader.get(imgurl,listener);
            if(isSave&&imgView.getDrawingCache()!=null)localCache.putBitmap(imgurl,imgView.getDrawingCache());
        }
    }


}
