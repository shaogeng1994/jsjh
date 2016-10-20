package com.jsojs.baselibrary.myview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsojs.b2b.app.R;
import com.jsojs.b2b.javabean.City;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CityViewAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<City> list;

    public CityViewAdapter(Context context, List<City> list){
        this.context = context;
        this.layoutInflater= LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(list.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.my_bottomsheet_item,null);
            holder.textView = (TextView) convertView.findViewById(R.id.my_bottomsheet_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position).getName());
        return convertView;
    }

    private class ViewHolder{
        public TextView textView;
    }
}
