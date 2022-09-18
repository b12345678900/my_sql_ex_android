package com.example.dcght.mysqlexapp.ExFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.dcght.mysqlexapp.Comment;
import com.example.dcght.mysqlexapp.Excersice;
import com.example.dcght.mysqlexapp.HttpCall;
import com.example.dcght.mysqlexapp.HttpRequest;
import com.example.dcght.mysqlexapp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;


public class forumfragment extends Fragment implements HttpRequest.MyCustomCallBack, forumFragmentDelegate {


    private String kolvoStranic ;
    private RecyclerView commentList;
    private  String currentPage ;
    private HashMap<String,String> postParams;
    private String forum_url;
    private Spinner current_page_tv;

    private commentAdapter adapter;
    private View view;
    private LinearLayout buttons;

    public void onResume() {
        super.onResume();

        if (!getUserVisibleHint())
        {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    public void notife(int id) {
        int current_page=Integer.parseInt(currentPage);
        int countOfPages=Integer.parseInt(kolvoStranic);


        switch (id)
        {
            case R.id.previous_page:

                if(current_page>1) {
                    loadData( current_page-1);
                }
                break;
            case R.id.next_page:

                if(current_page<countOfPages) {
                    loadData( current_page+1);
                }
                break;
            case R.id.last_page:


                loadData( countOfPages);

                break;
            case R.id.first_page:


                loadData(1);

                break;
        }
    }

    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {

            onResume();
        }
    }



    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_com, container, false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
      commentList=view.findViewById(R.id.commentRV);
      commentList.setLayoutManager(layoutManager);
      commentList.setHasFixedSize(true);

      buttons=view.findViewById(R.id.button_layout);
      current_page_tv=(Spinner) view.findViewById(R.id.current_page);
        if (buttons!=null) {
            int count = buttons.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = buttons.getChildAt(i);
                if (v.getId()!=R.id.current_page) {
                    ((Button) v).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notife(v.getId());
                        }
                    });
                }
            }
        }
        //lv.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);
        SpinnerInteractionListener listenner=new SpinnerInteractionListener(this);
       current_page_tv.setOnItemSelectedListener(listenner);
       current_page_tv.setOnTouchListener(listenner);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(commentList.getContext(),
                layoutManager.getOrientation());

        commentList.addItemDecoration(dividerItemDecoration);
        loadData( 0);

        return view;
    }

   /* private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            lv.getViewTreeObserver().removeOnPreDrawListener(this);
            lv.setVisibility(View.VISIBLE);

            return true;
        }
    };*/
    private  int convertDpToPixels(float dp) {
        Context context=getActivity();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }
    public void loadData( int page)
    {
       String url=forum_url;
        if (url!="") {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl( url);
            HashMap<String, String> params = new HashMap<>();
            if (page != 0) {
                params = postParams;
                params.put("textbox", "" + page);
                httpCall.setMethodtype(HttpCall.POST);
            }
            httpCall.setParams(params);
            new HttpRequest(forumfragment.this) {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);

                    callback.doSomething(response, "firstLoad");

                }
            }.execute(httpCall);
        }
        else
        {
            buttons.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forum_url= getArguments().getString("forum_url");
        //setRetainInstance(true);
    }

    public static forumfragment newInstance(Excersice ex) {

        forumfragment item = new forumfragment();

        Bundle args = new Bundle();
        args.putString("forum_url",ex.forum_url);

        item.setArguments(args);



        return item;
    }
    private void parse(String someResult)
    {
        Document doc = Jsoup.parse(someResult);
        Elements authors= doc.select("table[id] td[valign=\"middle\"]>b");
        Elements contents=doc.select("td[id]");
        Elements dates=doc.select("table[id] td[style]");
       kolvoStranic=doc.select("input[name=\"kolvostranic\"]").first().attr("value");
        currentPage= doc.select("input[name=\"textbox\"]").first().attr("value");
        Elements form1=doc.select("form[name=\"form1\"]+td");
        Elements input=form1.select("input");
        postParams=new  HashMap<String,String>();
        Element item;
       // ((Button)buttons.findViewById(R.id.select_page)).setText(currentPage+" из "+kolvoStranic);
        for (org.jsoup.nodes.Element element:input)
        {
            postParams.put(element.attr("name"), element.attr("value"));
        }
        ArrayList<Comment>  comments= new ArrayList<Comment>();

        for (int i = 0; i < contents.size(); i++) {
            comments.add(new Comment(authors.get(i).text(), dates.get(i).text(), contents.get(i).html()));
        }

        if (adapter==null) {

            adapter = new commentAdapter();
           commentList.setAdapter(adapter);
           adapter.setDataSource(comments);
        }
        else
        {

            adapter.setDataSource(comments);
            adapter.notifyDataSetChanged();

        }
        String [] spinner_elements =new String[Integer.parseInt(kolvoStranic)];
        for (int i=0; i<Integer.parseInt(kolvoStranic);i++)
        {
            spinner_elements[i]=String.valueOf(i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinner_elements);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        current_page_tv.setAdapter(adapter);

        current_page_tv.setSelection(Integer.parseInt(currentPage)-1);


         //current_page_tv.setText(""+currentPage+" из "+ kolvoStranic);
    }
    public void doSomething(String someResult,String tag)
    {
        if (tag=="firstLoad")
        {

            parse(someResult);

        }



    }
}


 class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

    boolean userSelect = false;
private forumFragmentDelegate delegate;
    public SpinnerInteractionListener (forumFragmentDelegate delegate)
    {
        this.delegate=delegate;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (userSelect) {
            // Your selection handling code here
            delegate.loadData(pos+1);
            userSelect = false;
        }
    }

     @Override
     public void onNothingSelected(AdapterView<?> parent) {

     }

 }

 interface forumFragmentDelegate
 {
     public void loadData( int page);
 }