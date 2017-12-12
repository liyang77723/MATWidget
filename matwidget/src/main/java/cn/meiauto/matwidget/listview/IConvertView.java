package cn.meiauto.matwidget.listview;

/**
 * 绑定数据到视图
 */
public interface IConvertView<T> {
    /**
     * 绑定数据到要显示的视图控件
     */
    void convertDataToView(BaseViewHolder holder, T t, int position);
}
