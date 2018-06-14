package com.example.shanshan.notes;

/**
 * Created by 533 on 2018/6/13.
 * 添加新笔记，保存时会检查是否为空
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cui.finalhomework.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotesActivity extends Activity {
    private DBHelper db = null;
    private EditText et1,et2;
    private TextView tv1,tv2,tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotes);
        db = new DBHelper(this);

        et1=findViewById(R.id.et_content);//笔记内容
        et2=findViewById(R.id.et_title);//标题
        tv1=findViewById(R.id.tv_save);//“保存”
        tv2=findViewById(R.id.tv_cancel);//“取消”
        tv3=findViewById(R.id.tv_date);//日期

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = sdf.format(date);
        tv3.setText(dateString);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et1.getText())) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                    builder.setMessage("请填写笔记内容！").setNeutralButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }
                    );
                    AlertDialog ad = builder.create();
                    ad.show();
                } else {
                    String content = et1.getText().toString();
                    String title=et2.getText().toString();
                    //获取写日志的时间
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateNum = sdf.format(date);

                    ContentValues values = new ContentValues();
                    values.put("content", content);
                    values.put("title",title);
                    values.put("date",dateNum);

                    DBHelper helper = new DBHelper(getApplicationContext());
                    helper.insert(values);

                    Intent intent = new Intent(NotesActivity.this, DisplayActivity.class);
                    startActivity(intent);
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, DisplayActivity.class);
                startActivity(intent);
            }
        });
    }


}

