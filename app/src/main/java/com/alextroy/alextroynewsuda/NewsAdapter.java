package com.alextroy.alextroynewsuda;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private Context context;
    private ArrayList<News> news;

    public NewsAdapter(Context context, ArrayList<News> news) {
        this.context = context;
        this.news = news;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News currentNews = news.get(position);
        holder.titleTextView.setText(currentNews.getWebTitle());
        holder.sectionNameTextView.setText(currentNews.getWebSectionName());
        holder.authorTextView.setText(currentNews.getWebAuthor());
        holder.dateTextView.setText(currentNews.getWebPublicationDate());

        holder.cardView.setOnClickListener(v -> {
            Uri newsUri = Uri.parse(currentNews.getWebUrl());
            Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);
            context.startActivity(newsIntent);
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView sectionNameTextView;
        public TextView authorTextView;
        public TextView dateTextView;
        public CardView cardView;

        public NewsHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view);
            titleTextView = v.findViewById(R.id.web_title);
            sectionNameTextView = v.findViewById(R.id.web_section_name);
            authorTextView = v.findViewById(R.id.web_author);
            dateTextView = v.findViewById(R.id.web_date);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
