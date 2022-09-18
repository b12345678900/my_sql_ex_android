package com.example.dcght.mysqlexapp.ExFragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dcght.mysqlexapp.Comment;
import com.example.dcght.mysqlexapp.R;

import java.util.ArrayList;

public class commentAdapter extends  RecyclerView.Adapter<commentAdapter.commentViewHolder> {
    private Integer numbersItems;
    private ArrayList<Comment> comments;

    public void setDataSource(ArrayList<Comment> list)
    {
        comments=list;
    }
    @NonNull
    @Override

    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context= viewGroup.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.comment_list_item,viewGroup,false);
        commentViewHolder CVH= new commentViewHolder(view);

        return CVH;
    }

    @Override
    public void onBindViewHolder(@NonNull commentViewHolder commentViewHolder, int i) {
        commentViewHolder.bind(comments.get(i));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class commentViewHolder extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView author;
        WebView content;

        public commentViewHolder(@NonNull View itemView) {
            super(itemView);
            date= itemView.findViewById(R.id.commentDate);
            author= itemView.findViewById(R.id.commentAuthor);
            content= (WebView)itemView.findViewById(R.id.commentContent);
            WebSettings webSettings = content.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowContentAccess(true);

        }
        void bind(Comment comment)
        {
            date.setText(comment.author);
            author.setText(comment.date);
            String htmldata="<!DOCTYPE html>\n<html>\n"+"<head>\n"+
        "<meta name='viewport' content='initial-scale=1'>\n"+

       " <link rel='stylesheet' type='text/css' href='comment.css'>\n"+
        "<script src='highlight.js'></script>\n"+
        "<script>\n"+
                "hljs.initHighlightingOnLoad('sql');\n"+
        "</script>\n"+

        "</head>\n"+"<body>\n"+comment.content+"\n</body>\n"+
                   "<script>\n"+
                    "var e = new MouseEvent('click', {"+
                "view: window, bubbles: true, cancelable: true"+
        "});\n"+
           " var a =document.querySelectorAll('a[href=\"###\"]');\n"+
           " for (i = 0; i < a.length; ++i) {\n"+
                "a[i].dispatchEvent(e);"+
            "}"+ "\nvar b =document.querySelectorAll('pre code');\n"+
            "for (i = 0; i < b.length; ++i) {\n"+
                "hljs.highlightBlock(b[i]);\n"+
            "}\n"+
        "</script>\n"+"</html>";
            Log.d("bbb", htmldata);


            //content.loadData(htmldata,"text/html","UTF-8");
            content.loadDataWithBaseURL("file:///android_asset/", htmldata, "text/html", "UTF-8", null);
        }
    }
}
