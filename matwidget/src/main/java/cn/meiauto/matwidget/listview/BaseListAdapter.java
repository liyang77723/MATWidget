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

    private Context mContext;
    private List<T> mBeans;
    private int mLayoutId;
    private IConvertView<T> mIConvert;

    public BaseListAdapter(Context context, int layoutId, IConvertView<T> iConvertView) {
        mContext = context;
        mBeans = new ArrayList<>();
        mLayoutId = layoutId;
        mIConvert = iConvertView;
    }

    @Override
    public int getCount() {
        return mBeans.size();
    }

    @Override
    public T getItem(int i) {
        return mBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        mIConvert.convertDataToView(holder, mBeans.get(position), position);
        return convertView;
    }

    @Override
    public void add(T bean) {
        if (null != mBeans && null != bean) {
            mBeans.add(bean);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addAll(List<T> beans) {
        if (null != mBeans && null != beans) {
            mBeans.addAll(beans);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addAll(T[] beans) {
        if (null != mBeans && null != beans) {
            Collections.addAll(mBeans, beans);
            notifyDataSetChanged();
        }
    }

    @Override
    public void insert(int position, T bean) {
        if (null != mBeans && null != bean) {
            mBeans.add(position, bean);
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(int position) {
        if (null != mBeans && !mBeans.isEmpty()) {
            mBeans.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(T bean) {
        if (null != mBeans && !mBeans.isEmpty()) {
            mBeans.remove(bean);
            notifyDataSetChanged();
        }
    }

    @Override
    public void clear() {
        if (null != mBeans && !mBeans.isEmpty()) {
            mBeans.clear();
            notifyDataSetChanged();
        }
    }

    public void addData(T bean) {
        if (null != mBeans && null != bean) {
            mBeans.add(bean);
        }
    }
}