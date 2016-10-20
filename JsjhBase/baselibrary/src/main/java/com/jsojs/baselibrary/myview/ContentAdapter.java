package com.jsojs.baselibrary.myview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.jsojs.b2b.app.R;
import com.jsojs.b2b.javabean.GoodsUnderBrand;

import java.util.ArrayList;
import java.util.HashMap;

import jsjhAPI.MyVolley;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Shao on 2016/7/1.
 */
public class ContentAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private LayoutInflater mInflater;
    private ArrayList<GoodsUnderBrand> list;
    private static final int TYPE_COUNT = 2;
    private static  final int TYPE_TITLE = 0;
    private static  final int TYPE_CONTENT =1;
    private int currentType;
    private Context context;
    public ContentAdapter(Context context, ArrayList<GoodsUnderBrand> list){
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        if(list==null)return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        HashMap<String,String> map = new HashMap<>();
        map.put("id",list.get(position).getId());
        map.put("price",list.get(position).getPrice());
        return map;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.goodsclass__lv_right,null);
            holder.contentItem = (TextView) convertView.findViewById(R.id.goodsclass_content_item);
            holder.imageView = (NetworkImageView) convertView.findViewById(R.id.goodsclass_content_img);
            holder.priceTV = (TextView) convertView.findViewById(R.id.goodsclass_content_price);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.contentItem.setText(list.get(position).getTitle());
        isHavePrice(holder.priceTV,position);
        String url = list.get(position).getImgurl();
        if(!url.equals("")&&url!=null){
            holder.imageView.setDefaultImageResId(R.mipmap.sy_img_cpt);
            holder.imageView.setImageUrl(url,MyVolley.getInstance(context).getImageLoader());
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.goodsclass_item,parent,false);
        }
        ((TextView)(convertView)).setText(list.get(position).getBaseBrand());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return list.get(position).getBrandId();
    }

    private class ViewHolder{
        public TextView contentItem;
        public NetworkImageView imageView;
        public TextView priceTV;
    }
    private  void isHavePrice(TextView tv, int position){
        String price = list.get(position).getPrice();
        if(price==null||price.equals("")){
            tv.setText("当前地区暂无配送");
            tv.setTextColor(context.getResources().getColor(R.color.colorGrayFont));
        }else{
            tv.setText("￥"+price+"/"+list.get(position).getUnitText());
            tv.setTextColor(Color.RED);
        }
    }


}