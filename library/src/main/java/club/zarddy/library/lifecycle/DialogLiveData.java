package club.zarddy.library.lifecycle;

import androidx.lifecycle.MutableLiveData;

import club.zarddy.library.bean.DialogBean;

public final class DialogLiveData<T> extends MutableLiveData {

    private DialogBean bean = new DialogBean();

    public void setValue(boolean isShowing) {
        bean.setShowing(isShowing);
        bean.setMessage("");
        setValue((T) bean);
    }

    public void setValue(boolean isShowing, String message) {
        bean.setShowing(isShowing);
        bean.setMessage(message);
        setValue((T) bean);
    }
}
