package club.zarddy.library.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;

import com.qmuiteam.qmui.arch.QMUIFragment;
import club.zarddy.library.util.ToastUtils;
import club.zarddy.library.view.LoadingDialog;

/**
 * 不需要ViewModel的页面基类
 */
public abstract class BaseNoModelFragment<DB extends ViewDataBinding> extends QMUIFragment {

    protected DB mDataBinding;
    protected FragmentActivity mActivity;
    private LoadingDialog mLoadingDialog;

    @Override
    protected View onCreateView() {
        return mDataBinding.getRoot();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = initDataBinding(inflater, getContentViewId(), container);
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        initView();
        setData();
        setEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDataBinding != null) {
            mDataBinding.unbind();
        }
    }

    /**
     * 初始化DataBinding
     */
    protected DB initDataBinding(@NonNull LayoutInflater inflater, @LayoutRes int layoutId, @Nullable ViewGroup container) {
        return DataBindingUtil.inflate(inflater, layoutId, container, false);
    }

    /**
     * 显示加载对话框
     * @param message 提示信息
     */
    protected void showLoadingDialog(String message) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mActivity);
        }
        mLoadingDialog.setLoadingMsg(message);
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void showMessage(@StringRes int stringResId) {
        showMessage(mActivity.getResources().getString(stringResId));
    }

    public void showMessage(String message) {
        ToastUtils.showText(mActivity, message);
    }

    protected abstract int getContentViewId();

    protected abstract void initView();

    protected abstract void setData();

    protected abstract void setEvent();
}
