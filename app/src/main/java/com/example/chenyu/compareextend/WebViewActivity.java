package com.example.chenyu.compareextend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebViewActivity extends AppCompatActivity {
    private PullToRefreshWebView pullToRefreshWebView;
    private WebView webView;
    List<Map<String,Object>> mData=new ArrayList<Map<String,Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        pullToRefreshWebView= (PullToRefreshWebView) findViewById(R.id.pull_refresh_webview);
        webView= pullToRefreshWebView.getRefreshableView();
        webView.loadUrl("http://www.baidu.com");
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                      webView.loadUrl(url);
                return false;
            }
        });
//        lv= (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
//        mData=getmData();
//        adapter=new SimpleAdapter(this,mData,R.layout.simple_list_item_2,new String[]{"title","text"},new int[]{R.id.text1,R.id.text2});
//        lv.setAdapter(adapter);
        initIndicator();
       // 刷新监听事件
        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                // 模拟加载任务
                new GetDataTask().execute();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String label = simpleDateFormat.format(System.currentTimeMillis());
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
            }
        });
//      //  下拉刷新 上拉加载
//        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String label = simpleDateFormat.format(System.currentTimeMillis());
//                Toast.makeText(WebViewActivity.this, "PullDownRefresh", Toast.LENGTH_SHORT).show();
//                new GetDataTask().execute();
//
//                // 显示最后更新的时间
//                refreshView.getLoadingLayoutProxy()
//                        .setLastUpdatedLabel(label);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String label = simpleDateFormat.format(System.currentTimeMillis());
//                Toast.makeText(WebViewActivity.this, "PullUpToRefresh", Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(MainActivity.this, GridActivity.class);
////                startActivity(intent);
////                new GetDataTask().execute();
////                // 显示最后更新的时间
////                refreshView.getLoadingLayoutProxy()
////                        .setLastUpdatedLabel(label);
//            }
//
//        });
    }
    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }
    private void initIndicator()
    {
        ILoadingLayout startLabels = pullToRefreshWebView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("就把我往死里面拉吧...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放了我，我就刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pullToRefreshWebView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("就把我往死里面拉吧123...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新123...");// 刷新时
        endLabels.setReleaseLabel("放了我，我就刷新123...");// 下来达到一定距离时，显示的提示
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            Map<String,Object> map=new HashMap<String,Object>();
            //如果这个地方不使用线程休息的话，刷新就不会显示在那个PullToRefreshListView的UpdatedLabel上面
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            //加载需要显示的网页
            webView.loadUrl("http://www.baidu.com");
            pullToRefreshWebView.onRefreshComplete();
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
