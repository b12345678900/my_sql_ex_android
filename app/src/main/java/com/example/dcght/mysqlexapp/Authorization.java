package com.example.dcght.mysqlexapp;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;

public class Authorization {



    public static void Auth(String login, String password, final HttpRequest.MyCustomCallBack act)  {

        HttpCall httpCallPost1 = new HttpCall();
        httpCallPost1.setMethodtype(HttpCall.POST);
        httpCallPost1.setUrl("https://sql-ex.ru/index.php");
        HashMap<String,String> paramsPost1 = new HashMap<>();

        paramsPost1.put("login",login);
        paramsPost1.put("psw",password);
        httpCallPost1.setParams(paramsPost1);
        new HttpRequest(act){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);

                callback.doSomething(response,"auth");
            }
        }.execute(httpCallPost1);


    }
}
