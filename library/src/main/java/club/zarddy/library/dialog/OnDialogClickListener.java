package club.zarddy.library.dialog;

import android.app.Dialog;

/**
 * 对话框点击监听
 */
public interface OnDialogClickListener {
    /**
     * 对话框点击回调事件
     * @param dialog 对话框
     * @param confirm 是否点击了确认按钮
     */
    void onClick(Dialog dialog, boolean confirm);
}