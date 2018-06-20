package com.example.shuo.quiz;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cui.finalhomework.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;

public class QuizProgressActivity extends AppCompatActivity {

    //测试用题库表名，测试规模
    String tableName;
    int quizSize;
    int currentNo;

    private TextView contentTextView;
    private TextView optionATextView;
    private TextView optionBTextView;
    private TextView optionCTextView;
    private TextView optionDTextView;

    private CheckBox checkBoxA;
    private CheckBox checkBoxB;
    private CheckBox checkBoxC;
    private CheckBox checkBoxD;

    private Button hintButton;
    private Button nextQuestionButton;

    private LibraryDatabaseHelper libraryHelper;
    private SQLiteDatabase libraryDB;

    int thisID;
    String content;
    String optionA;
    String optionB;
    String optionC;
    String optionD;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_progress);
        //初始化UI空间
        initWidget();
        //初始化程序与题目数据库之间的链接
        initConnect();
        //初始化本次测试的相关信息
        initThisQuiz();
        //开始测试
        startQuiz();
    }

    private void initWidget(){
        contentTextView = (TextView) findViewById(R.id.content_textview);
        optionATextView = (TextView) findViewById(R.id.a_textview);
        optionBTextView = (TextView) findViewById(R.id.b_textview);
        optionCTextView = (TextView) findViewById(R.id.c_textview);
        optionDTextView = (TextView) findViewById(R.id.d_textview);

        checkBoxA = (CheckBox) findViewById(R.id.a_checkbox);
        checkBoxB = (CheckBox) findViewById(R.id.b_checkbox);
        checkBoxC = (CheckBox) findViewById(R.id.c_checkbox);
        checkBoxD = (CheckBox) findViewById(R.id.d_checkbox);

        hintButton = (Button) findViewById(R.id.hint_button);
        nextQuestionButton = (Button) findViewById(R.id.next_quesiton_button);

        //设置滚动
        contentTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        optionATextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        optionBTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        optionCTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        optionDTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(QuizProgressActivity.this, "该题暂无提示",
                        Toast.LENGTH_SHORT).show();
            }
        });

        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                clearAllCheckbox();
                currentNo ++;
                displayQuestion(tableName, currentNo);
            }
        });
    }

    private void initConnect(){
        libraryHelper = new LibraryDatabaseHelper(this, "Library.db",
                null, 1);
        libraryDB = libraryHelper.getWritableDatabase();
    }

    private void initThisQuiz(){
        Intent intent = getIntent();
        tableName = intent.getStringExtra("table_name");
        quizSize = intent.getIntExtra("quiz_size", -1);

        Log.d("Received tableName", tableName);
        Log.d("Received quizSize", String .valueOf(quizSize));

        currentNo = 1;
    }

    private void startQuiz(){
        displayQuestion(tableName, currentNo);
    }

    private void displayQuestion(String paramTable, int paramItem){
        Cursor thisCursor = libraryDB.query(paramTable, null, "questionID = ?",
                new String[]{String.valueOf(paramItem)}, null, null, null);
        if(thisCursor.moveToFirst()){
            do {
                int thisID = thisCursor.getInt(thisCursor.getColumnIndex("questionID"));
                content = thisCursor.getString(thisCursor.getColumnIndex("content"));
                optionA = thisCursor.getString(thisCursor.getColumnIndex("optionA"));
                optionB = thisCursor.getString(thisCursor.getColumnIndex("optionB"));
                optionC = thisCursor.getString(thisCursor.getColumnIndex("optionC"));
                optionD = thisCursor.getString(thisCursor.getColumnIndex("optionD"));
                answer  = thisCursor.getString(thisCursor.getColumnIndex("answer"));

                Log.d("Query by table & index", String.valueOf(thisID));
                Log.d("Query by table & index", content);
                Log.d("Query by table & index", optionA);
                Log.d("Query by table & index", optionB);
                Log.d("Query by table & index", optionC);
                Log.d("Query by table & index", optionD);
                Log.d("Query by table & index", answer);

                contentTextView.setText(content);
                optionATextView.setText(optionA);
                optionBTextView.setText(optionB);
                optionCTextView.setText(optionC);
                optionDTextView.setText(optionD);
            }while (thisCursor.moveToNext());
        }
    }

    private boolean checkAnswer(){
        boolean ret;
        String thisAns = "";
        if(checkBoxA.isChecked()){
            thisAns += "A";
        }
        if (checkBoxB.isChecked()){
            thisAns += "B";
        }
        if (checkBoxC.isChecked()){
            thisAns += "C";
        }
        if (checkBoxD.isChecked()){
            thisAns += "D";
        }

        Log.d("Test", thisAns);
        Log.d("Answer", answer);

        if(thisAns.equals(answer)){
            Toast.makeText(QuizProgressActivity.this, "本题正确",
                    Toast.LENGTH_SHORT).show();
            ret=true;
        }else {
            Toast.makeText(QuizProgressActivity.this, "本次错误，正确选项为"+answer,
                    Toast.LENGTH_SHORT).show();
            ret=false;
        }

        return ret;
    }

    private void clearAllCheckbox(){
        checkBoxA.setChecked(false);
        checkBoxB.setChecked(false);
        checkBoxC.setChecked(false);
        checkBoxD.setChecked(false);
    }

    private void debugModule4(){
        Cursor cursor = libraryDB.query("year2017vol1", null, null,
                null, null, null, null);
        Log.d("Debug Module", "here");
        if(cursor.moveToFirst()){
            do{
                int thisID = cursor.getInt(cursor.getColumnIndex("questionID"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String optionA = cursor.getString(cursor.getColumnIndex("optionA"));
                String optionB = cursor.getString(cursor.getColumnIndex("optionB"));
                String optionC = cursor.getString(cursor.getColumnIndex("optionC"));
                String optionD = cursor.getString(cursor.getColumnIndex("optionD"));
                String answer  = cursor.getString(cursor.getColumnIndex("answer"));

                Log.d("Debug Module", String.valueOf(thisID));
                Log.d("Debug Module", content);
                Log.d("Debug Module", optionA);
                Log.d("Debug Module", optionB);
                Log.d("Debug Module", optionC);
                Log.d("Debug Module", optionD);
                Log.d("Debug Module", answer);
            }while(cursor.moveToNext());
        }
    }
}
