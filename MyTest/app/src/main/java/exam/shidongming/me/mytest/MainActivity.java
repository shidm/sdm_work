package exam.shidongming.me.mytest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;

    private TextView tv1,tv2,tv3;

    private View color;

    private Fragment fg1;
    private Fragment fg2;
    private Fragment fg3;
    private List fgList = new ArrayList();
    private Update mUpdate;

    private MyPagerAdapter myPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        it();
        init();
        Toast.makeText(this,"请稍候片刻，正在加载内容",Toast.LENGTH_SHORT).show();
    }

    private void it() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv1.setOnClickListener(new MyOnClickListener(0));
        tv2.setOnClickListener(new MyOnClickListener(1));
        tv3.setOnClickListener(new MyOnClickListener(2));
        color = findViewById(R.id.color);

        int width = getScreenWidth(this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width/3,4);
        color.setLayoutParams(params);
    }

    public static int getScreenWidth(Context ctx){
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.myViewPager);
        fg1 = new PagerFragment1();
        fg2 = new PagerFragment2();
        fg3 = new PagerFragment3();
        fgList.add(fg1);
        fgList.add(fg2);
        fgList.add(fg3);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),fgList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i2) {
            color.setX((i + v) * getScreenWidth(getApplicationContext()) / 3);
        }

        @Override
        public void onPageSelected(int i) {
            tv1.setTextColor(getResources().getColor(R.color.blue));
            tv2.setTextColor(getResources().getColor(R.color.blue));
            tv3.setTextColor(getResources().getColor(R.color.blue));
            switch (i) {
                case 0:
                    tv1.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 1:
                    tv2.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 2:
                    tv3.setTextColor(getResources().getColor(R.color.red));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_appRefresh:
                mUpdate = new Update(this);
                mUpdate.checkUpdateInfo();
                Toast.makeText(this,"ca",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode==KeyEvent.KEYCODE_BACK) && (event.getAction()==KeyEvent.ACTION_DOWN)) {
//            exit2();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void exit2() {
//        long time = System.currentTimeMillis();
//        if((time-exitTime)>2000){
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            exitTime = time;
//        }else{
//            finish();
//            System.exit(0);
//        }
//    }
}
