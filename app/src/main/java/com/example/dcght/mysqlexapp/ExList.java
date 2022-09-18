package com.example.dcght.mysqlexapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExList implements HttpRequest.MyCustomCallBack {
    private  ArrayList<ArrayList<String>>list;
    private ExList instance;
    public ArrayList<ArrayList<String>> getInstance()
    {
        if (instance==null)
        {
            instance=new ExList();
        }
        return list;
    }
    public  void SetExList()
    {
        HttpCall httpCallPost2 = new HttpCall();
        httpCallPost2.setMethodtype(HttpCall.GET);
        httpCallPost2.setUrl("http://sql-ex.ru/learn_exercises.php?LN=1&Lang=0");
        HashMap<String,String> params = new HashMap<>();
        //params.put("name","James Bond");
        httpCallPost2.setParams(params);
        new HttpRequest(ExList.this){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                callback.doSomething(response,"learn_ex_list");
            }
        }.execute(httpCallPost2);
    }
    public void doSomething(String result,String tag)
    {

    }
}
