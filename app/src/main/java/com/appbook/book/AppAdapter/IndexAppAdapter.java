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
import com.appbook.book.BookDetailActivity;
import com.appbook.book.R;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.fragment.Fragments1Activity;
import com.appbook.book.widget.PicassoUtlis;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/10/18.
 */

public class IndexAppAdapter extends BaseAdapter {
    private Fragments1Activity activity;
    Context context;
   public ArrayList<mBookInfo> dataList= new ArrayList<>();
    private LayoutInflater v ;
    private ListView mListView;
    private View view;
    public IndexAppAdapter(Fragments1Activity activity, Context context, ArrayList<mBookInfo> inputDataList)
    {
        this.activity = new WeakReference<Fragments1Activity>(activity).get();
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
            view = v.inflate(R.layout.book_items1, null);
            holder.book_logo = (ImageView) view.findViewById(R.id.ivBook);
            view.setTag(holder);
        }else{
            holder = (Holder)  view.getTag();
        }
        PicassoUtlis.LoadImage(appUnit.getLogo(),holder.book_logo);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getContext(), BookDetailActivity.class);
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
        public ImageView book_logo;
    }
}
