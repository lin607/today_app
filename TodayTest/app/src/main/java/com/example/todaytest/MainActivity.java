package com.example.todaytest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public volatile List<WebList> webNameList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>(); // 每一个tablayout的fragment列表,
    private String[] titles = {"Science", "Sport", "Business","Entertainment", "Digital"};  // tablayout的标题
    private DrawerLayout mDrawerLayout;  // 滑出旁边菜单窗口

//    public List<WebList> getWebNameList(){
//        return webNameList;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // 主活动启动后执行的函数体
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // 挂上主活动的布局
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // 获取toolbar，把之前的actionbar隐藏，操作在styles.xml中更改noactionbar

        toolbar.setTitle(""); // 清空原来标题内容，即项目名称TodayTest，使用toolbar中的textview控件显示标题
        setSupportActionBar(toolbar);  // 把toolbar代替原来的actionbar

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);  // 获取滑出布局的控件，在activity_main文件

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view); // 获取滑出布局的菜单，在activity_main文件中
        ActionBar actionBar = getSupportActionBar();  // 获取标题控件
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);  // 在标题左边设置一个可以点击的图标，像（三）一样的东西
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); // 显示点击的图标即(三)图标
        }
        navView.setCheckedItem(R.id.nav_call); // 将滑动菜单的nav_call一栏默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {  // 侧滑布局的菜单列表的点击事件
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers(); // 关闭侧滑菜单
                return true;
            }
        });
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
//                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        initView();  // viewpager2+fragment组合显示
    }



    public boolean onCreateOptionsMenu(Menu menu){  // 显示标题右边的三点
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;  // false表示不显示
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  // 设置标题控件toolbar的点击事件
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
//            case R.id.backup:
//                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.delete:
//                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked refresh", Toast.LENGTH_SHORT).show();
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
//                    }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            default:
        }
        return true;
    }

    private void initView(){
        ViewPager2 viewPager2 = findViewById(R.id.view_pager2);  // 获取viewpager2的控件实例
        TabLayout tabLayout = findViewById(R.id.tab_layout);  // 获取tablayout的控件实例

        fragmentList.add(WebListFragment.newInstance("Science"));  // 每个tablayout绑定的fragment？？？
        fragmentList.add(WebListFragment.newInstance("Sport"));
        fragmentList.add(WebListFragment.newInstance("Business"));
        fragmentList.add(WebListFragment.newInstance("Entertainment"));
        fragmentList.add(WebListFragment.newInstance("Digital"));
//        ((RecyclerView) viewPager2.getChildAt(0)).getLayoutManager().setItemPrefetchEnabled(false);
        viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);  //关闭预加载？？？
//        Objects.requireNonNull(((RecyclerView) viewPager2.getChildAt(0)).getLayoutManager()).setItemPrefetchEnabled(false);
//        viewPager2.setOffscreenPageLimit(fragmentList.size());  //关闭预加载？？？
        viewPager2.setAdapter(new FragmentStateAdapter(this) { // viewpager2的适配器，注意匿名类名称

//            private final Long fid0 = 0L;
//            private final Long fid1 = 1L;
//            private final Long fid2 = 2L;
//            private final Long fid3 = 3L;
//            private final Long fid4 = 4L;
//
//            private ArrayList<Long> ids = new ArrayList<>();
//            {
//                ids.add(0L);
//                ids.add(1L);
//                ids.add(2L);
//                ids.add(3L);
//                ids.add(4L);
//            }
//
//            private final HashSet<Long> createdIds = new HashSet<>();
//
//            @Override
//            public long getItemId(int position) {
//                return ids.get(position);
//            }
//
//            @Override
//            public boolean containsItem(long itemId) {
//                return createdIds.contains(itemId);
//            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
//                final Long id = ids.get(position);
//                createdIds.add(id);
//                if (id.equals(fid0)){
//                    return fragmentList.get(0);
//                }
//                if (id.equals(fid1)){
//                    return fragmentList.get(1);
//                }
//                if (id.equals(fid2)){
//                    return fragmentList.get(2);
//                }
//                if (id.equals(fid3)){
//                    return fragmentList.get(3);
//                }
//                if (id.equals(fid4)){
//                    return fragmentList.get(4);
//                }
                return fragmentList.get(position); // 为viewpager2的每一页(position)定位，加载fragment
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();  // viewpager2有多少页？
            }
        });
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, false, new TabLayoutMediator.TabConfigurationStrategy() { // 设置tablayout和viewpager2联动
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]); // 获取每个position,设置这个position的tablayout标题
                // 添加图片怎么办？？？background
            }
        });
        tabLayoutMediator.attach(); // 把tablayout和viewpager2关联起来
    }

}