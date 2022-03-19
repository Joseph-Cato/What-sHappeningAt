package com.example.whatshappeningat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "NewsRecycleriewAdapter";

    private ArrayList<String[]> news = new ArrayList<>();
    private Context context;

    public NewsRecyclerViewAdapter(Context context, ArrayList<String[]> news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder called");

        String[] article = news.get(position);

        holder.title.setText(article[0]);
        holder.info.setText(article[1]);

        holder.newsItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked on article: " + article[0]);

                ViewerActivity.openURL(article[2], view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, info;
        RelativeLayout newsItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsItemLayout = itemView.findViewById(R.id.news_item);
            title = itemView.findViewById(R.id.title);
            info = itemView.findViewById(R.id.info);
        }
    }
}
