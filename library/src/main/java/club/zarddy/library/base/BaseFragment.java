package club.zarddy.library.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import club.zarddy.library.bean.DialogBean;
import club.zarddy.library.bean.TipsMessage;
import club.zarddy.library.lifecycle.BaseViewModel;

public abstract class BaseFragment<VM extends BaseViewModel, DB extends ViewDataBinding> extends BaseNoModelFragment<DB> {

    protected VM mViewModel;

    @Override
    protected DB initDataBinding(@NonNull LayoutInflater inflater, int layoutId, @Nullable ViewGroup container) {
        DB db = super.initDataBinding(inflater, layoutId, container);
        mViewModel = initViewModel();
        initObserve();
        return db;
    }

    /**
     * 监听当前ViewModel中showDialog和showError的值
     */
    private void initObserve() {
        if (mViewModel == null) {
            return;
        }

        mViewModel.getShowDialog(this, new Observer<DialogBean>() {
            @Override
            public void onChanged(DialogBean dialogBean) {
                if (dialogBean.isShowing()) {
                    showLoadingDialog(dialogBean.getMessage());

                } else {
                    dismissLoadingDialog();
                }
            }
        });

        mViewModel.getTipsMessage(this, new Observer<TipsMessage>() {
            @Override
            public void onChanged(TipsMessage tipsMessage) {
                showTipsMessage(tipsMessage);
            }
        });
    }

    /**
     * 初始化ViewModel
     */
    protected abstract VM initViewModel();

    /**
     * 显示ViewModel层发生的消息传输
     */
    protected void showTipsMessage(TipsMessage tipsMessage) {
        if (tipsMessage != null) {
            showMessage(tipsMessage.getMessage());
        }
    }
}
