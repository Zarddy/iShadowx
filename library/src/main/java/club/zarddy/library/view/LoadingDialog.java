package club.zarddy.library.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import club.zarddy.library.R;
import club.zarddy.library.databinding.DialogLoadingBinding;

public class LoadingDialog extends Dialog {

    private DialogLoadingBinding binding;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
        setCanceledOnTouchOutside(false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_loading, null, false);
        setContentView(binding.getRoot());
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = QMUIDisplayHelper.dp2px(context, 220);
            lp.height = QMUIDisplayHelper.dp2px(context, 130);
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }
    }

    /**
     * 设置等待提示信息
     * @param message 提示信息
     */
    public void setLoadingMsg(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        binding.txtMessage.setText(message);
    }
}
