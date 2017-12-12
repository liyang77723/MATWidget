package cn.meiauto.matwidget.listview;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用ViewHolder，显示数据到相应的视图上
 */
@SuppressWarnings("ALL")
public class BaseViewHolder {

    private SparseArray<View> mViews;

    private View mConvertView;

    public BaseViewHolder(View convertView) {
        mConvertView = convertView;
        mViews = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public BaseViewHolder setText(int viewId, int textId) {
        TextView tv = getView(viewId);
        tv.setText(textId);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setCheckBoxChecked(int viewId, boolean checked) {
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(checked);
        return this;
    }

    public View getConvertView() {
        return mConvertView;
    }
}