package com.appbook.book.fragment.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appbook.book.BookDetailActivity;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.CallBackImpl.RecommendedNetworkCallBackImpl;
import com.appbook.book.HttpUtils.SendRequest;
import com.appbook.book.MyRunApplication;
import com.appbook.book.R;
import com.appbook.book.ReadActivity;
import com.appbook.book.ViewUtils.GlideImageLoader;
import com.appbook.book.ViewUtils.RefreshLayout;
import com.appbook.book.entity.MessageInfoIndex;
import com.appbook.book.entity.mBookInfo;
import com.justwayward.reader.base.Constant;
import com.justwayward.reader.bean.Recommend;
import com.justwayward.reader.manager.CollectionsManager;
import com.justwayward.reader.ui.easyadapter.RecommendAdapter;
import com.justwayward.reader.ui.easyadapter.RecommendAdapters;
import com.justwayward.reader.utils.FileUtils;
import com.justwayward.reader.utils.ToastUtils;
import com.justwayward.reader.view.recyclerview.EasyRecyclerView;
import com.justwayward.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

import static com.justwayward.reader.view.recyclerview.adapter.RecyclerArrayAdapter.*;


/**
 * Created by zhangzhongping on 16/10/28.
 */

public class Mains1Activity extends Fragment implements  OnItemClickListener,RecyclerArrayAdapter.OnItemLongClickListener {
    EasyRecyclerView mRecyclerView;

    public RecommendAdapters mAdapter;
    private static Mains1Activity activity;
    private com.appbook.book.HttpUtils.HttpUtils  httpUtils;
    public RefreshLayout swipeRefreshLayout;
    public RelativeLayout refreshLayout1;
    private  View view;
    public static Mains1Activity newInstance() {
        activity = new Mains1Activity();
        return activity;
    }
    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.activity_book_jia, null);
            mRecyclerView = (EasyRecyclerView) view.findViewById(R.id.recyclerview);
            refreshLayout1 = (RelativeLayout) view.findViewById(R.id.refreshLayout1);
            swipeRefreshLayout = (RefreshLayout) view.findViewById(R.id.srlayout);

            configViews();
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                    android.R.color.holo_orange_light, android.R.color.holo_green_light);
            // 设置下拉刷新监听器
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if(swipeRefreshLayout.isShown()){
                                swipeRefreshLayout.setVisibility(View.GONE);
                            }
                            refreshLayout1.setVisibility(View.VISIBLE);
                            queryFiles();

                        }
                    }, 500);
                }
            });
            swipeRefreshLayout.setOnLoadListener(null);
        }
        return view;
    }
    public void configViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setItemDecoration(ContextCompat.getColor(this.getContext(), R.color.common_divider_narrow), 1, 0, 0);
        mAdapter = new RecommendAdapters(getContext());
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapterWithProgress(mAdapter);
        queryFiles();
    }
    private void queryFiles() {
        List<Recommend.RecommendBooks> cursor = new ArrayList<>();
        try {
            FileUtils.Init();
            cursor = FileUtils.OpenMain(Constant.BASE_PATH );
        } catch (Exception e) {

        }finally {
            if (cursor != null) {
                mAdapter.clear();
                if(cursor.size()>0){
                    mAdapter.addAll(cursor);
                }
                SendRequest.SendRandomBook(getContext(),this,1);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        final Recommend.RecommendBooks books = mAdapter.getItem(position);
        if(books.isNetwore){
            Intent intent = new Intent(this.getActivity(), BookDetailActivity.class);
            mBookInfo appUnit1 = books.bookInfo;
            Bundle bundle = new Bundle();
            bundle.putSerializable("book", appUnit1);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            ReadActivity.startActivity(getContext(), books, books.isFromSD);
        }

    }

    /**
     * 显示删除本地缓存对话框
     *
     * @param removeList
     */
    private void showDeleteCacheDialog(final List<Recommend.RecommendBooks> removeList) {
        final boolean selected[] = {true};
        new AlertDialog.Builder(this.getActivity())
                .setTitle(getString(R.string.remove_selected_book))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new AsyncTask<String, String, String>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();

                            }

                            @Override
                            protected String doInBackground(String... params) {
                                CollectionsManager.getInstance().removeSome(removeList);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                ToastUtils.showSingleToast("成功移除书籍");
                                for (Recommend.RecommendBooks bean : removeList) {
                                    mAdapter.remove(bean);
                                }

                            }
                        }.execute();

                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), null)
                .create().show();
    }

    @Override
    public boolean onItemLongClick(int position) {
        if(mAdapter.getItem(position).isNetwore){
            return false;
        }
        List<Recommend.RecommendBooks> removeList = new ArrayList<>();
        removeList.add(mAdapter.getItem(position));
        showDeleteCacheDialog(removeList);
        return false;
    }



}
