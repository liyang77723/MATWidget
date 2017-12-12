package cn.meiauto.matwidget.listview;

import java.util.List;

/**
 * adapter数据处理
 */
public interface IHandleAdapterData<T> {
    void add(T bean);

    void addAll(List<T> beans);

    void addAll(T[] beans);

    void insert(int pos, T bean);

    void remove(int pos);

    void clear();
}
