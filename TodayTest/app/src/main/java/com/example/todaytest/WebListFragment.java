package com.example.todaytest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebListFragment extends Fragment {
    private Object lock = new Object();
    private static String val;
    private static final String KEY = "key";
    public volatile List<WebList> webNameList = new ArrayList<>();  // recyclerview的数据列表，volatile关键字，提示线程修改？
    public WebListFragment(){
    } //初始化方法

    public static WebListFragment newInstance(String ty){// 静态初始化方法可以通过类名调用，可以设置参数传递数据，然后后面判断数据，初始化recyclerview的列表数据
        val = ty;
        WebListFragment fragment = new WebListFragment(); // 实例化，调用oncreateview方法
//        Bundle ag = new Bundle();
//        ag.putString(KEY, ty); // 设置数据
//        fragment.setArguments(ag); // 传递数据，这种方法不会在重新启动活动后造成数据丢失，https://blog.csdn.net/tu_bingbing/article/details/24143249
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,  //初始化这个fragment的布局，可能会多次调用，效率慢，可设置缓存界面，本项目无缓存，https://blog.csdn.net/wukong1981/article/details/42198217?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_weblist, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view); // 获取控件
//        init_view_data();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
////                    if (val.equals("Sport")) {
//                    Document doc = Jsoup.connect("https://www.toutiao.com/api/pc/realtime_news/").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36").ignoreContentType(true).get();
//                    Element data = doc.body();
//                    String data_js = data.text();
//
//                    JSONObject jobj = new JSONObject(data_js);
//                    JSONArray jsonArray = jobj.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String title =jsonObject.getString("title");
//                        String url = "https://www.toutiao.com" + jsonObject.getString("open_url");
//
//                        byte[] bytes;
//                        String img_url = "http:" + jsonObject.getString("image_url");
//                        URL url1 = new URL(img_url);
//                        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
//                        connection.setConnectTimeout(5000);
//                        connection.setRequestMethod("GET");
//                        int code = connection.getResponseCode();
//                        if (code == 200) {
//                            InputStream is = connection.getInputStream();
//                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                            byte[] buffer = new byte[1024];
//                            int len;
//                            while ((len = is.read(buffer)) != -1) {
//                                byteArrayOutputStream.write(buffer, 0, len);
//                            }
//                            bytes = byteArrayOutputStream.toByteArray();
//                            WebList webList = new WebList(title, url, bytes);
//                            webNameList.add(webList);
//                        }
//                    }
////                    }
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        try {
//            lock.wait();
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        webNameList = activity.webNameList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext()); // 默认创建一个竖直方向上的manager，RecyclerView中子Item的布局管理器，有三种构造方法，https://blog.csdn.net/ww897532167/article/details/85952498?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param#1.1%C2%A0LinearLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        WebAdapter adapter = new WebAdapter(webNameList, view.getContext());  // 这里的getContext方法会不会空，https://blog.csdn.net/weixin_40060411/article/details/80446498
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void init_view_data(){
     new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    if (val.equals("Sport")) {
                    Document doc = Jsoup.connect("https://www.toutiao.com/api/pc/realtime_news/").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36").ignoreContentType(true).get();
                    Element data = doc.body();
                    String data_js = data.text();

                    JSONObject jobj = new JSONObject(data_js);
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title =jsonObject.getString("title");
                        String url = "https://www.toutiao.com" + jsonObject.getString("open_url");

                        byte[] bytes;
                        String img_url = "http:" + jsonObject.getString("image_url");
                        URL url1 = new URL(img_url);
                        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setRequestMethod("GET");
                        int code = connection.getResponseCode();
                        if (code == 200) {
                            InputStream is = connection.getInputStream();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = is.read(buffer)) != -1) {
                                byteArrayOutputStream.write(buffer, 0, len);
                            }
                            bytes = byteArrayOutputStream.toByteArray();
                            WebList webList = new WebList(title, url, bytes);
                            webNameList.add(webList);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    lock.notify();
                }
            }
        }).start();
    }

//    private void initView(View view) throws IOException {
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view); // 获取控件
//        initFruits(); // 装载数据到列表中，此时函数可以利用传入的value选择装哪些数据
//        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext()); // 默认创建一个竖直方向上的manager，RecyclerView中子Item的布局管理器，有三种构造方法，https://blog.csdn.net/ww897532167/article/details/85952498?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param#1.1%C2%A0LinearLayoutManager
//        recyclerView.setLayoutManager(layoutManager);
//        WebAdapter adapter = new WebAdapter(webNameList, view.getContext());  // 这里的getContext方法会不会空，https://blog.csdn.net/weixin_40060411/article/details/80446498
//        recyclerView.setAdapter(adapter);
//    }

//    private void initFruits() throws IOException {  // 利用传入的value判断装哪些数据
//            if (val.equals("Entertainment")){
//                Document doc = Jsoup.connect("https://www.toutiao.com/ch/news_entertainment/").get();
//                Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
////                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                Elements timeLinks = doc.select("div.footer-bar");
//                for(int j = 0;j < titleLinks.size();j++){
//                    String title = titleLinks.get(j).select("a").text();
//                    String uri = titleLinks.get(j).select("a").attr("href");
////                    String desc = descLinks.get(j).select("span").text();
//                    String time = timeLinks.get(j).select("span.footer-bar-action time").text();
////                    News news = new News(title,uri,desc,time);
////                    newsList.add(news);
//                    WebList webList = new WebList(title, uri, time);
//                }
//            }
//            else if (val.equals("Sport")){
//                Document doc = Jsoup.connect("https://www.toutiao.com/ch/news_sports/").get();
//                Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
//    //                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                Elements timeLinks = doc.select("div.footer-bar");
//                for(int j = 0;j < titleLinks.size();j++){
//                    String title = titleLinks.get(j).select("a").text();
//                    String uri = titleLinks.get(j).select("a").attr("href");
//    //                    String desc = descLinks.get(j).select("span").text();
//                    String time = timeLinks.get(j).select("span.footer-bar-action time").text();
//    //                    News news = new News(title,uri,desc,time);
//    //                    newsList.add(news);
//                    WebList webList = new WebList(title, uri, time);
//                }
//            }
//            else if (val.equals("Science")){
//                Document doc = Jsoup.connect("https://www.toutiao.com/ch/news_tech/").get();
//                Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
//                //                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                Elements timeLinks = doc.select("div.footer-bar");
//                for(int j = 0;j < titleLinks.size();j++){
//                    String title = titleLinks.get(j).select("a").text();
//                    String uri = titleLinks.get(j).select("a").attr("href");
//                    //                    String desc = descLinks.get(j).select("span").text();
//                    String time = timeLinks.get(j).select("span.footer-bar-action time").text();
//                    //                    News news = new News(title,uri,desc,time);
//                    //                    newsList.add(news);
//                    WebList webList = new WebList(title, uri, time);
//                }
//            }
//            else if (val.equals("Business")){
//                Document doc = Jsoup.connect("https://www.toutiao.com/ch/news_finance/").get();
//                Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
//                //                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                Elements timeLinks = doc.select("div.footer-bar");
//                for(int j = 0;j < titleLinks.size();j++){
//                    String title = titleLinks.get(j).select("a").text();
//                    String uri = titleLinks.get(j).select("a").attr("href");
//                    //                    String desc = descLinks.get(j).select("span").text();
//                    String time = timeLinks.get(j).select("span.footer-bar-action time").text();
//                    //                    News news = new News(title,uri,desc,time);
//                    //                    newsList.add(news);
//                    WebList webList = new WebList(title, uri, time);
//                }
//            }
//            else if (val.equals("Digital")){
//                Document doc = Jsoup.connect("https://www.toutiao.com/ch/digital/").get();
//                Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
//                //                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                Elements timeLinks = doc.select("div.footer-bar");
//                for(int j = 0;j < titleLinks.size();j++){
//                    String title = titleLinks.get(j).select("a").text();
//                    String uri = titleLinks.get(j).select("a").attr("href");
//                    //                    String desc = descLinks.get(j).select("span").text();
//                    String time = timeLinks.get(j).select("span.footer-bar-action time").text();
//                    //                    News news = new News(title,uri,desc,time);
//                    //                    newsList.add(news);
//                    WebList webList = new WebList(title, uri, time);
//                }
//            }
////            WebList apple = new WebList("https://baidu.com", R.drawable.apple_pic);
////            webNameList.add(apple);
////            WebList banana = new WebList("Banana", R.drawable.banana_pic);
////            webNameList.add(banana);
////            WebList orange = new WebList("Orange", R.drawable.orange_pic);
////            webNameList.add(orange);
////            WebList watermelon = new WebList("Watermelon", R.drawable.watermelon_pic);
////            webNameList.add(watermelon);
////            WebList pear = new WebList("Pear", R.drawable.pear_pic);
////            webNameList.add(pear);
////            WebList grape = new WebList("Grape", R.drawable.grape_pic);
////            webNameList.add(grape);
////            WebList pineapple = new WebList("Pineapple", R.drawable.pineapple_pic);
////            webNameList.add(pineapple);
////            WebList strawberry = new WebList("Strawberry", R.drawable.strawberry_pic);
////            webNameList.add(strawberry);
////            WebList cherry = new WebList("Cherry", R.drawable.cherry_pic);
////            webNameList.add(cherry);
////            WebList mango = new WebList("Mango", R.drawable.mango_pic);
////            webNameList.add(mango);
//    }
    class WebAdapter extends RecyclerView.Adapter<WebAdapter.ViewHolder> {
        private List<WebList> mWebListList;  // 布局
        private Context context;  // 上下文
        class ViewHolder extends RecyclerView.ViewHolder{
            View webView;
            ImageView webImage;
            TextView webTitle;
            public ViewHolder(View view){  // 缓存数据
                super(view);
                webView = view;
                webImage = (ImageView) view.findViewById(R.id.web_image);
                webTitle = (TextView) view.findViewById(R.id.web_title);
            }
        }
        public WebAdapter(List<WebList> webListList, Context context){  // 初始化方法
            this.context = context;
            mWebListList = webListList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weblist_item, parent, false);
            final WebAdapter.ViewHolder holder = new ViewHolder(view);
            holder.webView.setOnClickListener(new View.OnClickListener() {  // 根据点击位置，设置点击事件
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    WebList webList = mWebListList.get(position);
                    Intent intent = new Intent(context, WebContentActivity.class);
                    intent.putExtra("webname", webList.getUrl());
                    startActivity(intent);
//                    WebContentActivity.actionStart(getActivity(), webList.getName());
                }
            });
            holder.webImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    WebList webList = mWebListList.get(position);
                    Toast.makeText(v.getContext(), "you clicked time",Toast.LENGTH_SHORT).show();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(WebAdapter.ViewHolder holder, int position) { // 把列表数据设置在布局里
            WebList webList = mWebListList.get(position);
            Bitmap bitmap = BitmapFactory.decodeByteArray(webList.getBit_image(), 0, webList.getBit_image().length);
            holder.webImage.setImageBitmap(bitmap);
            holder.webTitle.setText(webList.getNewstitle());
        }

        @Override
        public int getItemCount() {
            return mWebListList.size();
        }
    }
}

//else if (val.equals("Entertainment")) {
//                        Document doc = Jsoup.connect("https://www.toutiao.com/ch/news_sports/").get();
//                        Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
//                        //                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                        Elements timeLinks = doc.select("div.footer-bar");
//                        for (int j = 0; j < titleLinks.size(); j++) {
//                            String title = titleLinks.get(j).select("a").text();
//                            String uri = titleLinks.get(j).select("a").attr("href");
//                            //                    String desc = descLinks.get(j).select("span").text();
//                            String time = timeLinks.get(j).select("span.footer-bar-action.time").text();
//                            //                    News news = new News(title,uri,desc,time);
//                            //                    newsList.add(news);
//                            WebList webList = new WebList(title, uri, time);
//                            webNameList.add(webList);
//                        }
//                    } else if (val.equals("Science")) {
//                        Document doc = Jsoup.connect("https://www.toutiao.com/ch/news_tech/").get();
//                        Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
//                        //                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                        Elements timeLinks = doc.select("div.footer-bar");
//                        for (int j = 0; j < titleLinks.size(); j++) {
//                            String title = titleLinks.get(j).select("a").text();
//                            String uri = titleLinks.get(j).select("a").attr("href");
//                            //                    String desc = descLinks.get(j).select("span").text();
//                            String time = timeLinks.get(j).select("span.footer-bar-action time").text();
//                            //                    News news = new News(title,uri,desc,time);
//                            //                    newsList.add(news);
//                            WebList webList = new WebList(title, uri, time);
//                            webNameList.add(webList);
//                        }
//                    } else if (val.equals("Business")) {
//                        Document doc = Jsoup.connect("https://www.toutiao.com/ch/news_finance/").get();
//                        Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
//                        //                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                        Elements timeLinks = doc.select("div.footer-bar");
//                        for (int j = 0; j < titleLinks.size(); j++) {
//                            String title = titleLinks.get(j).select("a").text();
//                            String uri = titleLinks.get(j).select("a").attr("href");
//                            //                    String desc = descLinks.get(j).select("span").text();
//                            String time = timeLinks.get(j).select("span.footer-bar-action time").text();
//                            //                    News news = new News(title,uri,desc,time);
//                            //                    newsList.add(news);
//                            WebList webList = new WebList(title, uri, time);
//                            webNameList.add(webList);
//                        }
//                    } else if (val.equals("Digital")) {
//                        Document doc = Jsoup.connect("https://www.toutiao.com/ch/digital/").get();
//                        Elements titleLinks = doc.select("div.title-box");    //解析来获取每条新闻的标题与链接地址
//                        //                Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
//                        Elements timeLinks = doc.select("div.footer-bar");
//                        for (int j = 0; j < titleLinks.size(); j++) {
//                            String title = titleLinks.get(j).select("a").text();
//                            String uri = titleLinks.get(j).select("a").attr("href");
//                            //                    String desc = descLinks.get(j).select("span").text();
//                            String time = timeLinks.get(j).select("span.footer-bar-action time").text();
//                            //                    News news = new News(title,uri,desc,time);
//                            //                    newsList.add(news);
//                            WebList webList = new WebList(title, uri, time);
//                            webNameList.add(webList);
//                        }
//                    }