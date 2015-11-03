package com.example.chenyu.compareextend;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private PullToRefreshListView  lv;
    private String[] mListTitle={"姓名: ","性别: ","年龄: ","居住地: ","邮箱: "};
    private String[] mListStr={"chenyu","男","25","北京","2657607916@qq.com"};
    private ListView mlistView=null;
    private int i=0;
    private SimpleAdapter adapter;
    private Button button;
    List<Map<String,Object>> mData=new ArrayList<Map<String,Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bylistview);
        lv= (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
        mData=getmData();
        adapter=new SimpleAdapter(this,mData,R.layout.simple_list_item_2,new String[]{"title","text"},new int[]{R.id.text1,R.id.text2});
        lv.setAdapter(adapter);
        initIndicator();
        //刷新监听事件
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>(){
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // 模拟加载任务
                new GetDataTask().execute();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String label=simpleDateFormat.format(System.currentTimeMillis());
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
            }
        });
        //下拉刷新 上拉加载
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String label = simpleDateFormat.format(System.currentTimeMillis());
                Toast.makeText(MainActivity.this, "PullDownRefresh", Toast.LENGTH_SHORT).show();
                new GetDataTask().execute();

                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String label = simpleDateFormat.format(System.currentTimeMillis());
                Toast.makeText(MainActivity.this, "PullUpToRefresh", Toast.LENGTH_SHORT).show();
                new GetDataTask().execute();
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
                //下拉功能演示完之后，这个地方是跳到GridView界面去
//                Intent intent = new Intent(MainActivity.this, GridActivity.class);
//                startActivity(intent);
            }
        });
    }
    //初始化那个下拉和上拉要显示的文字
    private void initIndicator()
    {
        ILoadingLayout startLabels = lv
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("就把我往死里面拉吧...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放了我，我就刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = lv.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("就把我往死里面拉吧123...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新123...");// 刷新时
        endLabels.setReleaseLabel("放了我，我就刷新123...");// 下来达到一定距离时，显示的提示
    }
    public List<Map<String,Object>> getmData(){
        for(int i=0;i<mListTitle.length;i++){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("title",mListTitle[i]);
            map.put("text",mListStr[i]);
            mData.add(map);
        }
        return mData;
    }
    private class GetDataTask extends AsyncTask<Void, Void, Map<String,Object>>
    {
        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            Map<String,Object> map=new HashMap<String,Object>();
            //如果这个地方不使用线程休息的话，刷新就不会显示在那个PullToRefreshListView的UpdatedLabel上面
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put("title","title"+(i++)+":--->");
            map.put("text", "text" + (i++));
            //加入数据，这里是可以的
//          mData.add(map);
            return map;
        }
        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            //            super.onPostExecute(stringObjectMap);
            mData.add(stringObjectMap);
            //更新数据
            adapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed. 如果没有下面的函数那么刷新将不会停
            lv.onRefreshComplete();

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
