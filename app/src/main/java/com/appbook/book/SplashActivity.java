package com.appbook.book;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;


@SuppressLint("SdCardPath")
public class SplashActivity extends Activity  {
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadMainUI();
        }
    };
	/**
	 * 加载主界面
	 */
	private void loadMainUI() {
		Intent intent = new Intent(this, TotalMainActivity.class);
		startActivity(intent);
        System.gc();
		finish();
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_mainnewrun);
        TextView textView = (TextView) findViewById(R.id.textView13);
        ImageView imageView5 = (ImageView) findViewById(R.id.imageView5);
        textView.setText(getString(R.string.app_name));
        imageView5.setBackgroundResource(R.mipmap.icon);
        Random random = new Random();
        int pick = random.nextInt(5);
        ImageView imageView = (ImageView) findViewById(R.id.imageView13);
        switch (pick){
            case 0:
                imageView.setBackgroundResource(R.drawable.upgrade_0);
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.upgrade_1);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.upgrade_2);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.upgrade_3);
                break;
            default:
                imageView.setBackgroundResource(R.drawable.upgrade_3);
                break;
        }
        handler.postDelayed(runnable,3000);
	}

	//防止用户返回键退出APP
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
