package com.example.cui.finalhomework;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class display_undefinedlaw_search extends AppCompatActivity {
    private List<law_display> lawslist=new ArrayList<>();//法条列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_undefinedlaw_search);

        Intent intget=getIntent();
        String getname=intget.getStringExtra("name");
        String getcontent=intget.getStringExtra("content");

        sqlhelp sqldb=new sqlhelp(getApplicationContext());
        Cursor cursor;
        if(getname.equals("无"))
        {
            cursor=sqldb.query_undefined(getcontent);
            Log.v("1","enter");
        }
        else
        {
            Log.v("1","enter2");
            cursor=sqldb.query_undefined_accname(getcontent,getname);
        }
       if(cursor.moveToFirst())
       {
           for(int i=0;i<cursor.getCount();i++) {

               String lawname = cursor.getString(cursor.getColumnIndex("lawname"));
               String lawnum = cursor.getString(cursor.getColumnIndex("lawnum"));
               String lawcontent = cursor.getString(cursor.getColumnIndex("lawcontent"));
               law_display law_display = new law_display(lawname, lawnum, lawcontent);
               lawslist.add(law_display);
               cursor.moveToNext();

           }
       }
       cursor.close();

        law_display_adapter adapter=new law_display_adapter(display_undefinedlaw_search.this,R.layout.law_item,lawslist);
        ListView listView=(ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View view1 = parent.getChildAt(position);
                TextView tv1 = (TextView) view1.findViewById(R.id.lawname_display);
                TextView tv2=(TextView)view1.findViewById(R.id.lawnum_displau);
                Intent it1=new Intent(display_undefinedlaw_search.this,law_detail.class);
                it1.putExtra("name",tv1.getText().toString());
                it1.putExtra("num",tv2.getText().toString());
                startActivity(it1);
            }
        });
    }
}
