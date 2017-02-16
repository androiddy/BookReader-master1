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

import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.ViewUtils.CardViews;
import com.appbook.book.ViewUtils.MarqueeTextView;
import com.appbook.book.entity.HotBookInfo;
import com.appbook.book.entity.TypeAllInfo;
import com.appbook.book.fragment.main.MainActivity;
import com.appbook.book.utils.InputTools;
import com.cwvs.microlife.searchview.SearchActivity;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhangzhongping on 16/11/4.
 */

public class HotSearchAppAdapter extends BaseAdapter {
    private int color = 0;
    private SearchActivity activity;
    Context context;
    private int currentItem = -1;
    public ArrayList dataList= new ArrayList<>();
    private LayoutInflater v ;
    private ListView mListView;
    private View view;
    private  int rem;
    public HotSearchAppAdapter(SearchActivity searchActivity, Context context, ArrayList inputDataList)
    {
        this.activity = new WeakReference<SearchActivity>(searchActivity).get();
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
        rem = new Random().nextInt(6)+1;
        InitColor(rem);
        final HotBookInfo appUnit=(HotBookInfo)dataList.get(position);
        view = convertView;
        Holder holder = null;
        if(view == null){
            holder = new Holder();
            view = v.inflate(R.layout.hot_book_item, null);
            holder.name = (MarqueeTextView) view.findViewById(R.id.textView8);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.name.setText(appUnit.getHot_name());
        holder.name.setTextColor(color);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.etSearchContent.setText(appUnit.getHot_name());
                activity.etSearchContent.setSelection(appUnit.getHot_name().length());
                activity.loadingDialog.show();
                if(InputTools.KeyBoard(activity.etSearchContent)){
                    InputTools.HideKeyboard(activity.etSearchContent);
                }
                activity.swipeRefreshLayout.setMoreData(true);
                SendRequest.Unmber = 0;
                SendRequest.SendFindByBook(activity, activity, activity.etSearchContent.getText().toString(), System.currentTimeMillis() - MyRunApplication.getTTME(), false,appUnit.getHot_id()+"");
            }
        });
        return  view;
    }
    public static class Holder{
        public MarqueeTextView name; //名称


    }
    /* @ColorInt public static final int BLACK       = 0xFF000000;
   @ColorInt public static final int DKGRAY      = 0xFF444444;
   @ColorInt public static final int GRAY        = 0xFF888888;
   @ColorInt public static final int LTGRAY      = 0xFFCCCCCC;
   @ColorInt public static final int WHITE       = 0xFFFFFFFF;
   @ColorInt public static final int RED         = 0xFFFF0000;
   @ColorInt public static final int GREEN       = 0xFF00FF00;
   @ColorInt public static final int BLUE        = 0xFF0000FF;
   @ColorInt public static final int YELLOW      = 0xFFFFFF00;
   @ColorInt public static final int CYAN        = 0xFF00FFFF;
   @ColorInt public static final int MAGENTA     = 0xFFFF00FF;*/
    public void InitColor(int id){
        switch (id){
            case 1:
                color = 0xFF000000;//黑色
                break;
            case 2:
                color = 0xFF888888;
                break;
            case 3:
                color = 0xFFCCCCCC;
                break;
            case 4:
                color = 0xFFFF0000;
                break;
            case 5:
                color = 0xFF0000FF;
                break;
            case 6:
                color = 0xFF00FFFF;
                break;
            case 7:
                color = 0xFFFF00FF;
                break;

        }
    }
}
