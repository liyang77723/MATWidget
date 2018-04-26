package cn.meiauto.matwidget.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.meiauto.matwidget.listview.BaseListAdapter;
import cn.meiauto.matwidget.listview.BaseViewHolder;
import cn.meiauto.matwidget.listview.IConvertView;

public class MainActivity extends AppCompatActivity {

    Class[] activities = {
            ZoomImageViewActivity.class,
            ScrollRefreshActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.lv_main);
        BaseListAdapter<Class> adapter = new BaseListAdapter<>(this,
                android.R.layout.simple_list_item_1, new IConvertView<Class>() {
            @Override
            public void convertDataToView(BaseViewHolder holder, Class activity, int position) {
                holder.setText(android.R.id.text1, activity.getSimpleName());
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, activities[position]));
            }
        });

        adapter.addAll(activities);
    }
}
