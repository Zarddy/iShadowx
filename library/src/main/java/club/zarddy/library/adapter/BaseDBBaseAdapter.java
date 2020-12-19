package club.zarddy.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 不可用
 * @param <Data>
 * @param <DB>
 */
public abstract class BaseDBBaseAdapter<Data, DB extends ViewDataBinding> extends BaseAdapter {

    private Context mContext;
    private List<Data> mData;
    private int mLayoutId;
    protected int mVariableId;
    protected OnItemClickListener<Data> mListener;
    private LayoutInflater mLayoutInflater;

    public BaseDBBaseAdapter(Context context, int layoutId, int variableId) {
        this.mLayoutId = layoutId;
        this.mVariableId = variableId;
        this.mData = new ArrayList<>();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

//    public BaseDBBaseAdapter(Context context, List<Data> data, int layoutId, int variableId) {
//        this.mData = data == null ? new ArrayList<Data>() : data;
//        this.mLayoutId = layoutId;
//        this.mVariableId = variableId;
//        this.mLayoutInflater = LayoutInflater.from(context);
//        this.mContext = context;
//    }

    @Override
    public int getCount() {
        if (mData == null || mData.isEmpty()) {
            return 0;
        }
        return this.mData.size();
    }

    @Override
    public Data getItem(int position) {
        if (mData == null || mData.isEmpty() || mData.size() <= position) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//
//        if (convertView != null) {
//            holder = (ViewHolder) convertView.getTag();
//
//        } else {
//            convertView = mInflater.inflate(R.layout.adapter_home_product_grid_item, parent, false);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        }
//
//        holder.fill(position);
//        return convertView;




//        if (convertView == null) {
//            dataBinding = DataBindingUtil.inflate(mLayoutInflater, mLayoutId, parent, false);
//            convertView = dataBinding.getRoot();
//            convertView.setTag(dataBinding);
//        } else {
//            dataBinding = (ViewDataBinding) convertView.getTag();
//        }
//
//        dataBinding.setVariable(mVariableId, mData.get(position));
//        return convertView;





        DB binding;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(mLayoutId, parent, false);
            binding = DataBindingUtil.bind(convertView);
            convertView.setTag(binding);
        }else{
            binding = (DB) convertView.getTag();
        }
        binding.setVariable(position, mData.get(position));
        return binding.getRoot();
    }

    /**
     * 设置新数据
     */
    public void setNewData(List<Data> list) {
        this.mData.clear();
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加新数据
     */
    public void appendData(Data data) {
        this.mData.add(data);
        notifyDataSetChanged();
    }

    /**
     * 添加新数据列表
     */
    public void appendDataList(List<Data> list) {
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 设置Item 长按、点击事件
     * @param listener 点击事件
     */
    public void setOnItemListener(OnItemClickListener<Data> listener) {
        this.mListener = listener;
    }
}
