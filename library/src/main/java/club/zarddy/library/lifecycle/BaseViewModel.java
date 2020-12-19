package club.zarddy.library.lifecycle;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import club.zarddy.library.bean.DialogBean;
import club.zarddy.library.bean.TipsMessage;
import club.zarddy.library.bean.TipsStatus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends ViewModel {

    /**
     * 管理RxJava请求
     */
    private CompositeDisposable compositeDisposable;

    /**
     * 用来通知 Activity/Fragment 是否显示等待加载Dialog
     */
    protected DialogLiveData<DialogBean> mLoadingDialog = new DialogLiveData<>();

    /**
     * 当 ViewModel 层出现错误需要通知到 Activity/Fragment
     */
    protected MutableLiveData<TipsMessage> mTipsMessage = new MutableLiveData<>();

    /**
     * 添加 rxjava 发出的请求
     * @param disposable
     */
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void getShowDialog(LifecycleOwner owner, Observer<DialogBean> observer) {
        mLoadingDialog.observe(owner, observer);
    }

    public void getTipsMessage(LifecycleOwner owner, Observer<TipsMessage> observer) {
        mTipsMessage.observe(owner, observer);
    }

    /**
     * ViewModel 销毁同时也取消请求
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        mLoadingDialog = null;
        mTipsMessage = null;
    }

    public DialogLiveData<DialogBean> getShowDialog() {
        return mLoadingDialog;
    }

    public void showSuccessMessage(String message) {
        mTipsMessage.setValue(new TipsMessage(TipsStatus.SUCCESS, message));
    }

    public void showNormalMessage(String message) {
        mTipsMessage.setValue(new TipsMessage(TipsStatus.NORMAL, message));
    }

    public void showErrorMessage(String message) {
        mTipsMessage.setValue(new TipsMessage(TipsStatus.ERROR, message));
    }
}
