package cn.meiauto.matwidget.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cn.meiauto.matwidget.listview.BaseListAdapter;
import cn.meiauto.matwidget.listview.BaseViewHolder;
import cn.meiauto.matwidget.listview.IConvertView;

public class MainActivity extends AppCompatActivity {

    Content[] contents = {
            new Content("ZoomImageView", ZoomImageViewActivity.class)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.lv_main);
        BaseListAdapter<Content> adapter = new BaseListAdapter<>(this, android.R.layout.simple_list_item_1, new IConvertView<Content>() {
            @Override
            public void convertDataToView(BaseViewHolder holder, Content content, int position) {
                holder.setText(android.R.id.text1, content.name);
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, contents[position].clazz));
            }
        });

        adapter.addAll(contents);
    }

    class Content {
        String name;
        Class clazz;

        Content(String name, Class clazz) {
            this.name = name;
            this.clazz = clazz;
        }
    }
}
