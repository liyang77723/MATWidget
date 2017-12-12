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
    private Context mContext;
    private List<T> mBeans;
    private int mLayoutResId;
    private IConvertView<T> mIConvertView;

    public BaseRecycleAdapter(Context context, int layoutResId, IConvertView<T> iConvertView) {
        mContext = context;
        mBeans = new ArrayList<>();
        mLayoutResId = layoutResId;
        mIConvertView = iConvertView;
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    public T getItem(int postion) {
        return mBeans.get(postion);
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutResId, parent, false);
        return new BaseRecycleViewHolder(view, mClickListener, mLongClickListener);
    }

    @Override
    public void onBindViewHolder(final BaseRecycleViewHolder holder, int position) {
        mIConvertView.convertDataToView(holder.getHolder(), mBeans.get(position), position);
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
        return mBeans;
    }

    public void add(T bean) {
        if (null != mBeans && null != bean) {
            mBeans.add(bean);
            notifyItemChanged(mBeans.size() - 1);
        }
    }

    public void addAll(List<T> beans) {
        if (null != mBeans && null != beans) {
            mBeans.addAll(beans);
            int addSize = beans.size();
            notifyItemRangeChanged(mBeans.size() - addSize - 1, addSize);
        }
    }

    @Override
    public void addAll(T[] beans) {
        if (null != mBeans && null != beans) {
            Collections.addAll(mBeans, beans);
            int addSize = beans.length;
            notifyItemRangeChanged(mBeans.size() - addSize - 1, addSize);
        }
    }

    public void insert(int position, T bean) {
        if (null != mBeans && null != bean) {
            mBeans.add(position, bean);
            notifyItemInserted(position);
        }
    }

    public void remove(int position) {
        if (null != mBeans && !mBeans.isEmpty()) {
            mBeans.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void clear() {
        if (null != mBeans && !mBeans.isEmpty()) {
            mBeans.clear();
            notifyDataSetChanged();
        }
    }
}
