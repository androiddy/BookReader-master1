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
import com.appbook.book.R;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.widget.PicassoUtlis;
import com.cwvs.microlife.searchview.SearchActivity;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/11/4.
 */

public class FindBookAppAdapter extends BaseAdapter {
    private SearchActivity activity;
    Context context;
    private int currentItem = -1;
    public ArrayList dataList= new ArrayList<>();
    private LayoutInflater v ;
    private ListView mListView;
    private View view;
    public FindBookAppAdapter(SearchActivity activity, Context context, ArrayList inputDataList)
    {
        this.activity = new WeakReference<SearchActivity>(activity).get();
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
        final mBookInfo appUnit=(mBookInfo)dataList.get(position);
        Holder holder = null;
        view = convertView;
        if(view==null){
            holder = new Holder();
            view = v.inflate(R.layout.item_findbook, null);
            holder.name = (TextView) view.findViewById(R.id.tvLatelyUpdate);
            holder.names = (TextView) view.findViewById(R.id.tvRecommendTitle);
            holder.number = (TextView) view.findViewById(R.id.tvRecommendShort);
            holder.book_logo2 = (ImageView) view.findViewById(R.id.ivRecommendCover);
            view.setTag(holder);
        }else{
            holder = (Holder)  view.getTag();
        }
        PicassoUtlis.LoadImage(appUnit.getLogo(),holder.book_logo2);
        holder.names.setText(appUnit.getBook_name());
        holder.name .setText(appUnit.getBook_author()+" | "+appUnit.getBook_ytpename()+" | "+appUnit.getBook_number());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book",appUnit);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        return  view;
    }
    public static class Holder{
        public TextView name; //名称
        public TextView names; //名称
        public TextView number;
        public ImageView book_logo2;

    }
}
