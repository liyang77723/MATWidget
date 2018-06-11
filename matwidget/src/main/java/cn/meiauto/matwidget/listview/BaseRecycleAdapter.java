package cn.meiauto.matwidget.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RecycleView通用的adapter
 * 数据管理放在adapter里面处理，外部最好不要有数据操作
 *
 * @author LiYang
 */
public class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleViewHolder> implements IHandleAdapterData<T> {
    protected Context context;
    protected List<T> datas;
    private int mLayoutResId;
    private IConvertView<T> mIConvertView;

    public BaseRecycleAdapter(Context context, int layoutResId, IConvertView<T> iConvertView) {
        this.context = context;
        datas = new ArrayList<>();
        mLayoutResId = layoutResId;
        mIConvertView = iConvertView;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public T getItem(int postion) {
        return datas.get(postion);
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(mLayoutResId, parent, false);
        return new BaseRecycleViewHolder(view, mClickListener, mLongClickListener);
    }

    @Override
    public void onBindViewHolder(final BaseRecycleViewHolder holder, int position) {
        mIConvertView.convertDataToView(holder.getHolder(), datas.get(position), position);
    }

    private IOnItemClickListener mClickListener;
    private IOnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(IOnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setOnItemLongClickListener(IOnItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    public List<T> getBeans() {
        return datas;
    }

    @Override
    public void add(T bean) {
        if (null != datas && null != bean) {
            datas.add(bean);
            notifyItemChanged(datas.size() - 1);
        }
    }

    @Override
    public void addAll(List<T> beans) {
        if (null != datas && null != beans) {
            datas.addAll(beans);
            int addSize = beans.size();
            notifyItemRangeChanged(datas.size() - addSize - 1, addSize);
        }
    }

    @Override
    public void addAll(T[] beans) {
        if (null != datas && null != beans) {
            Collections.addAll(datas, beans);
            int addSize = beans.length;
            notifyItemRangeChanged(datas.size() - addSize - 1, addSize);
        }
    }

    @Override
    public void insert(int position, T bean) {
        if (null != datas && null != bean) {
            datas.add(position, bean);
            notifyItemInserted(position);
        }
    }

    @Override
    public void remove(int position) {
        if (null != datas && !datas.isEmpty()) {
            datas.remove(position);
            notifyItemRemoved(position);
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
