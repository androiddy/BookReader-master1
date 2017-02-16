package com.appbook.book.AppAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appbook.book.BookDetailActivity;
import com.appbook.book.MainActivity;
import com.appbook.book.R;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.widget.PicassoUtlis;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/10/18.
 */

public class AppAdapter extends BaseAdapter {
    private MainActivity activity;
    Context context;
    private int currentItem = -1;
    public ArrayList<mBookInfo> dataList= new ArrayList<>();
    private LayoutInflater v ;
    private ListView mListView;
    private View view;
    public AppAdapter(MainActivity activity, Context context, ArrayList<mBookInfo> inputDataList)
    {
        this.activity = new WeakReference<MainActivity>(activity).get();
        this.context=context;
        this.v = LayoutInflater.from(context);
        dataList.clear();

        for(int i=0;i<inputDataList.size();i++)
        {
            dataList.add(inputDataList.get(i));
        }
        inputDataList.clear();
        inputDataList = null;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final mBookInfo appUnit=dataList.get(position);
        Holder holder = null;
        view = convertView;
        if(view==null){
            holder = new Holder();
            view = v.inflate(R.layout.book_item, null);
            holder.book_name = (TextView) view.findViewById(R.id.book_name);
            holder.book_authr = (TextView) view.findViewById(R.id.book_auther);
            holder.book_number = (TextView) view.findViewById(R.id.book_number);
            holder.book_size = (TextView) view.findViewById(R.id.book_size);
            holder.book_typename = (TextView) view.findViewById(R.id.book_typename);
            holder.book_logo = (ImageView) view.findViewById(R.id.ivBook);
            view.setTag(holder);
        }else{
            holder = (Holder)  view.getTag();
        }
        PicassoUtlis.LoadImage(appUnit.getLogo(),holder.book_logo);
        //holder.textView.setText(appUnit.getDrawable());
        holder.book_name.setText(appUnit.getBook_name());
        holder.book_authr.setText("作者："+appUnit.getBook_author());
        holder.book_number.setText("章节："+appUnit.getBook_number());
        holder.book_size.setText("大小："+appUnit.getBook_size());
        holder.book_typename.setText("类型："+appUnit.getBook_ytpename());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BookDetailActivity.class);
                mBookInfo appUnit1 = dataList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", appUnit1);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        return  view;
    }


    public static class Holder{
        public TextView book_name; //名称
        public TextView book_authr; //作者
        public TextView book_size; //大小
        public TextView book_number; //章数
        public TextView book_typename;//分类
        public ImageView book_logo;
    }

}
