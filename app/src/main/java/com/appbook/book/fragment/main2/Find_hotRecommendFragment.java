package com.appbook.book.fragment.main2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.TotalMainActivity;
import com.appbook.book.ViewUtils.RefreshLayout;
import com.appbook.book.entity.MessageInfoIndex;
import com.appbook.book.entity.NnumberAll;
import com.appbook.book.utils.DateFormatUtils;
import com.appbook.book.widget.PicassoUtlis;
import com.appbook.book.widget.VRefresh;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import java.util.ArrayList;


/**
 * Created by zhangzhongping on 16/11/27.
 */

public class Find_hotRecommendFragment extends Fragment implements NetworkCallBack {
    private View view;
    public VRefresh swipeRefreshLayout;
    public RunRankAdapter runRankAdapter;
    public boolean isPrepared = true;
    public RequestParams requestParams;
    public  ListView recyclerView;
    public HttpUtils httpUtils;
    public NnumberAll nnumberAll;
    private String SendMesAllId = "/Book/message/findAllMesTypeId.do";
    private RelativeLayout refreshLayout1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // detach/attach can lead to view recreate frequently\
        if(view == null){
            view=inflater.inflate(R.layout.activity_mainmes, null);
            lazyLoad();
        }
        return view;
    }
    public void RunMainX5Web(int position){
        //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
        Intent intent = new Intent(getActivity(), MessageIndexctivity.class);
        //用Bundle携带数据
        Bundle bundle = new Bundle();
        MessageInfoIndex m = ((MessageInfoIndex) runRankAdapter.getItem(position));
        //传递name参数为tinyphp
        bundle.putSerializable("mesinfo", m );
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /**
     * 显示删除本地缓存对话框
     *
     * @param
     */
    private void showDeleteCacheDialog(final int position) {
        final boolean selected[] = {true};
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示").setMessage("当前非Wifi状态，是否继续浏览！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RunMainX5Web(position);
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }
    protected void lazyLoad() {
        if(isPrepared){
            recyclerView = (ListView) view.findViewById(R.id.recyclerView);
            swipeRefreshLayout = (VRefresh) view.findViewById(R.id.srlayout);
            swipeRefreshLayout.setView(getContext(),recyclerView);
            refreshLayout1 = (RelativeLayout) view.findViewById(R.id.refreshLayout1);
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                    android.R.color.holo_orange_light, android.R.color.holo_green_light);
            // 设置下拉刷新监听器
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            nnumberAll.setCurrentPage(0);
                            requestParams = new RequestParams();
                            requestParams.put("id",Integer.parseInt(getArguments().getString("infourl")));
                            requestParams.put("mesid",nnumberAll.getCurrentPage());
                            httpUtils = new HttpUtils(MyRunApplication.getURL(SendMesAllId),Find_hotRecommendFragment.this.getContext(),Find_hotRecommendFragment.this, nnumberAll);
                            httpUtils.MesAllNetworkRequest(requestParams,false);
                            swipeRefreshLayout.setMoreData(true);
                        }
                    }, 0);
                }
            });
            swipeRefreshLayout.setOnLoadListener(new VRefresh.OnLoadListener() {
                @Override
                public void onLoadMore() {
                    requestParams = new RequestParams();
                    requestParams.put("id",Integer.parseInt(getArguments().getString("infourl")));
                    requestParams.put("mesid",nnumberAll.getCurrentPage());
                    httpUtils = new HttpUtils(MyRunApplication.getURL(SendMesAllId),Find_hotRecommendFragment.this.getContext(),Find_hotRecommendFragment.this, nnumberAll);
                    httpUtils.MesAllNetworkRequest(requestParams,true);
                }

            });
            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (com.appbook.book.utils.HttpUtils.isNetworkAvailable(getActivity())) {
                        if (com.appbook.book.utils.HttpUtils.isWifi(getActivity().getApplicationContext())) {
                            RunMainX5Web(position);
                        } else {
                            showDeleteCacheDialog(position);
                        }
                    } else {
                        Toast.makeText(getContext(), "当前没有网络连接!", 0).show();
                    }
                }
            });
            nnumberAll = new NnumberAll();
            nnumberAll.setTIME_END(0);
            nnumberAll.setNUMBER(0);
            nnumberAll.setCurrentPage(0);
            requestParams = new RequestParams();
            requestParams.put("id",Integer.parseInt(getArguments().getString("infourl")));
            requestParams.put("mesid",nnumberAll.getCurrentPage());
            httpUtils = new HttpUtils(MyRunApplication.getURL(SendMesAllId),this.getContext(),this, nnumberAll);
            httpUtils.MesAllNetworkRequest(requestParams,false);
            isPrepared = false;
        }
    }

    @Override
    public void onSuccess(ArrayList arrayList, boolean str, boolean cache) {

        if(refreshLayout1.isShown()){
            refreshLayout1.setVisibility(View.GONE);
        }
        swipeRefreshLayout.setRefreshing(false);
        try{
            swipeRefreshLayout.setLoading(false);
        }catch (Exception e){

        }
        if (arrayList == null) {
            swipeRefreshLayout.setMoreData(false);
            return;
        }
        if(runRankAdapter!=null){
            if(str){
                if(arrayList.size()>0){
                    if(arrayList.size()<10){
                        swipeRefreshLayout.setMoreData(false);
                    }
                    swipeRefreshLayout.setLoading(false);
                    runRankAdapter.ls.addAll(arrayList);
                }else{
                    swipeRefreshLayout.setLoading(false);
                    swipeRefreshLayout.setMoreData(false);
                }
                runRankAdapter.notifyDataSetChanged();
                return;
            }
            runRankAdapter.ls = arrayList;
            runRankAdapter.notifyDataSetChanged();
            return;
        }
        runRankAdapter = new RunRankAdapter(getContext(), arrayList);
        recyclerView.setAdapter(runRankAdapter);
    }

    @Override
    public void onFiled(Throwable throwable, boolean str) {
        try{
            if(refreshLayout1.isShown()){
                refreshLayout1.setVisibility(View.GONE);
            }
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setLoading(false);
        }catch (Exception e){

        }
    }

    @Override
    public void onError() {

    }

    class RunRankAdapter extends BaseAdapter {
        ArrayList<MessageInfoIndex> ls;
        Context mContext;
        LayoutInflater inflater;
        final int TYPE_1 = 0;
        final int TYPE_2 = 1;
        final int TYPE_3 = 2;

        public RunRankAdapter(Context context,
                              ArrayList<MessageInfoIndex> list) {
            ls = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return ls.size();
        }

        @Override
        public Object getItem(int position) {
            return ls.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // 每个convert view都会调用此方法，获得当前所需要的view样式
        @Override
        public int getItemViewType(int position) {
            MessageInfoIndex messageInfoIndex = ls.get(position);
            return messageInfoIndex.getMessage_view();
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder1 holder1 = null;
            ViewHolder2 holder2 = null;
            ViewHolder3 holder3 = null;
            MessageInfoIndex messageInfoIndex = (MessageInfoIndex) getItem(position);
            int type = getItemViewType(position);
            if (convertView == null) {
                inflater = LayoutInflater.from(mContext);
                switch (type) {
                    case TYPE_1:
                        convertView = inflater.inflate(R.layout.item_runrank1,
                                parent, false);
                        holder1 = new ViewHolder1();
                        holder1.rank1 = (TextView) convertView
                                .findViewById(R.id.item_nfplvadapter_duotu_title);
                        holder1.rank2 = (TextView) convertView
                                .findViewById(R.id.item_nfplvadapter_duotu_source);
                        holder1.time1 = (ImageView) convertView
                                .findViewById(R.id.item_nfplvadapter_duotu_img1);
                        holder1.time2 = (ImageView) convertView
                                .findViewById(R.id.item_nfplvadapter_duotu_img2);
                        holder1.time3 = (ImageView) convertView
                                .findViewById(R.id.item_nfplvadapter_duotu_img3);
                        convertView.setTag(holder1);
                        break;
                    case TYPE_2:
                        convertView = inflater.inflate(R.layout.item_runrank2,
                                parent, false);
                        holder2 = new ViewHolder2();
                        holder2.rank1 = (TextView) convertView
                                .findViewById(R.id.textView12);
                        holder2.rank2 = (TextView) convertView
                                .findViewById(R.id.textView14);
                        holder2.time1 = (ImageView) convertView
                                .findViewById(R.id.imageView4);
                        convertView.setTag(holder2);
                        break;
                    case TYPE_3:
                        convertView = inflater.inflate(R.layout.item_runrank3,
                                parent, false);
                        holder3 = new ViewHolder3();
                        holder3.rank1 = (TextView) convertView
                                .findViewById(R.id.item_nfplvadapter_duotu_title);
                        holder3.rank2 = (TextView) convertView
                                .findViewById(R.id.item_nfplvadapter_duotu_source);
                        holder3.time1 = (ImageView) convertView
                                .findViewById(R.id.item_nfplvadapter_duotu_img1);
                        convertView.setTag(holder3);
                        break;
                    default:
                        break;
                }

            } else {
                switch (type) {
                    case TYPE_1:
                        holder1 = (ViewHolder1) convertView.getTag();
                        break;
                    case TYPE_2:
                        holder2 = (ViewHolder2) convertView.getTag();
                        break;
                    case TYPE_3:
                        holder3 = (ViewHolder3) convertView.getTag();
                        break;
                }
            }
            // 设置资源
            switch (type) {
                case TYPE_1:
                    String image = messageInfoIndex.getImage();
                    String[] strings = image.split("--dy--");
                    holder1.rank1.setText(messageInfoIndex.getMessage_name());
                    holder1.rank2.setText(DateFormatUtils.getTimesToNow(messageInfoIndex.getMessage_time()));
                    PicassoUtlis.LoadImage(strings, holder1.time1,holder1.time2,holder1.time3);
                    break;
                case TYPE_2:
                    holder2.rank1.setText(messageInfoIndex.getMessage_name());
                    holder2.rank2.setText(DateFormatUtils.getTimesToNow(messageInfoIndex.getMessage_time()));
                    PicassoUtlis.LoadImage(messageInfoIndex.getImage().replace("--dy--", ""),holder2.time1);
                    break;
                case TYPE_3:
                    String images = messageInfoIndex.getImage();
                    holder3.rank1.setText(messageInfoIndex.getMessage_name());
                    holder3.rank2.setText(DateFormatUtils.getTimesToNow(messageInfoIndex.getMessage_time()));
                    PicassoUtlis.LoadImage(images.replace("--dy--", ""),holder3.time1);
                    break;
            }

            return convertView;
        }

        public class ViewHolder1 {
            TextView rank1;
            TextView rank2;
            ImageView time1;
            ImageView time2;
            ImageView time3;
        }

        public class ViewHolder2 {
            TextView rank1;
            TextView rank2;
            ImageView time1;
        }
        public class ViewHolder3 {
            TextView rank1;
            TextView rank2;
            ImageView time1;

        }

    }
}
