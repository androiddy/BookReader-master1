package com.appbook.book.fragment.main2;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appbook.book.AppAdapter.Find_tab_Adapter;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.R;
import com.appbook.book.entity.MessageListInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * 发现页面
 */
public class MessageListctivity extends Fragment implements NetworkCallBack,View.OnClickListener {

    private TabLayout tab_FindFragment_title;
    private ViewPager vp_FindFragment_pager;
    private FragmentPagerAdapter fAdapter;
    private List<Fragment> list_fragment;
    private ArrayList list_title;
    private View view ;
    private RelativeLayout refreshLayout1;
    private RelativeLayout refreshLayout;
    private View toolbar_shadow;
    public static MessageListctivity newInstance() {
        MessageListctivity fragment = new MessageListctivity();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.activity_message_list, container, false);
            initControls(view);
        }
        return view;
    }

    /**
     * 初始化各控件
     * @param view
     */
    private void initControls(View view) {
        toolbar_shadow = view.findViewById(R.id.toolbar_shadow);
        refreshLayout = (RelativeLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout1 = (RelativeLayout) view.findViewById(R.id.refreshLayout1);
        tab_FindFragment_title = (TabLayout)view.findViewById(R.id.tab_FindFragment_title);
        vp_FindFragment_pager = (ViewPager)view.findViewById(R.id.vp_FindFragment_pager);
        tab_FindFragment_title.setTabMode(TabLayout.MODE_FIXED);
        vp_FindFragment_pager.setOffscreenPageLimit(3);
        refreshLayout.setOnClickListener(this);
        SendRequest.SendMesList(this);
        //tab_FindFragment_title.set
    }
    @Override
    public void onClick(View v) {
            if(v.getId()==R.id.refreshLayout){
                refreshLayout1.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
                SendRequest.SendMesList(this);
            }
    }
    static class MyPagerAdapter extends FragmentPagerAdapter {
        private  List<Fragment> mFragments = new ArrayList<>();
        private  List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm,List<Fragment> mFragments,List<String> mFragmentTitles) {
            super(fm);
            this.mFragments = mFragments;
            this.mFragmentTitles = mFragmentTitles;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void onSuccess(ArrayList arrayList, boolean str, boolean cache) {
        if(arrayList==null||arrayList.size()<=0){
            return;
        }
        refreshLayout1.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
        tab_FindFragment_title.setVisibility(View.VISIBLE);
        vp_FindFragment_pager.setVisibility(View.VISIBLE);
        toolbar_shadow.setVisibility(View.VISIBLE);
        ArrayList<MessageListInfo> ac = arrayList;
        list_fragment = new ArrayList<Fragment>();
        list_title = new ArrayList();
        for(int i = 0;i<ac.size();i++){
            Bundle args = new Bundle();
            Find_hotRecommendFragment fragment = new Find_hotRecommendFragment();
            args.putString("infourl", ac.get(i).getMes_id()+"");
            fragment.setArguments(args);
            list_fragment.add(fragment);
            //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
            list_title.add(ac.get(i).getMes_name());
            tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(ac.get(i).getMes_name()));
        }
        fAdapter = new MyPagerAdapter(getFragmentManager(),list_fragment,list_title);
        //viewpager加载adapter
        vp_FindFragment_pager.setAdapter(fAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);
    }
    @Override
    public void onFiled(Throwable throwable, boolean str) {
        if(!str){
            tab_FindFragment_title.setVisibility(View.GONE);
            toolbar_shadow.setVisibility(View.GONE);
            vp_FindFragment_pager.setVisibility(View.GONE);
            refreshLayout1.setVisibility(View.GONE);
            if(vp_FindFragment_pager.getChildCount()<5){
                refreshLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onError() {

    }
}
