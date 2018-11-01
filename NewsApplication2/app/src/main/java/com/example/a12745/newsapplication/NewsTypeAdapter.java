package com.example.a12745.newsapplication;

import android.content.Context;
import android.view.*;
import android.widget.*;
import android.support.v7.widget.RecyclerView;
import java.util.List;


public class NewsTypeAdapter extends RecyclerView.Adapter<NewsTypeAdapter.ViewHolder>
{
    private List<NewsType> newsTypeList;
    private Context mContext;
    private int position;
    private int parentHeight;
    private int parentWidth;
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View newsTypeView;
        TextView newsTypeName;
        public ViewHolder(View view)
        {
            super(view);
            newsTypeView = view;
            newsTypeName = (TextView) view.findViewById(R.id.news_type_name);
        }
    }
    public NewsTypeAdapter (List<NewsType> newsTypeList)
    {
        this.newsTypeList = newsTypeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(mContext == null){ mContext = parent.getContext();}
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_type_item,parent,false);
        parentHeight = parent.getHeight();
        parentWidth = parent.getWidth();
        final ViewHolder holder = new ViewHolder(view);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = parentWidth/6;//界面显示6个类型
        holder.newsTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                MainActivity.type=newsTypeList.get(position).getType();
                MainActivity.sendRequestWithOkHttp();//发送请求
                final NewsAdapter adapter = new NewsAdapter(MainActivity.newsList);
                MainActivity.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                MainActivity.recyclerView_news_type.requestFocus();
            }
        });//更改显示的新闻类型
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {
        NewsType newsType =newsTypeList.get(position);
        holder.newsTypeName.setText(newsType.getName());
    }

    @Override
    public int getItemCount()
    {
        return newsTypeList.size();
    }
}
