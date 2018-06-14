package com.example.shanshan.notes;
/**
 * Created by 533 on 2018/6/14.
 * 用于搜索content中含有关键词的笔记
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.cui.finalhomework.MainActivity;
import com.example.cui.finalhomework.R;

public class SearchActivity extends ListActivity {

    private TextView tv_title;
    //private ListView listview;
    private TextView tv_add;
    private TextView tv_back;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);
        Intent intent=getIntent();
        String s=intent.getStringExtra("search");//获取intent传过来的搜索值
        final DBHelper helper=new DBHelper(this);

        //搜索后结果放入listview
        Cursor c = helper.queryContent(s);
        String[] from={"title","date"};
        int[] to={R.id.tv_title,R.id.tv_date};
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,R.layout.notes_item,c,from,to);
        final ListView listView=getListView();
        listView.setAdapter(adapter);
        final AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        //设置监听器
        tv_title=findViewById(R.id.tv_title);
        tv_add=findViewById(R.id.tv_add);//"新建"
        tv_back=findViewById(R.id.tv_back);//"返回"

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1=new Intent(SearchActivity.this,NotesActivity.class);
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
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor tmp=(Cursor)listView.getItemAtPosition(position);
                String title=tmp.getString(tmp.getColumnIndex("title"));
                String content=tmp.getString(tmp.getColumnIndex("content"));
                String No = tmp.getString(tmp.getColumnIndex("_id"));
                Intent it1 = new Intent(SearchActivity.this, UpdateActivity.class);
                it1.putExtra("No", No);
                it1.putExtra("title",title);
                it1.putExtra("content",content);
                startActivity(it1);
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1=new Intent(SearchActivity.this,MainActivity.class);
                startActivity(it1);
            }
        });
    }

}
