package com.example.a12745.newsapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import org.json.*;
import java.util.*;
import okhttp3.*;

public class  MainActivity extends AppCompatActivity {
    public static String type = "top";
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static List<News> newsList = new ArrayList<>();
    public static RecyclerView recyclerView,recyclerView_news_type;
    public static NewsTypeAdapter typeAdapter;
    List<NewsType> newsTypeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isConnectIsNomarl();//检查网络连接
        //初始化类型列表
        initNewsType();
        recyclerView_news_type = (RecyclerView) findViewById(R.id.news_type);
        LinearLayoutManager linearLayoutManager_type = new LinearLayoutManager(this);
        linearLayoutManager_type.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_news_type.setLayoutManager(linearLayoutManager_type);
        typeAdapter = new NewsTypeAdapter(newsTypeList);
        recyclerView_news_type.setAdapter(typeAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));//分割线
        sendRequestWithOkHttp();//发送请求
        final NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isConnectIsNomarl();//检查网络连接
                sendRequestWithOkHttp();//发送请求
                final NewsAdapter adapter = new NewsAdapter(newsList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public static void sendRequestWithOkHttp()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://v.juhe.cn/toutiao/index?type="+MainActivity.type+"&key=bf2e5ade512368affaf20a4167a4c857").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);//解析数据
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void parseJSONWithGSON(String jsonData)
    {
        MainActivity.newsList.clear();
        try{
            JSONObject jsonObject1 = new JSONObject(jsonData);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("result");
            JSONArray jsonArray = jsonObject2.getJSONArray("data");
            for (int i=0;i < jsonArray.length();i++)
            {
                News n = new News();
                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                n.setTitle(jsonObject.getString("title"));
                n.setUrl(jsonObject.getString("url"));
                n.setThumbnail_pic_s(jsonObject.getString("thumbnail_pic_s"));
                n.setAuthor_name(jsonObject.getString("author_name"));
                n.setDate(jsonObject.getString("date"));
                MainActivity.newsList.add(n);
            }
        }
        catch (Exception e)
        {e.printStackTrace(); }
    }

    private void initNewsType()
    {
        NewsType top = new NewsType("头条","top");
        newsTypeList.add(top);
        NewsType shehui = new NewsType("社会","shehui");
        newsTypeList.add(shehui);
        NewsType guonei = new NewsType("国内","guonei");
        newsTypeList.add(guonei);
        NewsType guoji = new NewsType("国际","guoji");
        newsTypeList.add(guoji);
        NewsType yule = new NewsType("娱乐","yule");
        newsTypeList.add(yule);
        NewsType tiyu = new NewsType("体育","tiyu");
        newsTypeList.add(tiyu);
        NewsType junshi = new NewsType("军事","junshi");
        newsTypeList.add(junshi);
        NewsType keji = new NewsType("科技","keji");
        newsTypeList.add(keji);
        NewsType caijing = new NewsType("财经","caijing");
        newsTypeList.add(caijing);
        NewsType shishang = new NewsType("时尚","shishang");
        newsTypeList.add(shishang);
    }

    private void isConnectIsNomarl()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
        } else {
            Toast.makeText(MainActivity.this, "网络连接失败！", Toast.LENGTH_LONG).show();
        }
    }

}
