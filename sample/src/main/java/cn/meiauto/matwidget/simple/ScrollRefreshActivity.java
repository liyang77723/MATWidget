package cn.meiauto.matwidget.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.meiauto.matwidget.refresh.smart.SmartRefreshLayout;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/4/26
 */
public class ScrollRefreshActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scroll_refresh);

        final SmartRefreshLayout refreshLayout = findViewById(R.id.refresh_view);
        refreshLayout.setOnRefreshListener(new SmartRefreshLayout.onRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.stopRefresh();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.stopLoadMore();
                    }
                }, 1000);
            }
        });
    }
}
