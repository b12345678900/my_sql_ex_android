package com.example.dcght.mysqlexapp.ExFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dcght.mysqlexapp.ExActivity;
import com.example.dcght.mysqlexapp.Excersice;
import com.example.dcght.mysqlexapp.HttpCall;
import com.example.dcght.mysqlexapp.HttpRequest;
import com.example.dcght.mysqlexapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

interface EditCodeDelegate
{
    String getCode();
}
public class runFragment extends Fragment implements HttpRequest.MyCustomCallBack {
    public EditCodeDelegate codeFragmentDelegate;
    final public int LEARN_STAGE=0;
    final public int RATING_STAGE=1;
    final public int DML_STAGE=2;
    final public String[]nameStage={"Обучающий этап","Рейтинговый этап","DML"};
    String[]subd={"MSSQL","MYSQL","PGSQL","ORACLE"};
    private String login;
    private String password;
    String ExNmber;
    TextView tv;
    EditText et;
    WebView wV;
    LinearLayout resCont;
    String Stage;
    Spinner selSubd;
    CheckBox withoutCheckFlag;
    private ProgressBar pB;
    public runFragment() {
        // Required empty public constructor
    }
    private void excecute (HashMap<String,String> params, String url)
    {
        if (url==null) {
            switch (Stage) {
                case LEARN_STAGE + "":
                    if (selSubd.getSelectedItem().toString()!="MSSQL") {
                        url = "https://sql-ex.ru/exercises/index.php?act=check";
                    }
                    else
                    {
                        url = "https://sql-ex.ru/learn_exercises.php";
                    }
                    break;
                case RATING_STAGE + "":
                    url = "https://sql-ex.ru/exercises.php";
                    break;
                case DML_STAGE + "":
                    url = "https://sql-ex.ru/dmlexercises.php";
                    break;

            }
        }
        HttpCall httpCallPost1 = new HttpCall();
        httpCallPost1.setMethodtype(HttpCall.POST);
        httpCallPost1.setUrl(url);
        httpCallPost1.setParams(params);
        new HttpRequest(this){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);

                callback.doSomething(response,"answer");
            }
        }.execute(httpCallPost1);

    }

    // TODO: Rename and change types and number of parameters
    public static runFragment newInstance(Excersice ex) {

        runFragment item = new runFragment();

            Bundle args = new Bundle();
            args.putString("stage",ex.Stage);
            args.putString("ex_numb",ex.ExNumber);
            item.setArguments(args);



        return item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Stage=getArguments().getString("stage");
            ExNmber=getArguments().getString("ex_numb");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_run, container, false);
        selSubd=view.findViewById(R.id.select_subd);
        withoutCheckFlag=view.findViewById(R.id.no_check);
        resCont =(LinearLayout) view.findViewById(R.id.res_content);
        wV=view.findViewById(R.id.resultWebView);
        wV.setVisibility(View.INVISIBLE);
        pB=view.findViewById(R.id.progressBar);
        view.findViewById(R.id.ok_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wV.loadData("","text/html; charset=UTF-8",null);
                wV.setVisibility(View.INVISIBLE);
                HashMap<String,String> paramsPost1 = new HashMap<>();
                if(Integer.parseInt(Stage)==LEARN_STAGE && selSubd.getSelectedItem().toString()!="MSSQL" )
                {


                    paramsPost1.put("num", "" + ExNmber);
                    excecute(paramsPost1,"https://sql-ex.ru/exercises/index.php?act=showokres");


                }
                else {

                    paramsPost1.put("showOKres", "" + ExNmber);
                    paramsPost1.put("wo", "false");
                    excecute(paramsPost1,null);
                }

            }
        });


        view.findViewById(R.id.btn_exec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wV.loadData("","text/html; charset=UTF-8",null);
                wV.setVisibility(View.INVISIBLE);
                String code=codeFragmentDelegate.getCode();

                HashMap<String,String> paramsPost1 = new HashMap<>();

                if(Integer.parseInt(Stage)==LEARN_STAGE && selSubd.getSelectedItem().toString()!="MSSQL")
                {

                    paramsPost1.put("query", code);
                    paramsPost1.put("num", "" + ExNmber);


                    paramsPost1.put("check",""+withoutCheckFlag.isChecked());

                }
                else {
                    paramsPost1.put("txtsql", code);
                    paramsPost1.put("checkMe", "" + ExNmber);
                    paramsPost1.put("CHB", "" + 1);
                    if (withoutCheckFlag.isChecked())
                    {
                        paramsPost1.put("wo",""+1);
                    }
                }



                excecute(paramsPost1,null);

            }
        });

        selSubd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                HashMap<String, String> paramsPost1 = new HashMap<>();
                HttpCall httpCallPost1 = new HttpCall();
                httpCallPost1.setMethodtype(HttpCall.POST);
                httpCallPost1.setUrl("https://sql-ex.ru/exercises/index.php?act=choisedb");
                HashMap<String, String> params = new HashMap<>();
                params.put("dbname", subd[position].toLowerCase());
                httpCallPost1.setParams(params);
                pB.setVisibility(View.VISIBLE);
                new HttpRequest(runFragment.this) {
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);

                        callback.doSomething(response,"subdChanged");
                    }
                }.execute(httpCallPost1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        if(Integer.parseInt(this.Stage)==LEARN_STAGE) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subd);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selSubd.setAdapter(adapter);

            selSubd.setSelection(0);
        }
        else
        {
            selSubd.setVisibility(View.INVISIBLE);
        }


        return  view;
    }


    @Override
    public void doSomething(String someResult, String tag) {
        if (tag=="answer")
        {
            String answer="";

            if(Integer.parseInt(Stage)!=LEARN_STAGE || selSubd.getSelectedItem().toString()=="MSSQL") {
                Pattern p;
                Matcher mc;
                p = Pattern.compile("<a name='answer_ref'></a>[\\s\\S]*?<div style=\"float:left\">|Execution time[\\s\\S]*?<div style=\"float:left\"", CASE_INSENSITIVE);
                mc = p.matcher(someResult);
                if (mc.find()) {
                    answer = mc.group(0);
                }
                answer=getString(R.string.template)+answer+" </body>\n" + "</html>";
            }
            else
            {
                answer=someResult;
                Pattern p1=Pattern.compile("^<table");
                Matcher mc=p1.matcher(someResult);

                try {
                    if(!mc.matches())
                    {
                        JSONObject jObject = new JSONObject(someResult);
                        answer= jObject.getString("resplace");
                        Log.d("aaa","ddd");
                    }

                }
                catch (Exception e)
                {
                    String c="4";
                }
                answer=getString(R.string.template_for_learn)+answer+" </body>\n" + "</html>";

            }


            wV.setVisibility(View.VISIBLE);
            wV.loadData(answer,"text/html; charset=UTF-8", null);




        }
        else if (tag=="subdChanged")
        {
            if (someResult=="error")
            {
                Toast.makeText(getContext(),"Субд не изменена", Toast.LENGTH_SHORT).show();
            }
            pB.setVisibility(View.INVISIBLE);
        }
    }
}
