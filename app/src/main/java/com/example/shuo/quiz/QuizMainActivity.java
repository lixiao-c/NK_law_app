package com.example.shuo.quiz;

import com.example.cui.finalhomework.*;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuizMainActivity extends AppCompatActivity {

    ArrayList questionLibrary;

    private LibraryDatabaseHelper libraryHelper;
    private SQLiteDatabase libraryDB;

    private RadioButton actualRadioButton;
    private RadioButton generateRadioButton;
    private EditText generateQuizSizeEditText;
    private ListView quizListView;
    private Button startQuizButton;

    private RadioGroup selRadioGroup;

    //所请求的题库表名
    String selTable;
    //测试的规模大小
    int selSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        //设置本应用当前所支持的题库列表
        setLibrary();
        //初始化应用与数据库之间的链接
        initConnect();
        //通过试探，判断是否自动对题库内容进行初始化
        //通过一个较为粗糙的“试探”，判断是否需要初始化题目的内容。
        //避免每次加载程序时均需重新初始化题库，影响效率
        attemptInitLibrary();
        //initLibraryContent();
        //初始化UI控件
        initWidget();
    }

    private void setLibrary(){
        questionLibrary = new ArrayList();

        //添加题库
        //注：此处可以进行扩展
        questionLibrary.add("year2017vol1");
        questionLibrary.add("year2017vol2");
    }

    private void initConnect(){
        //链接数据库
        libraryHelper = new LibraryDatabaseHelper(this, "Library.db",
                questionLibrary, null, 1);
        libraryDB = libraryHelper.getWritableDatabase();
    }

    private void attemptInitLibrary(){
        //通过试探判断是否需要初始化题库内容
        //判断表名链表中的第一个名称是否存在于现有的数据库当中，若有则说明该数据库工作运行状态十分良好
        boolean toInit = true;
        String attemptTable = questionLibrary.get(0).toString();
        Cursor cursor = libraryDB.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while(cursor.moveToNext()){
            //遍历所有表
            String name = cursor.getString(0);
            if(name.equals(attemptTable)){
                toInit = false;
                break;
            }
        }
        //若测试失败，则初始化题库内容（该过程耗时较长）
        if(toInit){
            initLibraryContent();
        }
    }

    private void initLibraryContent(){
        //正则表达式
        String regContent = "\\s+\\d+\\.(.+)";
        Pattern patContent = Pattern.compile(regContent);
        Matcher matContent = null;
        String regAnswer = "Answer:(\\w+)";
        Pattern patAnswer = Pattern.compile(regAnswer);
        Matcher matAnswer = null;
        String regOptionA = "\\s+A\\.(.+)";
        Pattern patOptionA = Pattern.compile(regOptionA);
        Matcher matOptionA = null;
        String regOptionB = "\\s+B\\.(.+)";
        Pattern patOptionB = Pattern.compile(regOptionB);
        Matcher matOptionB = null;
        String regOptionC = "\\s+C\\.(.+)";
        Pattern patOptionC = Pattern.compile(regOptionC);
        Matcher matOptionC = null;
        String regOptionD = "\\s+D\\.(.+)";
        Pattern patOptionD = Pattern.compile(regOptionD);
        Matcher matOptionD = null;

        //导入题库
        for(int i=0; i<questionLibrary.size(); i++){
            //遍历题库链表，对每一个题库的内容进行导入
            //当前导入的表名
            String tableName = questionLibrary.get(i).toString();
            Log.d("Table name", tableName);

            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            String resName = tableName;
            int resID = getResources().getIdentifier(resName, "raw", getBaseContext().getPackageName());
            try {
                inputStream = this.getResources().openRawResource(resID);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //单道题的信息插入容器
                ContentValues insertValue = new ContentValues();
                //单道题的信息是否读取完毕
                boolean infoComplete = false;
                //当前读入的信息行
                String thisLine;
                //提取的各单项信息
                String content = null;
                String answer = null;
                String optionA = null;
                String optionB = null;
                String optionC = null;
                String optionD = null;
                while((thisLine = bufferedReader.readLine()) != null){
                    Log.d("This Line", thisLine);
                    //以正则规则匹配当前读入行
                    matContent = patContent.matcher(thisLine);
                    matAnswer = patAnswer.matcher(thisLine);
                    matOptionA = patOptionA.matcher(thisLine);
                    matOptionB = patOptionB.matcher(thisLine);
                    matOptionC = patOptionC.matcher(thisLine);
                    matOptionD = patOptionD.matcher(thisLine);

                    //判断
                    if(matContent.matches()){
                        content= matContent.group(1);
                        Log.d("Content", content);
                    }else if (matAnswer.matches()){
                        answer = matAnswer.group(1);
                        Log.d("Answer", answer);
                    } else if (matOptionA.matches()){
                        optionA = matOptionA.group(1);
                        Log.d("Option A", optionA);
                    }else if (matOptionB.matches()){
                        optionB = matOptionB.group(1);
                        Log.d("Option B", optionB);
                    }else if (matOptionC.matches()){
                        optionC = matOptionC.group(1);
                        Log.d("Option C", optionC);
                    }else if (matOptionD.matches()){
                        optionD = matOptionD.group(1);
                        Log.d("Option D", optionD);

                        //D选项读取完毕，该题信息读取完成
                        infoComplete = true;
                    }else {
                        //Do Nothing
                    }

                    if(infoComplete){
                        //如单道题的信息读取完毕，则将该题信息插入题目数据库中
                        insertValue.put("content", content);
                        insertValue.put("optionA", optionA);
                        insertValue.put("optionB", optionB);
                        insertValue.put("optionC", optionC);
                        insertValue.put("optionD", optionD);
                        insertValue.put("answer", answer);
                        libraryDB.insert(tableName, null, insertValue);
                        insertValue.clear();
                        infoComplete=false;
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(bufferedReader != null){
                    try{
                        bufferedReader.close();
                        Log.d("Debug Module", "Close Reader");
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initWidget(){
        actualRadioButton = (RadioButton) findViewById(R.id.actual_radiobutton);
        generateRadioButton = (RadioButton) findViewById(R.id.generate_radiobutton);
        generateQuizSizeEditText = (EditText) findViewById(R.id.generate_quiz_size_edittext);
        quizListView = (ListView) findViewById(R.id.quiz_listview);
        startQuizButton = (Button) findViewById(R.id.start_quiz_button);
        selRadioGroup = (RadioGroup) findViewById(R.id.sel_radiogroup);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(QuizMainActivity.this,
                android.R.layout.simple_list_item_1, questionLibrary);
        quizListView.setAdapter(adapter);

        actualRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQuizSizeEditText.setEnabled(false);
            }
        });

        generateRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQuizSizeEditText.setEnabled(true);
            }
        });

        quizListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selTable = questionLibrary.get(i).toString();
                Toast.makeText(QuizMainActivity.this, "您选择了"+selTable,
                        Toast.LENGTH_SHORT).show();
            }
        });

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkFlag1=false;
                boolean checkFlag2=false;
                if(actualRadioButton.isChecked()){
                    //原始题集进行测试，不做处理
                    checkFlag1=true;
                }
                else if(generateRadioButton.isChecked()){
                    //生成题集进行测试
                    String editTextContent = generateQuizSizeEditText.getText().toString();
                    if(editTextContent.isEmpty()){
                        Toast.makeText(QuizMainActivity.this, "尚未选择测试规模",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        selSize = Integer.parseInt(editTextContent);
                        checkFlag1=true;
                    }
                }
                else {
                    Toast.makeText(QuizMainActivity.this, "尚未选择测试类型",
                            Toast.LENGTH_SHORT).show();
                }

                if(selTable == null){
                    Toast.makeText(QuizMainActivity.this, "尚未选择测试题集",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    checkFlag2=true;
                }

                Intent intent = new Intent(QuizMainActivity.this, QuizProgressActivity.class);
                //传递参数，包括请求的表格名以及请求的问题规模
                intent.putExtra("table_name", selTable);
                intent.putExtra("quiz_size", selSize);
                if(checkFlag1 && checkFlag2){
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quiz_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.upgrade_item:
                Toast.makeText(this, "题库进行更新", Toast.LENGTH_SHORT).show();
                initLibraryContent();
                break;
            case R.id.module_info_item:
                Toast.makeText(this, "本模块开发者：Li Tianshuo\nEmail： litianshuo@mail.nankai.edu.cn",
                        Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }
}
