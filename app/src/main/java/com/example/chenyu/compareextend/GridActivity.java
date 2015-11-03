package com.example.chenyu.compareextend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GridActivity extends AppCompatActivity {
    private LinkedList<String> mListItems;
    private PullToRefreshGridView mPullRefreshListView;
    private ArrayAdapter<String> mAdapter;
    private int mItemCount = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bygridview);
        // 得到控件
        mPullRefreshListView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
        // 初始化数据和数据源
        initDatas();
        mAdapter = new ArrayAdapter<String>(this, R.layout.griditem,
                R.id.id_grid_item_text, mListItems);
        mPullRefreshListView.setAdapter(mAdapter);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<GridView> refreshView)
                    {
                       // 显示最后更新的时间
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String label=simpleDateFormat.format(System.currentTimeMillis());
                         refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
                        new GetDataTask().execute();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<GridView> refreshView)
                    {
//                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String label=simpleDateFormat.format(System.currentTimeMillis());
//                        refreshView.getLoadingLayoutProxy()
//                                .setLastUpdatedLabel(label);
//                        new GetDataTask().execute();
                        //下拉功能演示完之后，这个地方是跳到GridView界面去
                        Intent intent = new Intent(GridActivity.this, WebViewActivity.class);
                        startActivity(intent);
                    }
                });
    }
    private void initDatas()
    {
        mListItems = new LinkedList<String>();

        for (int i = 0; i < mItemCount; i++)
        {
            mListItems.add(i + "");
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                Thread.sleep(3000);
            } catch (InterruptedException e)
            {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mListItems.add("" + mItemCount++);
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
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
