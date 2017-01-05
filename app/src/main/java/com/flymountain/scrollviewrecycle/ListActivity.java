package com.flymountain.scrollviewrecycle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.circlrnum.flymountain.customrecycle.CustomRecycleView;

import java.util.ArrayList;


/**
 * 作者 yuanfei on 2016/10/18.
 * 邮箱 yuanfei221@126.com
 */

public class ListActivity extends AppCompatActivity implements CustomRecycleView.OnMutilRecyclerViewListener {
    CustomRecycleView recycleView;
    RecyclerAdapter adapter;
    ArrayList<String> list = new ArrayList<>();
    private int flag;
    View head , foot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);
        recycleView = (CustomRecycleView) findViewById(R.id.recycleView);
        recycleView.setOnMutilRecyclerViewListener(this);
        setRecycle();
    }

    @Override
    public void onRefresh() {
        Log.e("onRefresh","onRefresh"+flag);
        flag = 0;
        list.clear();
        new UpdateTextTask().execute();
    }

    @Override
    public void onLoadMore() {
        flag = flag +10;
        new UpdateTextTask().execute();
    }

    public void setRecycle() {
        head = LayoutInflater.from(this).inflate(R.layout.head_item, recycleView, false);
//        foot = LayoutInflater.from(this).inflate(R.layout.foot_item, recycleView, false);
        adapter = new RecyclerAdapter(this);
        recycleView.setAdapter(adapter);
        //必须在setAdapter之后
        recycleView.addHeadView(head);
//        recycleView.addFootView(foot);
//        recycleView.addFootView(foot);
        setDate(0);
    }

    public void setTime() {
        setDate(flag);
        recycleView.stopLoadingMore();
        recycleView.stopRefresh();
    }

    public void setDate(int index) {
        for (int i = index; i < 10 + index; i++) {
            list.add("ni" + i);
        }
        adapter.clearData();
        adapter.addData(list);
    }

    /**
     * 延时加载数据
     */
    class UpdateTextTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            setTime();
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }
}
