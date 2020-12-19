package club.zarddy.library.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import club.zarddy.library.R;

public class DialogHelper {

    static boolean confirmDialogResult = false;

    /**
     * 显示确认对话框
     * @param iconResId 顶部图标
     * @param title 标题
     * @param content 对话框内容
     * @param confirmButton 确认按钮-文字
     * @param cancelButton 取消按钮-文字
     * @param listener 点击事件回调
     * @return 对话框
     */
    public static Dialog showConfirmDialog(final Activity activity, final int iconResId, final String title, final String content,
                                           final String confirmButton, final String cancelButton,
                                           final OnDialogClickListener listener) {

        return showConfirmDialog(activity, iconResId, title, content, confirmButton, cancelButton, true, listener);
    }

    public static Dialog showConfirmDialog(final Activity activity, final int iconResId, final String title, final String content,
                                           final String confirmButton, final String cancelButton, final boolean cancelable,
                                           final OnDialogClickListener listener) {

        final Dialog dialog = new Dialog(activity, R.style.dialog_normal);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm, null);
        confirmDialogResult = false;

        // 顶部图标
        ImageView icon = (ImageView) inflate.findViewById(R.id.icon);
        if (iconResId > 0) {
            icon.setImageResource(iconResId);
            icon.setVisibility(View.VISIBLE);
        }
        // 标题
        TextView txtTitle = (TextView) inflate.findViewById(R.id.txt_title);
        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
            txtTitle.setVisibility(View.VISIBLE);
        }
        // 内容
        TextView txtContent = (TextView) inflate.findViewById(R.id.txt_content);
        txtContent.setText(content);
        // 确定按钮
        TextView btnOk = (TextView) inflate.findViewById(R.id.button_ok);
        if (!TextUtils.isEmpty(confirmButton)) {
            btnOk.setText(confirmButton);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialogResult = true;
                dialog.dismiss();
            }
        });
        // 取消按钮
        TextView btnCancel = (TextView) inflate.findViewById(R.id.button_cancel);
        if (!TextUtils.isEmpty(cancelButton)) {
            btnCancel.setText(cancelButton);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialogResult = false;
                dialog.dismiss();
            }
        });
        btnCancel.setVisibility(cancelable ? View.VISIBLE : View.GONE);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (listener != null)
                    listener.onClick(dialog, confirmDialogResult);
            }
        });

        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setCancelable(cancelable);

        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            //设置Dialog从窗体底部弹出
            dialogWindow.setGravity(Gravity.CENTER);
            //获得窗体的属性
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = activity.getResources().getDimensionPixelSize(R.dimen.alert_dialog_width);
            // 将属性设置给窗体
            dialogWindow.setAttributes(lp);
        }
        dialog.show();
        return dialog;
    }

    public static Dialog showConfirmDialog(Activity activity, @DrawableRes int iconResId, String content,
                                           String confirmButton, String cancelButton, OnDialogClickListener listener) {
        return showConfirmDialog(activity, iconResId, "", content, confirmButton, cancelButton, listener);
    }

    public static Dialog showConfirmDialog(Activity activity, @DrawableRes int iconResId, @StringRes int contentResId,
                                           int okButtonResId, int cancelButtonResId, OnDialogClickListener listener) {
        String content = activity.getResources().getString(contentResId);
        String confirmButton = activity.getResources().getString(okButtonResId);
        String cancelButton = activity.getResources().getString(cancelButtonResId);
        return showConfirmDialog(activity, iconResId, "", content, confirmButton, cancelButton, listener);
    }

    public static Dialog showConfirmDialog(Activity activity, @StringRes int contentResId,
                                           int okButtonResId, int cancelButtonResId, OnDialogClickListener listener) {
        return showConfirmDialog(activity, 0, contentResId, okButtonResId, cancelButtonResId, listener);
    }

    public static Dialog showConfirmDialog(Activity activity, String title, String content, OnDialogClickListener listener) {
        return showConfirmDialog(activity, 0, title, content, "", "", listener);
    }

    public static Dialog showConfirmDialog(Activity activity, String title, String content, boolean cancelable, OnDialogClickListener listener) {
        return showConfirmDialog(activity, 0, title, content, "", "", cancelable, listener);
    }

    public static Dialog showConfirmDialog(Activity activity, String content, OnDialogClickListener listener) {
        return showConfirmDialog(activity, 0, "", content, "", "", listener);
    }

    public static Dialog showConfirmDialog(Activity activity, String content, String confirmButton, String cancelButton, OnDialogClickListener listener) {
        return showConfirmDialog(activity, 0, "", content, confirmButton, cancelButton, listener);
    }

    public static Dialog showConfirmDialog(Activity activity, String title, String content, String confirmButton, String cancelButton, OnDialogClickListener listener) {
        return showConfirmDialog(activity, 0, title, content, confirmButton, cancelButton, listener);
    }

    public static Dialog showConfirmDialog(Activity activity, @StringRes int contentResId, OnDialogClickListener listener) {
        return showConfirmDialog(activity, activity.getResources().getString(contentResId),listener);
    }

    public static Dialog showConfirmDialog(Activity activity, @DrawableRes int iconResId, @StringRes int contentResId, OnDialogClickListener listener) {
        return showConfirmDialog(activity, iconResId, contentResId,
                R.string.button_dialog_ok, R.string.button_dialog_cancel, listener);
    }

    /**
     * 显示确认对话框
     * @param iconResId 顶部图标
     * @param title 标题
     * @param content 对话框内容
     * @return 对话框
     */
    public static Dialog showAlertDialog(final Activity activity, final int iconResId, final String title, final String content,
                                         final DialogInterface.OnDismissListener onDismissListener) {
        return showAlertDialog(activity, iconResId, title, content, "", onDismissListener);
    }

    public static Dialog showAlertDialog(final Activity activity, final int iconResId, final String title, final String content,
                                         final String button, final DialogInterface.OnDismissListener onDismissListener) {

        final Dialog dialog = new Dialog(activity, R.style.dialog_normal);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.dialog_alert, null);

        // 顶部图标
        ImageView icon = (ImageView) inflate.findViewById(R.id.icon);
        if (iconResId > 0) {
            icon.setImageResource(iconResId);
            icon.setVisibility(View.VISIBLE);
        }
        // 标题
        TextView txtTitle = (TextView) inflate.findViewById(R.id.txt_title);
        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
            txtTitle.setVisibility(View.VISIBLE);
        }
        // 内容
        TextView txtContent = (TextView) inflate.findViewById(R.id.txt_content);
        txtContent.setText(content);

        if (onDismissListener != null) {
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    onDismissListener.onDismiss(dialogInterface);
                }
            });
        }
        // 确定按钮
        TextView btnOk = (TextView) inflate.findViewById(R.id.button_ok);
        if (!TextUtils.isEmpty(button)) {
            btnOk.setText(button);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            //设置Dialog从窗体底部弹出
            dialogWindow.setGravity(Gravity.CENTER);
            //获得窗体的属性
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = activity.getResources().getDimensionPixelSize(R.dimen.alert_dialog_width);
            // 将属性设置给窗体
            dialogWindow.setAttributes(lp);
        }
        dialog.show();
        return dialog;
    }

    public static Dialog showAlertDialog(final Activity activity, final String content) {

        return showAlertDialog(activity, content, null);
    }

    public static Dialog showAlertDialog(final Activity activity, final String content, final DialogInterface.OnDismissListener onDismissListener) {

        return showAlertDialog(activity, 0, "", content, onDismissListener);
    }
}
