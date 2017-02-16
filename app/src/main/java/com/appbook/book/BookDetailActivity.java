package com.appbook.book;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.appbook.book.CallBack.DowBookCallBack;
import com.appbook.book.CallBack.NetworkCallBack;
import com.appbook.book.CallBackImpl.DowBookCallBackImpl;
import com.appbook.book.HttpUtils.HttpUtils;
import com.appbook.book.Thread.DowThread;
import com.appbook.book.entity.mBookInfo;
import com.appbook.book.fragment.FragmentActivity;
import com.appbook.book.fragment.Fragments1Activity;
import com.appbook.book.fragment.UserFragmen;
import com.appbook.book.widget.PicassoUtlis;
import com.justwayward.reader.bean.Recommend;
import com.justwayward.reader.utils.FileUtils;
import com.loopj.android.http.RequestParams;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhongping on 16/10/28.
 */

public class BookDetailActivity extends com.appbook.book.Activitys.ClassMainActivity {
    private  RequestParams params;
    private ViewPager mViewPager;
    private mBookInfo mBook;
    private String URL = "/Book/Book/RandomByid.do";
    private ProgressDialog progressDialog = null;
    private DowThread dowThread;
    private UserFragmen userFragmen = null;
    private DowBookCallBack dowBookCallBack;
    private Recommend.RecommendBooks books = null;
    private FloatingActionButton fab;
    private  Drawable drawable = null;
    private HttpUtils httpUtils;
    private String URLs = "/Book/Book/AddByIdDowCount.do";
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                long index = (long) msg.obj;
                setProgres(index);
            }else{
                Top();
            }
        }
    };
    public void Top(){
        fab.setImageDrawable(drawable);
        params = new RequestParams();
        params.put("id", mBook.getBook_id());
        httpUtils = new com.appbook.book.HttpUtils.HttpUtils(MyRunApplication.getURL(URLs),getApplicationContext(),new TopNet(),null);
        httpUtils.NetworkRequest(params,false,0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mBook = (mBookInfo) getIntent().getSerializableExtra("book");
        userFragmen = UserFragmen.newInstance(mBook.getBook_id());
        toolbar.setTitle(mBook.getBook_name());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView book_name = (TextView) findViewById(R.id.book_name);
        TextView book_authr = (TextView) findViewById(R.id.book_auther);
        TextView book_number = (TextView) findViewById(R.id.book_number);
        TextView book_size = (TextView) findViewById(R.id.book_size);
        TextView book_typename = (TextView) findViewById(R.id.book_typename);
        book_name.setText(mBook.getBook_name());
        book_authr.setText("作者："+mBook.getBook_author());
        book_number.setText("章节："+mBook.getBook_number());
        book_size.setText("大小："+mBook.getBook_size());
        book_typename.setText("类型："+mBook.getBook_ytpename());
        ImageView ivImage = (ImageView) findViewById(R.id.ivBook);
        PicassoUtlis.LoadImage(mBook.getLogo(),ivImage);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("内容简介"));
        tabLayout.addTab(tabLayout.newTab().setText("精品推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("小说评论"));
        tabLayout.setupWithViewPager(mViewPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawable = this.getResources().getDrawable(R.mipmap.open);
        if(InitByte()){
            fab.setImageDrawable(drawable);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(InitByte()){
                    if(books==null){
                        books = new Recommend.RecommendBooks();
                        books._id = mBook.getBook_name()+"-dy-"+ com.appbook.book.utils.HttpUtils.getMD5((mBook.getBook_id()+"").getBytes());
                        books.path = FileUtils.getChapterPath(mBook.getBook_name()+mBook.getBook_id(),1);
                        books.title = mBook.getBook_name();
                        books.isFromSD = true;
                        books.lastChapter = FileUtils.formatFileSizeToString(new File(FileUtils.getChapterPath(mBook.getBook_name()+mBook.getBook_id(),1)).length());
                    }
                    try{
                        ReadActivity.startActivity(BookDetailActivity.this, books, books.isFromSD);
                    }catch (Exception e){

                    }
                }else{
                    OpenShowDow(100);
                    dowBookCallBack = new DowBookCallBackImpl(BookDetailActivity.this);
                    String url = mBook.getBook_dowurl();
                    try {
                        String[] urls = url.split("/");
                        String urlpa = new String(java.net.URLEncoder.encode(urls[urls.length-1],"utf-8").getBytes());
                        urls[0] = url.replace(urls[urls.length-1],urlpa);
                        url = urls[0];
                        dowThread = new DowThread(mBook.getBook_id(),BookDetailActivity.this,dowBookCallBack,url,mBook.getBook_name(),mBook.getBook_byte());
                        dowThread.start();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void OpenShowDow(int sta){
        try{
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("下载中");
            progressDialog.setMax(sta);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    progressDialog = null;
                }
            });
            progressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public boolean InitByte(){
        File file = new File(FileUtils.getChapterPath(mBook.getBook_name()+"-dy-"+ com.appbook.book.utils.HttpUtils.getMD5((mBook.getBook_id()+"").getBytes()), 1));
        if(file.exists()){
            return true;
        }
        return false;
    }
    public void CloseShowDow(){
        if(progressDialog!=null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    public void setProgres(long i){
        if(progressDialog!=null){
            progressDialog.setProgress((int) i);
        }
    }
    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FragmentActivity.newInstance(mBook.getBook_introduction()), "内容简介");
        adapter.addFragment(Fragments1Activity.newInstance(MyRunApplication.getURL(URL)), "精品推荐");
        adapter.addFragment(userFragmen, "小说评论");
        mViewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        com.appbook.book.utils.HttpUtils.Cancelrequest(this);
        super.onDestroy();
    }

    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
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
