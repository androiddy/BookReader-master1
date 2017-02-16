package com.appbook.book.fragment.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.CallBackImpl.TypeNetworkCallBackImpl;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.ViewUtils.GlideImageLoader;
import com.appbook.book.ViewUtils.RefreshLayout;
import com.appbook.book.entity.BannerInfo;
import com.appbook.book.entity.MessageInfoIndex;
import com.appbook.book.entity.ProjectInfo;
import com.appbook.book.fragment.main2.MessageIndexctivity;
import com.loopj.android.http.RequestParams;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by zhangzhongping on 16/10/28.
 */

public class MainActivity extends BaseFragment implements NetworkCallBack,View.OnClickListener {
    private static MainActivity activity;
    private HttpUtils httpUtils;
    private TypeNetworkCallBackImpl typeNetworkCallBack;
    public ListView listView;
    private  View view = null;
    public RefreshLayout swipeRefreshLayout;
    public RelativeLayout refreshLayout1;
    private String URL = "/Book/Book/findAllByType.do";
    RequestParams  params;
    /** 标志位，标志已经初始化完成 */
    private String[] image;
    private String[] name;
    private boolean isPrepared  = true;
    private Banner banner;
    public RelativeLayout relativeLayout;
    private MessageInfoIndex messageInfoIndex;
    private ProjectInfo projectInfo;
    public CardView global_search_action_bar_rl;
    public static MainActivity newInstance() {
        activity = new MainActivity();
        return activity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.activity_mainbook, null);
            lazyLoad();
        }
        return view;
    }
    public void InitView(View view){
        banner = (Banner) view.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        global_search_action_bar_rl = (CardView) view.findViewById(R.id.global_search_action_bar_rl);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout1 = (RelativeLayout) view.findViewById(R.id.refreshLayout1);
        relativeLayout.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.recyclerView);
        listView.setDividerHeight(1);
        swipeRefreshLayout = (RefreshLayout) view.findViewById(R.id.srlayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        // 设置下拉刷新监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        SendRequest.SendMesBanner(MainActivity.this);
                        httpUtils.TypeNetworkRequest(false,1);
                    }
                }, 0);
            }
        });
        swipeRefreshLayout.setOnLoadListener(null);
    }
    @Override
    protected void lazyLoad() {
        if (!isVisible||!isPrepared) {
            return;
        }
        typeNetworkCallBack = new TypeNetworkCallBackImpl(this);
        httpUtils = new HttpUtils(MyRunApplication.getURL(URL),getContext(),typeNetworkCallBack,null);
        InitView(view);
        if(isPrepared){
            SendPost();
            isPrepared = false;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refreshLayout:
                relativeLayout.setVisibility(View.GONE);
                refreshLayout1.setVisibility(View.VISIBLE);
                SendPost();
                break;
        }
    }
    public void SendPost(){
        try {
            swipeRefreshLayout.autoRefresh();
        } catch (Exception e) {
            httpUtils.TypeNetworkRequest(false,1);
            e.printStackTrace();
        }
    }
    @Override
    public void onSuccess(final ArrayList arrayList, boolean str, boolean cache) {
        if(arrayList==null||arrayList.size()<=0){
            return;
        }
        image = new String[arrayList.size()];
        name = new String[arrayList.size()];
        for (int i = 0;i<arrayList.size();i++){
            BannerInfo m = (BannerInfo) arrayList.get(i);
            image[i] = (m.getBanner_image().replace("--dy--",""));
            name[i] = (m.getBanner_name());
        }
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(Arrays.asList(name));
        //设置图片集合
        banner.setImages(Arrays.asList(image));
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                if (com.appbook.book.utils.HttpUtils.isNetworkAvailable(getActivity())) {
                    BannerInfo messageInfoIndexs = (BannerInfo) arrayList.get(position-1);
                    params = new RequestParams();
                    params.put("id", messageInfoIndexs.getId());
                    com.appbook.book.utils.HttpUtils.post(MainActivity.this.getContext(),MyRunApplication.getURL("/Book/Banner/AddBannerClick.do"),params);
                    if(messageInfoIndexs.getBanner_type()==0){
                        messageInfoIndex = new MessageInfoIndex();
                        messageInfoIndex.setId(messageInfoIndexs.getBanner_id());
                        messageInfoIndex.setMessage_name(messageInfoIndexs.getBanner_name());
                        messageInfoIndex.setMessage_typename(messageInfoIndexs.getBanner_typename());
                    }else{
                        projectInfo = new ProjectInfo();
                        projectInfo.setProject_id(messageInfoIndexs.getBanner_id());
                        projectInfo.setProject_name(messageInfoIndexs.getBanner_name());
                        projectInfo.setProject_index(messageInfoIndexs.getBanner_typename());
                        projectInfo.setProject_logo(messageInfoIndexs.getBanner_image());
                        Intent intent = new Intent(activity.getActivity(), CategoryListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("APP_OBTAIN", projectInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return;
                    }
                    if (com.appbook.book.utils.HttpUtils.isWifi(getActivity().getApplicationContext())) {
                        RunMainX5Web(messageInfoIndex);
                    } else {
                        showDeleteCacheDialog(messageInfoIndex);
                    }
                } else {
                    Toast.makeText(getContext(), "当前没有网络连接!", 0).show();
                }
            }
        });
        banner.start();
    }
    public void RunMainX5Web(MessageInfoIndex position){
        //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
        Intent intent = new Intent(getActivity(), MessageIndexctivity.class);
        //用Bundle携带数据
        Bundle bundle = new Bundle();
        //传递name参数为tinyphp
        bundle.putSerializable("mesinfo", position );
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        com.appbook.book.utils.HttpUtils.Cancelrequest(getContext());
        super.onDestroy();
    }

    /**
     * 显示删除本地缓存对话框
     *
     * @param
     */
    private void showDeleteCacheDialog(final MessageInfoIndex position) {
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
    @Override
    public void onFiled(Throwable throwable, boolean str) {

    }

    @Override
    public void onError() {

    }

    static class TopNet implements NetworkCallBack{

        @Override
        public void onSuccess(ArrayList arrayList, boolean str,boolean cache) {

        }

        @Override
        public void onFiled(Throwable throwable,boolean boo) {

        }

        @Override
        public void onError() {

        }
    }

}
