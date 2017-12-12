package cn.meiauto.matwidget.listview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

@SuppressWarnings("ALL")
public class BaseRecycleViewHolder extends RecyclerView.ViewHolder {

    private BaseViewHolder mHolder;

    public BaseRecycleViewHolder(View itemView, IOnItemClickListener cl, IOnItemLongClickListener lcl) {
        super(itemView);
        mHolder = new BaseViewHolder(itemView);
        loadListener(cl, lcl);
    }

    public BaseViewHolder getHolder() {
        return mHolder;
    }

    private void loadListener(final IOnItemClickListener cl, final IOnItemLongClickListener lcl) {
        if (cl != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cl.onClick(getAdapterPosition());
                }
            });
        }
        if (lcl != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return lcl.onLongClick(getAdapterPosition());
                }
            });
        }
    }
}
