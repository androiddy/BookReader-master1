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

import com.appbook.book.MainActivity;
import com.appbook.book.R;
import com.appbook.book.TotalMainActivity;
import com.appbook.book.entity.TypeAllInfo;
import com.appbook.book.widget.PicassoUtlis;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 16/10/29.
 */

public class TypeAppAdapter extends BaseAdapter {
    private com.appbook.book.fragment.main.MainActivity activity;
    Context context;
    private int currentItem = -1;
    public ArrayList<TypeAllInfo> dataList= new ArrayList<>();
    private LayoutInflater v ;
    private ListView mListView;
    private View view;
    public TypeAppAdapter(com.appbook.book.fragment.main.MainActivity activity, Context context, ArrayList<TypeAllInfo> inputDataList)
    {
        this.activity = new WeakReference<com.appbook.book.fragment.main.MainActivity>(activity).get();
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
        final TypeAllInfo appUnit=dataList.get(position);
        Holder holder = null;
        view = convertView;
        if(view==null){
            holder = new Holder();
            view = v.inflate(R.layout.book_type_mian, null);
            holder.name = (TextView) view.findViewById(R.id.textView);
            holder.number = (TextView) view.findViewById(R.id.textView2);
            holder.book_logo = (ImageView) view.findViewById(R.id.ivBook);
            holder.book_logo1 = (ImageView) view.findViewById(R.id.ivBook1);
            holder.book_logo2 = (ImageView) view.findViewById(R.id.ivBook2);
            view.setTag(holder);
        }else{
            holder = (Holder)  view.getTag();
        }
        String[] url = new String[3];
        url[0] = appUnit.getType_logo();
        url[1] = appUnit.getType_logo1();
        url[2] = appUnit.getType_logo2();
        PicassoUtlis.LoadImages(url, holder.book_logo,holder.book_logo1,holder.book_logo2);
        holder.name.setText(appUnit.getType_name());
        holder.number.setText("共"+appUnit.getType_number()+"册");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getActivity(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("booktype", dataList.get(position));
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        return  view;
    }
    public static class Holder{
        public TextView name; //名称
        public TextView number;
        public ImageView book_logo;
        public ImageView book_logo1;
        public ImageView book_logo2;

    }
}
