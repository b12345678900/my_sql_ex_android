package com.example.dcght.mysqlexapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import com.example.dcght.mysqlexapp.ui.main.SectionsPagerAdapter;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExActivity extends AppCompatActivity implements HttpRequest.MyCustomCallBack {
    final public int LEARN_STAGE=0;
    final public int RATING_STAGE=1;
    final public int DML_STAGE=2;
    String ExNmber;
    String Stage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_layout);
        Intent intent=getIntent();

         ExNmber=intent.getStringExtra("ExNumber");
         Stage=intent.getStringExtra("stage");
        if (savedInstanceState==null) {
            getEx(Integer.parseInt(ExNmber), Integer.parseInt(Stage));
        }
    }
    private void setupSectionAdapter(Excersice ex)
    {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), ex);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()!=1)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void doSomething(String someResult, String tag) {
        if (tag=="task")
        {
            if(someResult=="error"  )
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(ExActivity.this);
                builder.setMessage(R.string.connection_error);
                builder.setPositiveButton(R.string.btn_reload_txt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getEx(Integer.parseInt(ExNmber),Integer.parseInt(Stage));
                    }
                });
                builder.create().show();
                return;

            }
            String descBase="";
            String taskText="";
            String savedQuery="";
            String forum_url="";
            Pattern p = Pattern.compile("(?<=<td colspan=\"3\" bgcolor=\"#\\S{6}\" valign=\"middle\" align=\"left\">)([\\s\\S]+)<A (class=\"let\" )?href=\"javascript:openHelp");
            Matcher mc = p.matcher(someResult);
            if(mc.find()) {
                descBase = mc.group(1);
            }
            descBase=descBase.replaceAll("<(br|BR) ?/?>", "\n");
            descBase=descBase.replaceAll("</?B>", "");


            p = Pattern.compile("(?<=<!-- Задание: -->)[\\s\\S]+?(?=(<p>)|(<font color))");
            mc = p.matcher(someResult);
            if(mc.find()) {
                taskText = mc.group(0);
            }
            taskText=taskText.replaceAll("<(br|BR) ?/?>", "\n");
            taskText=taskText.replaceAll("</?B>", "");

            p = Pattern.compile("<textarea.*id=\"txtsql\"[\\s\\S]*?>([\\s\\S]+)</textarea>");
            mc = p.matcher(someResult);
            if(mc.find()) {
                savedQuery = mc.group(1);
            }
            p=Pattern.compile("(?<=<[Aa] class='let' href=')/forum\\S+(?='>)");
            mc=p.matcher(someResult);
            if(mc.find()) {
                forum_url = "https://www.sql-ex.ru"+mc.group(0);
            }
            String db="db"+ RegExHelper.getMatch(someResult,"/help/select13.php#db_(\\d)",1 );
            Excersice ex=new Excersice();
            ex.db=db;
            ex.descBase=descBase;
            ex.savedQuery=savedQuery;
            ex.taskText=taskText;
            ex.Stage=Stage;
            ex.ExNumber=ExNmber;
            ex.forum_url=forum_url;
            setupSectionAdapter(ex);

        }
    }
    //help/select13.php#db_
    protected void getEx (int exNum, int stage)
    {
        String url="";
        switch (stage)
        {
            case LEARN_STAGE:     url="https://sql-ex.ru/learn_exercises.php?LN="+exNum;
                break;
            case RATING_STAGE:     url="https://sql-ex.ru/exercises.php?N="+exNum;
                break;
            case DML_STAGE:     url="https://sql-ex.ru/dmlexercises.php?N="+exNum;
                break;

        }
        HttpCall httpCallPost1 = new HttpCall();
        httpCallPost1.setMethodtype(HttpCall.GET);
        httpCallPost1.setUrl(url);
        HashMap<String,String> params = new HashMap<>();
        httpCallPost1.setParams(params);
        new HttpRequest(ExActivity.this){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);

                callback.doSomething(response,"task");
            }
        }.execute(httpCallPost1);
    }

}