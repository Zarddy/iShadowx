package club.zarddy.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDBRecyclerViewAdapter<Data, DB extends ViewDataBinding>
        extends RecyclerView.Adapter<BaseDBRecyclerViewHolder> {

    private int mItemId;
    protected int mVariableId;
    protected List<Data> mData;
    protected OnItemClickListener<Data> mItemClickListener;
    protected int mSelectedPosition = -1; // 选中的菜单项位置

    public BaseDBRecyclerViewAdapter(int itemId, int variableId) {
        this(null, itemId, variableId);
    }

    public BaseDBRecyclerViewAdapter(List<Data> data, int itemId, int variableId) {
        this.mData = data == null ? new ArrayList<Data>() : data;
        this.mItemId = itemId;
        this.mVariableId = variableId;
    }

    @NonNull
    @Override
    public BaseDBRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DB binding = DataBindingUtil.inflate(inflater, mItemId, parent, false);
        return new BaseDBRecyclerViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseDBRecyclerViewHolder holder, final int position) {
        final DB binding = DataBindingUtil.getBinding(holder.itemView);
        if (binding == null) {
            return;
        }

        final Data itemData = mData.get(position);
        binding.setVariable(mVariableId, itemData);
        onBindViewHolder(itemData, binding, position);
        // 立即绑定数据
        binding.executePendingBindings();

        // 设置点击事件
        if (mItemClickListener == null) {
            return;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(holder.itemView, itemData, position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return mItemClickListener.onItemLongClick(holder.itemView, itemData, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 绑定数据
     */
    protected void onBindViewHolder(Data data, DB binding, int position) {

    }

    /**
     * 设置新数据
     */
    public void setNewData(List<Data> list) {
        this.mSelectedPosition = -1;
        this.mData.clear();
        if (list != null && !list.isEmpty()) {
            this.mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加新数据
     */
    public void appendData(Data data) {
        if (data != null) {
            this.mData.add(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加新数据列表
     */
    public void appendDataList(List<Data> list) {
        if (list != null && !list.isEmpty()) {
            this.mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置Item 长按、点击事件
     * @param onItemClickListener 点击事件
     */
    public void setOnItemClickListener(OnItemClickListener<Data> onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    /**
     * 设置选中的菜单项位置
     * @param position 选中的菜单位置
     */
    public void setSelectedPosition(int position) {
        this.mSelectedPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 清除选中的菜单项
     */
    public void clearSelected() {
        this.mSelectedPosition = -1;
        notifyDataSetChanged();
    }
}
