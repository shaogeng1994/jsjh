package com.jsojs.baselibrary.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by root on 16-10-6.
 */

public class VolleyRequestQueue {
    private RequestQueue requestQueue;
    private static VolleyRequestQueue instance;
    private VolleyRequestQueue(Context context){
        if(requestQueue==null)requestQueue = Volley.newRequestQueue(context);
    }
    public static VolleyRequestQueue getInstance(Context context){
        if(instance==null)instance = new VolleyRequestQueue(context);
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
