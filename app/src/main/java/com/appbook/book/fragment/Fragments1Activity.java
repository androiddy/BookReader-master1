package com.appbook.book.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.appbook.book.CallBackImpl.RecommendedNetworkCallBackImpl;
import com.appbook.book.R;

/**
 * Created by zhangzhongping on 16/10/28.
 */

public class Fragments1Activity extends Fragment {
    public GridView gridView;
    public com.appbook.book.HttpUtils.HttpUtils httpUtils;
    public RecommendedNetworkCallBackImpl typeNetworkCallBack;
    private static Fragments1Activity fragments1Activity;

    public static Fragments1Activity newInstance(String info) {
        Bundle args = new Bundle();
        fragments1Activity = new Fragments1Activity();
        args.putString("info", info);
        fragments1Activity.setArguments(args);
        return fragments1Activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details1, null);
        gridView = (GridView) view.findViewById(R.id.GridView);
        typeNetworkCallBack = new RecommendedNetworkCallBackImpl(this);
        String url  = getArguments().getString("info");
        httpUtils = new com.appbook.book.HttpUtils.HttpUtils(url,getContext(),typeNetworkCallBack,null);
        httpUtils.NetworkRequest(null,false,0);
        return view;
    }
}
