package cn.meiauto.matwidget.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * listview,gridview通用的adapter
 */
public class BaseListAdapter<T> extends BaseAdapter implements IHandleAdapterData<T> {

    protected Context context;
    protected List<T> datas;
    private int mLayoutId;
    private IConvertView<T> mIConvert;

    public BaseListAdapter(Context context, int layoutId, IConvertView<T> iConvertView) {
        this.context = context;
        datas = new ArrayList<>();
        mLayoutId = layoutId;
        mIConvert = iConvertView;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(mLayoutId, parent, false);
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        mIConvert.convertDataToView(holder, datas.get(position), position);
        return convertView;
    }

    @Override
    public void add(T bean) {
        if (null != datas && null != bean) {
            datas.add(bean);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addAll(List<T> beans) {
        if (null != datas && null != beans) {
            datas.addAll(beans);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addAll(T[] beans) {
        if (null != datas && null != beans) {
            Collections.addAll(datas, beans);
            notifyDataSetChanged();
        }
    }

    @Override
    public void insert(int position, T bean) {
        if (null != datas && null != bean) {
            datas.add(position, bean);
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(int position) {
        if (null != datas && !datas.isEmpty()) {
            datas.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(T bean) {
        if (null != datas && !datas.isEmpty()) {
            datas.remove(bean);
            notifyDataSetChanged();
        }
    }

    @Override
    public void clear() {
        if (null != datas && !datas.isEmpty()) {
            datas.clear();
            notifyDataSetChanged();
        }
    }

    public void addData(T bean) {
        if (null != datas && null != bean) {
            datas.add(bean);
        }
    }
}