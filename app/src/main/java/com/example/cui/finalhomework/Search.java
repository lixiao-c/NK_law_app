package com.example.cui.finalhomework;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    EditText input_law_num;
    EditText edit_test;
    Button buttonserach;
    Spinner spinnerlaw;
    Spinner spinner_undefined_law;
    Button button_undifined_search;

    private List<String> list;
    private List<String> list2;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private String spinstr;
    private String spinstr2;

    /*  照片滚动*/
    private ViewPager viewPager;
    private int[] imageResIds;
    private ArrayList<ImageView> imageViewList;
    private LinearLayout ll_point_container;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sqlhelp sqldb=new sqlhelp(getApplicationContext());
        input_law_num=(EditText) findViewById(R.id.edit_input_lawnum);
        button_undifined_search=(Button)findViewById(R.id.button_undfined_search);
        buttonserach=(Button)findViewById(R.id.buttonsearch);
        spinnerlaw=(Spinner)findViewById(R.id.spDwon);
        spinner_undefined_law=(Spinner)findViewById(R.id.sp2);
        edit_test=(EditText)findViewById(R.id.test_edit);

        list=new ArrayList<String>();
        list2=new ArrayList<String>();
        //添加下拉列表
        Cursor querydata=sqldb.queryall();
        if(querydata.moveToFirst())
        {
            for(int i=0;i<querydata.getCount();i++)
            {
                list.add(querydata.getString(0));
                list2.add(querydata.getString(0));
                querydata.moveToNext();
            }
            querydata.close();
        }
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);

        /*adapter设置一个下拉列表样式，参数为系统子布局*/
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*spDown加载适配器*/
        spinnerlaw.setAdapter(adapter);
        /*soDown的监听器*/
        spinnerlaw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinstr = (String) spinnerlaw.getSelectedItem();
                //把该值传给 TextView
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        list2.add("无");
        adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list2);
        /*adapter设置一个下拉列表样式，参数为系统子布局*/
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_undefined_law.setAdapter(adapter2);
        spinner_undefined_law.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinstr2 = (String) spinner_undefined_law.getSelectedItem();
                //把该值传给 TextView
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonserach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lawnum=input_law_num.getText().toString();
                Intent it1=new Intent(Search.this,law_detail.class);
                it1.putExtra("name",spinstr);
                it1.putExtra("num",lawnum);
                startActivity(it1);
            }
        });
       button_undifined_search.setOnClickListener(new View.OnClickListener() {//模糊查询的测试
            @Override
            public void onClick(View v) {
                /* 带传递值的在intent间穿梭   */
                    Intent it1=new Intent(Search.this,display_undefinedlaw_search.class);
                    it1.putExtra("name",spinstr2);
                    it1.putExtra("content",edit_test.getText().toString());
                    startActivity(it1);
            }
        });

        /* 设置滚动图片*/
        // 初始化布局 View视图 数据
        initViews();
        initData();
        // Controller 控制器
        initAdapter();
        // 开启轮询
        new Thread() {
            public void run() {
                isRunning = true;
                while (isRunning) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();
    }
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ll_point_container = (LinearLayout) findViewById(R.id.ll_point_container);
    }
    private void initData() {
        // 图片资源id数组
        imageResIds = new int[]{R.drawable.rolling1, R.drawable.rolling2, R.drawable.rolling3, R.drawable.rolling4, R.drawable.rolling5};

        // 初始化要展示的5个ImageView
        imageViewList = new ArrayList<ImageView>();

        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < imageResIds.length; i++) {
            // 初始化要显示的图片对象
            imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResIds[i]);
            imageViewList.add(imageView);

            // 加小白点, 指示器
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.bg_board);
            layoutParams = new LinearLayout.LayoutParams(5, 5);
            if (i != 0)
                layoutParams.leftMargin = 10;
            // 设置默认所有都不可用
            pointView.setEnabled(false);
            ll_point_container.addView(pointView, layoutParams);
        }
    }

    private void initAdapter() {
        ll_point_container.getChildAt(0).setEnabled(true);
        previousSelectedPosition = 0;

        // 设置适配器
        viewPager.setAdapter(new MyAdapter());

        // 默认设置到中间的某个位置
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        viewPager.setCurrentItem(5000000); // 设置到某个位置
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        // 3. 指定复用的判断逻辑, 固定写法
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 1. 返回要显示的条目内容, 创建条目
        public Object instantiateItem(ViewGroup container, int position) {
            // container: 容器: ViewPager
            int newPosition = position % imageViewList.size();
            ImageView imageView = imageViewList.get(newPosition);
            // a. 把View对象添加到container中
            container.addView(imageView);
            // b. 把View对象返回给框架, 适配器
            return imageView;
        }

        // 2. 销毁条目
        public void destroyItem(ViewGroup container, int position, Object object) {
            // object 要销毁的对象
            container.removeView((View) object);
        }
    }
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 滚动时调用
    }

    public void onPageSelected(int position) {
        // 新的条目被选中时调用
        int newPosition = position % imageViewList.size();
        // 把之前的禁用, 把最新的启用, 更新指示器
        ll_point_container.getChildAt(previousSelectedPosition).setEnabled(false);
        ll_point_container.getChildAt(newPosition).setEnabled(true);
        // 记录之前的位置
        previousSelectedPosition = newPosition;

    }
    public void onPageScrollStateChanged(int state) {
    }
}
