package com.example.dcght.mysqlexapp.ExFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dcght.mysqlexapp.Excersice;
import com.example.dcght.mysqlexapp.R;




public class codeFragment extends Fragment implements com.example.dcght.mysqlexapp.ExFragment.EditCodeDelegate{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String savedQuery;
    protected EditText codeView;
    private static final String ARG_SAVED_QUERY="savedQuery";



    // TODO: Rename and change types of parameters




    public codeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static codeFragment newInstance(Excersice ex) {

         codeFragment item = new codeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SAVED_QUERY, ex.savedQuery);


        item.setArguments(args);
        return item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            savedQuery = getArguments().getString(ARG_SAVED_QUERY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_code, container, false);
         codeView= v.findViewById(R.id.editCode);
        codeView.setText(savedQuery);

        return v;
    }


    @Override
    public String getCode() {
        return codeView.getText().toString();
    }
}




/*

package com.example.dcght.mysqlexapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dcght.mysqlexapp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class taskfragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private com.example.dcght.mysqlexapp.MainActivity.Excersice ex;
    Switch viewSwitch;
    WebView fullTaskPageVW;
    ScrollView scrollView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taskfragment,container,false);
        TextView tv =(TextView) view.findViewById(R.id.tvTab1);
        ImageView iv=(ImageView) view.findViewById(R.id.base_image) ;
        scrollView=view.findViewById(R.id.liteViewLayout);
        switch(ex.databaseNumber)
        {
            case ("1"):
                iv.setImageResource(R.drawable.computers);
                break;
            case ("2"):
                iv.setImageResource(R.drawable.income);
                break;
            case ("3"):
                iv.setImageResource(R.drawable.ships);
                break;
            case ("4"):
                iv.setImageResource(R.drawable.aero);
                break;
        }
        fullTaskPageVW=(WebView) view.findViewById(R.id.fullViewwV);
        fullTaskPageVW.loadData(ex.fullHTMLCode,"text/html; charset=UTF-8", null);
         viewSwitch = (Switch) view.findViewById(R.id.viewSwitch);

        viewSwitch.setOnCheckedChangeListener(this);

        tv.setText(ex.descbase+"\n"+ex.tasktext);

        return view;
    }


    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (fullTaskPageVW.getVisibility()==View.GONE) {
            fullTaskPageVW.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        }
        else {
            scrollView.setVisibility(View.VISIBLE);
            fullTaskPageVW.setVisibility(View.GONE);
        }
    }

    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         ex=(com.example.dcght.mysqlexapp.MainActivity.Excersice) getArguments().getSerializable("ex_object");
       // setRetainInstance(true);

    }
}

 */