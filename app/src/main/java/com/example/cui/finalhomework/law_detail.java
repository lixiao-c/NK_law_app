package com.example.cui.finalhomework;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class law_detail extends AppCompatActivity {

    private TextView text_name;
    private TextView text_num;
    private TextView text_content;
    private Button button_next;
    private Button button_form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_detail);
        Intent intget=getIntent();
        final String getname=intget.getStringExtra("name");
        String getnum=intget.getStringExtra("num");
        String lawname;

        text_name=(TextView)findViewById(R.id.detail_name);
        text_num=(TextView)findViewById(R.id.detail_num);
        text_content=(TextView)findViewById(R.id.detail_content);
        button_form=(Button)findViewById(R.id.formnum);
        button_next=(Button)findViewById(R.id.nextnum);

        sqlhelp sqldb=new sqlhelp(getApplicationContext());
                Cursor querydata=sqldb.query_accord_numandname(getname,getnum);
                if(querydata.moveToFirst())
                {
                    lawname=querydata.getString(querydata.getColumnIndex("lawname"));
                    text_name.setText("《"+lawname+"》");
                    String lawnum=querydata.getString(querydata.getColumnIndex("lawnum"));
                    text_num.setText("第 "+lawnum+" 条");
                    String lawcontent=querydata.getString(querydata.getColumnIndex("lawcontent"));
                    text_content.setText(lawcontent);
                }
                else{
                    text_name.setText("没有找到相关法条");
                }
                querydata.close();
        button_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_num.getText().toString() == "") {
                    /* do nothing */
                } else {
                    String getnum_again = text_num.getText().toString();
                    String num[] = getnum_again.split(" ");
                    getnum_again = num[1];
                    int i = Integer.parseInt(getnum_again);
                    if (i <= 1) {
                        Toast.makeText(law_detail.this, "已经是第一条", Toast.LENGTH_LONG).show();
                    } else {
                        i--;
                        sqlhelp sqldb = new sqlhelp(getApplicationContext());
                        Cursor querydata2 = sqldb.query_accord_numandname(getname, "" + i);
                        if (querydata2.moveToFirst()) {
                            text_num.setText("第 " + i + " 条");
                            String lawcontent = querydata2.getString(querydata2.getColumnIndex("lawcontent"));
                            text_content.setText(lawcontent);
                        }
                        querydata2.close();
                    }
                }
            }
        });
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_num.getText().toString() == "") {
                    /* do nothing */
                } else {
                    String getnum_again = text_num.getText().toString();
                    String num[] = getnum_again.split(" ");
                    getnum_again = num[1];
                    int i = Integer.parseInt(getnum_again);
                    i++;
                    sqlhelp sqldb = new sqlhelp(getApplicationContext());
                    Cursor querydata2 = sqldb.query_accord_numandname(getname, "" + i);
                    if (querydata2.moveToFirst()) {
                        text_num.setText("第 " + i + " 条");
                        String lawcontent = querydata2.getString(querydata2.getColumnIndex("lawcontent"));
                        text_content.setText(lawcontent);
                    } else {
                        Toast.makeText(law_detail.this, "已经是最后一条", Toast.LENGTH_LONG).show();
                    }
                    querydata2.close();
                }
            }
        });


    }
}
