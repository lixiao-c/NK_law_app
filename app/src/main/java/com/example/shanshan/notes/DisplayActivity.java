package com.example.shanshan.notes;

/**
 * Created by 533 on 2018/6/13.
 * 用于显示所有笔记的笔记首页
 * listview的item点击进入编辑页面，长按选择是否删除或清空
 */

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.cui.finalhomework.MainActivity;
import com.example.cui.finalhomework.R;

public class DisplayActivity extends ListActivity {

    private TextView tv_title,tv_add,tv_back;
    private  EditText et;
    private Button bt;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);
        final DBHelper helper=new DBHelper(this);

        Cursor c = helper.queryAll();
        String[] from={"title","date"};
        int[] to={R.id.tv_title,R.id.tv_date};
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,R.layout.notes_item,c,from,to);
        final ListView listView=getListView();
        listView.setAdapter(adapter);
        final AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        tv_title=findViewById(R.id.tv_title);
        tv_add=findViewById(R.id.tv_add);//"新建"
        tv_back=findViewById(R.id.tv_back);//"返回"
        et=findViewById(R.id.et_search);//搜索文本框
        bt=findViewById(R.id.bt_search);//“搜索”按钮

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1=new Intent(DisplayActivity.this,NotesActivity.class);
                startActivity(it1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long temp=id;
                builder1.setMessage("是否删除记录？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper.delete((int)temp);
                        Cursor c=helper.queryAll();
                        String[] from={"title","date"};
                        int[] to={R.id.tv_title,R.id.tv_date};
                        SimpleCursorAdapter adapter=new SimpleCursorAdapter(getApplicationContext(),R.layout.notes_item,c,from,to);
                        ListView listView=getListView();
                        listView.setAdapter(adapter);
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setNeutralButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper.deleteall();
                        Cursor c=helper.queryAll();
                        String[] from={"title","date"};
                        int[] to={R.id.tv_title,R.id.tv_date};
                        SimpleCursorAdapter adapter=new SimpleCursorAdapter(getApplicationContext(),R.layout.notes_item,c,from,to);
                        ListView listView=getListView();
                        listView.setAdapter(adapter);
                    }
                });
                AlertDialog ad1=builder1.create();
                ad1.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor tmp=(Cursor)listView.getItemAtPosition(position);
                String title=tmp.getString(tmp.getColumnIndex("title"));
                String content=tmp.getString(tmp.getColumnIndex("content"));
                    String No = tmp.getString(tmp.getColumnIndex("_id"));
                    Intent it1 = new Intent(DisplayActivity.this, UpdateActivity.class);
                    it1.putExtra("No", No);
                    it1.putExtra("title",title);
                    it1.putExtra("content",content);
                    startActivity(it1);
            }
            });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1=new Intent(DisplayActivity.this,MainActivity.class);
                startActivity(it1);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search=et.getText().toString();
                Intent it1=new Intent(DisplayActivity.this,SearchActivity.class);
                it1.putExtra("search",search);
                startActivity(it1);
            }
        });
    }

}