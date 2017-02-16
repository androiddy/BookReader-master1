package com.appbook.book.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.appbook.book.R;


/**
 * Created by zhangzhongping on 16/10/28.
 */

public class FragmentActivity extends Fragment {

    public static FragmentActivity newInstance(String info) {
        Bundle args = new Bundle();
        FragmentActivity fragmentActivity = new FragmentActivity();
        args.putString("info", info);
        fragmentActivity.setArguments(args);
        return fragmentActivity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        tvInfo.setText(getArguments().getString("info"));
        return view;
    }
}
