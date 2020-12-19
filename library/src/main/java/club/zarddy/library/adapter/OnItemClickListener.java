package club.zarddy.library.adapter;

import android.view.View;

/**
 * RecyclerView Item 长按、点击事件
 * @param <Data>
 */
public interface OnItemClickListener<Data> {

    /**
     * item 点击事件
     * @param data item数据
     * @param position 位置
     */
    void onItemClick(View view, Data data, int position);

    /**
     * item 长按事件
     * @param data item数据
     * @param position 位置
     * @return
     */
    boolean onItemLongClick(View view, Data data, int position);
}
