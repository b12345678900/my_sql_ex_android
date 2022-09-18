package com.example.dcght.mysqlexapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements HttpRequest.MyCustomCallBack {
    private String login;
    private String password;
    SimpleExpandableListAdapter adapter;
    ExpandableListView elvMain;
    final int CONN_ERROR=0;
final int LEARN_STAGE=0;
final int RATING_STAGE=1;
    final int DML_STAGE=2;
private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
         //textView=(TextView) findViewById(R.id.auth_res);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        login="b12345";
        password="79161418656";
        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
        elvMain.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        final int groupPosition, final int childPosition, long id) {
                String childText=getChildText(groupPosition,childPosition);
                if (!childText.contains("Ok")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Начать решать?");
                    builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getEx(groupPosition, childPosition);
                        }
                    });
                    builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.create().show();
                }
                else
                {
                    getEx(groupPosition, childPosition);
                }
                return false;
            }
        });
         Authorization.Auth(login, password,MainActivity.this);


    }
    private void getEx ( int groupPosition,   int childPosition)
    {
        String childText=getChildText(groupPosition,childPosition);
        String ExNumber="";
        Pattern p = Pattern.compile("^-?\\d+");
        Matcher mc = p.matcher(childText);
        if (mc.find())
        {
            ExNumber= mc.group(0);
        }
        Intent intent = new Intent(MainActivity.this, ExActivity.class);
        intent.putExtra("ExNumber",ExNumber);
        intent.putExtra("stage",""+groupPosition);
        startActivity(intent);
    }
    String getGroupText(int groupPos) {
        return ((Map<String,String>)(adapter.getGroup(groupPos))).get("groupName");
    }
    String getChildText(int groupPos, int childPos) {
        return ((Map<String,String>)(adapter.getChild(groupPos, childPos))).get("elementString");
    }
    public void doSomething(String result,String tag) {
        if (tag == "auth") {
            if (result != null) {
                getExList();

            }

        }
    }
    public void getExList()
    {
        Log.d("aaa","sssss");
        HttpRequest learn_ex_list;
        String learn="";
        HttpCall httpCallPost2 = new HttpCall();
        httpCallPost2.setMethodtype(HttpCall.GET);
        httpCallPost2.setUrl("https://sql-ex.ru/learn_exercises.php?Lang=0");
        HashMap<String,String> params = new HashMap<>();
        //params.put("name","James Bond");
        httpCallPost2.setParams(params);
        learn_ex_list=new HttpRequest(MainActivity.this);
        learn_ex_list.execute(httpCallPost2);
        try {
            learn= learn_ex_list.get();
        }
        catch (Exception e) {
        }


        HttpRequest rate_ex_list;
        String rate="";
        HttpCall httpCallPost3 = new HttpCall();
        httpCallPost3.setMethodtype(HttpCall.GET);
        httpCallPost3.setUrl("https://sql-ex.ru/exercises.php?Lang=0");
        HashMap<String,String> params1 = new HashMap<>();
        //params.put("name","James Bond");
        httpCallPost3.setParams(params1);
        learn_ex_list=new HttpRequest(MainActivity.this);
        learn_ex_list.execute(httpCallPost3);
        try {
            rate= learn_ex_list.get();
        }
        catch (Exception e) {
        }

        HttpRequest dml_ex_list;
        String dml="";
        HttpCall httpCallPost4 = new HttpCall();
        httpCallPost4.setMethodtype(HttpCall.GET);
        httpCallPost4.setUrl("https://sql-ex.ru/dmlexercises.php?Lang=0");
        HashMap<String,String> params2 = new HashMap<>();
        //params.put("name","James Bond");
        httpCallPost4.setParams(params1);
        learn_ex_list=new HttpRequest(MainActivity.this);
        learn_ex_list.execute(httpCallPost4);
        try {
            dml= learn_ex_list.get();
        }
        catch (Exception e) {
        }
        if (rate=="error" || dml=="error" || learn=="error")
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.connection_error);
            builder.setPositiveButton(R.string.btn_reload_txt, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Authorization.Auth(login, password,MainActivity.this);
                }
            });
            builder.create().show();
            return;
        }
        Pattern p = Pattern.compile("<select id=\"LN\" name=\"LN\" style=\"background-color:#AAD52B;\" OnChange=\"learnSelPickEx.*\">(<option.*)</select>");
        Matcher mc = p.matcher(learn);
        if(mc.find()) {
            learn = mc.group(1);
        }
/*String temp="";
p=Pattern.compile("(.*)<hr width=\"85%\"><div align='left'>");
mc=p.matcher(rate);
        if(mc.find()) {
            rate = mc.group(1);
        }*/
         p = Pattern.compile("<select.*style=\"background-color:#D3DCE3;\" OnChange=\"selPickEx.*\">(<option[\\s\\S]+?)</select>");

        mc = p.matcher(rate);
        if(mc.find()) {
            rate = mc.group(1);
       }
Log.d("aaa",rate);
        p = Pattern.compile("<select id=\"N\" name=\"N\" style=\"background-color:#d9e3d8;\" OnChange=\"dmlPickEx.*\">(<option.*)</select>");
        mc = p.matcher(dml);
        if(mc.find()) {
            dml = mc.group(1);
        }

        ArrayList<Map<String, String>> groupData;
        ArrayList<Map<String, String>> childDataItem;
        ArrayList<ArrayList<Map<String, String>>> childData;
        childData=new ArrayList<ArrayList<Map<String, String>>>();
        groupData=new  ArrayList<Map<String, String>>();
        Map<String, String> m;
        m = new HashMap<String, String>();
        m.put("groupName", "Обучающий этап");
        groupData.add(m);
        m =new HashMap<String, String>();
        m.put("groupName", "Рейтинговый этап");
        groupData.add(m);
        m =new HashMap<String, String>();
        m.put("groupName", "DML");
        groupData.add(m);

        p=Pattern.compile("((?<=((selected)|('))>)(.*?)(?=<)){1}?");
        learn=learn+"<";
        mc=p.matcher(learn);
        childDataItem=new ArrayList<Map<String, String>>();
        while(mc.find())
        {
            m =new HashMap<String, String>();
            m.put("elementString",learn.substring(mc.start(),mc.end()));
            childDataItem.add(m);
        }
        childData.add(childDataItem);

        p=Pattern.compile("((?<=((selected)|('))>)(.*?)(?=<)){1}?");
        rate=rate+"<";
        mc=p.matcher(rate);
        childDataItem=new ArrayList<Map<String, String>>();
        while(mc.find())
        {
            m =new HashMap<String, String>();
            m.put("elementString",rate.substring(mc.start(),mc.end()));
            childDataItem.add(m);
        }
        childData.add(childDataItem);

        p=Pattern.compile("((?<=((selected)|('))>)(.*?)(?=<)){1}?");
        dml=dml+"<";
        mc=p.matcher(dml);
        childDataItem=new ArrayList<Map<String, String>>();
        while(mc.find())
        {
            m =new HashMap<String, String>();
            m.put("elementString",dml.substring(mc.start(),mc.end()));
            childDataItem.add(m);
        }
        childData.add(childDataItem);
        int groupTo[] = new int[] {android.R.id.text1};
        String groupFrom[] = new String[] {"groupName"};
        String childFrom[] = new String[] {"elementString"};
        int childTo[] = new int[] {android.R.id.text1};

         adapter = new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo);


        elvMain.setAdapter(adapter);
    }


}
