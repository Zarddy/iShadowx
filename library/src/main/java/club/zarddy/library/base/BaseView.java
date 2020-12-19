package club.zarddy.library.base;

import androidx.annotation.StringRes;

public interface BaseView<T> {

    void setPresenter(T presenter);

    /**
     * 显示提示信息
     * @param stringResId 提示信息，字符串资源id
     */
    void showMessage(@StringRes int stringResId);

    /**
     * 显示提示信息
     * @param message 提示信息
     */
    void showMessage(String message);
}
