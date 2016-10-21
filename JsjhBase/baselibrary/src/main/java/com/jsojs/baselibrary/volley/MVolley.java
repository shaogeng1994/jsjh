    package com.jsojs.baselibrary.volley;

    import android.app.Dialog;
    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.support.v4.widget.SwipeRefreshLayout;
    import android.util.Log;
    import android.widget.ImageView;

    import com.android.volley.AuthFailureError;
    import com.android.volley.DefaultRetryPolicy;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.ImageLoader;
    import com.android.volley.toolbox.ImageRequest;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;
    import com.jsojs.baselibrary.R;
    import com.jsojs.baselibrary.myview.MyDialog;
    import com.jsojs.baselibrary.util.MyToast;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.HashMap;
    import java.util.Map;


    /**
     * Created by Administrator on 2016/8/17.
     */
    public  class MVolley {
        private static MVolley mInstance;
        private RequestQueue mRequestQueue;
        private Context context;
        private static Dialog dialog;
        private MVolley(Context context){
            this.context = context;
            if (mRequestQueue == null){
                mRequestQueue= Volley.newRequestQueue(context);
            }
        }

        public static MVolley getInstance(Context context){

            if(mInstance==null){
                mInstance = new MVolley(context);
            }else{
                mInstance.setContext(context);
            }
            return mInstance;
        }

        public void setContext(Context context){
            this.context = context;
        }
        public void addRequest(String url, final Map<String,String> map, final GetResponseLintener getResponseLintener, final GetErrorLintener getErrorLintener, boolean hasDialog, final SwipeRefreshLayout swipeRefreshLayout){
            final Dialog dialog = MyDialog.createLoadingDialog(context);
            if(hasDialog)dialog.show();
            else dialog.cancel();
            final HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("sysversion","2");
            hashMap.put("appversion","1.0.4");
            hashMap.put("source","1");
            hashMap.putAll(map);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if(dialog.isShowing())dialog.cancel();
                    if(swipeRefreshLayout!=null)swipeRefreshLayout.setRefreshing(false);
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
                    if(dialog.isShowing())dialog.cancel();
                    if(swipeRefreshLayout!=null)swipeRefreshLayout.setRefreshing(false);
                    if(volleyError.networkResponse!=null){
                        Log.i("shao","statusCode:"+volleyError.networkResponse.statusCode);
                        switch (volleyError.networkResponse.statusCode){
                            case 504:
                                MyToast.makeToast(context,"请求超时");
                                break;
                            case 404:
                                MyToast.makeToast(context,"找不到服务器");
                                break;
                            case 500:
                                MyToast.makeToast(context,"服务器未响应");
                                break;
                        }
                    }
                    Log.i("shao","volleyError:"+volleyError.toString());
                    if(volleyError.getMessage()!=null){
                        Log.i("shao",volleyError.getMessage());
                        if(volleyError.getMessage().startsWith("java.net.ConnectException")){
                            MyToast.makeToast(context,"连接失败");
                        }else if(volleyError.getMessage().startsWith("java.net.UnknownHostException")){
                            MyToast.makeToast(context,"找不到服务器");
                        }else{
                            MyToast.makeToast(context,"发生异常");
                        }
                    }
                    if(getErrorLintener!=null){
                        getErrorLintener.onError();
                    }

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return hashMap;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 1, 1.0f));
            mRequestQueue.add(stringRequest);
        }

        public void addRequest(String url, final Map<String,String> map, final GetResponseLintener getResponseLintener,boolean hasDialog,SwipeRefreshLayout swipeRefreshLayout){
            addRequest(url,map,getResponseLintener,null,hasDialog,swipeRefreshLayout);
        }

        public void addRequest(String url, final Map<String,String> map, final GetResponseLintener getResponseLintener,GetErrorLintener getErrorLintener){
            addRequest(url,map,getResponseLintener,getErrorLintener,true);
        }

        public void addRequest(String url, final Map<String,String> map, final GetResponseLintener getResponseLintener){
            addRequest(url,map,getResponseLintener,null,true);
        }

        public void addRequest(String url, final Map<String,String> map, final GetResponseLintener getResponseLintener,boolean hasDialog){
            addRequest(url,map,getResponseLintener,null,hasDialog,null);
        }

        public void addRequest(String url, final Map<String,String> map, final GetResponseLintener getResponseLintener,GetErrorLintener getErrorLintener,boolean hasDialog){
            addRequest(url,map,getResponseLintener,getErrorLintener,hasDialog,null);
        }

        public void addRequest(String url, final Map<String,String> map, final GetStringResponseLintener getStringResponseLintener){
            final Dialog dialog = MyDialog.createLoadingDialog(context);
            final HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("sysversion","2");
            hashMap.put("appversion","1.0.4");
            hashMap.put("source","1");
            hashMap.putAll(map);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if(dialog.isShowing())dialog.cancel();
                    getStringResponseLintener.getResponse(s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if(dialog.isShowing())dialog.cancel();
                    if(volleyError.networkResponse!=null){
                        Log.i("shao","statusCode:"+volleyError.networkResponse.statusCode);
                        switch (volleyError.networkResponse.statusCode){
                            case 504:
                                MyToast.makeToast(context,"请求超时");
                                break;
                            case 404:
                                MyToast.makeToast(context,"找不到服务器");
                                break;
                            case 500:
                                MyToast.makeToast(context,"服务器未响应");
                                break;
                        }
                    }
                    Log.i("shao","volleyError:"+volleyError.toString());
                    if(volleyError.getMessage()!=null){
                        Log.i("shao",volleyError.getMessage());
                        if(volleyError.getMessage().startsWith("java.net.ConnectException")){
                            MyToast.makeToast(context,"连接失败");
                        }else if(volleyError.getMessage().startsWith("java.net.UnknownHostException")){
                            MyToast.makeToast(context,"找不到服务器");
                        }else{
                            MyToast.makeToast(context,"发生异常");
                        }
                    }
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


        public interface GetResponseLintener{
            public void getResponse(JSONObject jsonObject);
        }

        public interface GetErrorLintener{
            public void onError();
        }

        public interface GetStringResponseLintener{
            public void getResponse(String s);
        }

        public void loadImageByVolley(ImageView imageView, String imageUrl){
            if(dialog!=null)dialog.cancel();
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
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(
                    imageView, R.color.colorGray ,0);

            if(imageUrl!=null)
                imageLoader.get(imageUrl, listener);
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
