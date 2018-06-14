package com.example.cui.finalhomework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shanshan.notes.DisplayActivity;
import com.example.shanshan.notes.NotesActivity;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.user_note:
                //添加跳转
                break;
            case R.id.programmer:
                //添加跳转
                break;
            default:
        }
        return true;
    }

    private Button start_law_search;
    private Button start_url;
    private Button notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         /*  批量初始化插入法条 */
        initlaw();

        sqlhelp sqldb=new sqlhelp(getApplicationContext());

        start_law_search=(Button)findViewById(R.id.button_start_search);
        start_url=(Button)findViewById(R.id.button_url);
        notes=(Button)findViewById(R.id.button_notes);

        start_law_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1=new Intent(MainActivity.this,Search.class);
                startActivity(it1);
            }
        });
        start_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1=new Intent(MainActivity.this,Urls.class);
                startActivity(it1);
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1=new Intent(MainActivity.this,DisplayActivity.class);
                startActivity(it1);
            }
        });
    }
    private String readStream(InputStream is) {
        String res;
        try {
            byte[] buf = new byte[is.available()];
            is.read(buf);
            res = new String(buf, "UTF-8");//txt采用的编码方式
            is.close();
        } catch (Exception e) {
            res = "";
        }
        return res;
    }
    private void initlaw()//初始化法条
    {
        sqlhelp sqldb=new sqlhelp(getApplicationContext());
        String setstr3=readStream(getResources().openRawResource(R.raw.law3));//获取文件 宪法
        String departstr3[]=setstr3.split("\r\n");
        for(int i=1;i<departstr3.length;i+=2)
        {
            sqldb.insert(departstr3[0],departstr3[i],departstr3[i+1]);
        }
        String setstr=readStream(getResources().openRawResource(R.raw.test));//获取文件 民法总则
        String departstr[]=setstr.split("\r\n");
        for(int i=1;i<departstr.length;i+=2)
        {
            sqldb.insert(departstr[0],departstr[i],departstr[i+1]);
        }
        String setstr2=readStream(getResources().openRawResource(R.raw.law2));//获取文件 刑法
        String departstr2[]=setstr2.split("\r\n");
        for(int i=1;i<departstr2.length;i+=2)
        {
            sqldb.insert(departstr2[0],departstr2[i],departstr2[i+1]);
        }
    }
}
