package club.zarddy.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import club.zarddy.library.util.ActivityUtils;
import club.zarddy.library.util.ToastUtils;
import club.zarddy.library.view.LoadingDialog;

/**
 * 不需要ViewModel的页面基类
 * @param <DB>
 */
public abstract class BaseNoModelActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    protected DB mDataBinding;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().addActivity(this);
        this.setContentView(getContextViewId());

        mDataBinding = initDataBind(getContextViewId());
        initView();
        setData();
        setEvent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        setData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDataBinding != null) {
            mDataBinding.unbind();
        }
        ActivityUtils.getInstance().removeActivity(this);
    }

    /**
     * 初始化DataBinding
     */
    protected DB initDataBind(@LayoutRes int layoutId) {
        return DataBindingUtil.setContentView(this, layoutId);
    }

    /**
     * 显示加载对话框
     * @param message 提示信息
     */
    protected void showLoadingDialog(String message) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }

        try {
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.setLoadingMsg(message);
                mLoadingDialog.show();
            }
        } catch (Exception e) {}
    }

    /**
     * 隐藏加载对话框
     */
    protected void dismissLoadingDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                        mLoadingDialog.dismiss();
                    }
                } catch (Exception e) {}
            }
        }, 300);
    }

    public void showMessage(@StringRes int stringResId) {
        showMessage(getResources().getString(stringResId));
    }

    public void showMessage(String message) {
        ToastUtils.showText(this, message);
    }

    protected abstract int getContextViewId();

    protected abstract void initView();

    protected abstract void setData();

    protected abstract void setEvent();

    public void onClick(View view) {};
}
