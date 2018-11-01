package com.example.a12745.newsapplication;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import android.support.v7.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>
{
    public static String nowUrl;
    private List<News> mNewsList;
    private Context mContext;
    class ViewHolder extends RecyclerView.ViewHolder
    {
        View newsView;
        ImageView newsImage;
        TextView newsTitle,newsAuthorName,newsDate;
        public ViewHolder(View view)
        {
            super(view);
            newsView = view;
            newsImage = (ImageView) view.findViewById(R.id.news_image);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsDate = (TextView) view.findViewById(R.id.news_date);
            newsAuthorName = (TextView) view.findViewById(R.id.news_author_name);
        }

    }
    public NewsAdapter(List<News> newsList)
    {
        mNewsList = MainActivity.newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)//点击事件
    {
        if(mContext == null){mContext = parent.getContext();}
        final View view = LayoutInflater.from(mContext).inflate(R.layout.news_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                nowUrl = mNewsList.get(position).getUrl();//将最新的url设置为要打开的url
                loadUrl();
                //加载url
            }
        });
        MainActivity.recyclerView_news_type.requestFocus();
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        News news = MainActivity.newsList.get(position);
        holder.newsTitle.setText(news.getTitle());//加载新闻标题
        holder.newsAuthorName.setText(news.getAuthor_name());//加载作者
        holder.newsDate.setText(news.getDate());//加载时间
        Glide.with(mContext).load(news.getThumbnail_pic_s()).into(holder.newsImage);//加载首页图片;
    }

    @Override
    public int getItemCount()
    {
        return mNewsList.size();
    }

    public void loadUrl()
    {
        Intent intent = new Intent(mContext,LoadUrlActivity.class);
        mContext.startActivity(intent);//调用url
    }
}