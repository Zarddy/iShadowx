package club.zarddy.library.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import club.zarddy.library.R;
import club.zarddy.library.bean.TipsMessage;
import club.zarddy.library.bean.TipsStatus;

/**
 * 提示消息提示框
 */
public class TipMessageDialog extends Dialog {

    /**
     * 显示，并在 1.5秒后关闭对话框，并关闭页面
     * @param tipsMessage 提示信息
     */
    public static void showAndForceDismiss(Activity activity, TipsMessage tipsMessage) {
        TipMessageDialog dialog = new TipMessageDialog(activity);
        dialog.show(tipsMessage, true);
    }

    private Activity activity;
    private ImageView imgIcon;
    private TextView txtContent;
    private Handler handler = new Handler(Looper.getMainLooper());

    public TipMessageDialog(@NonNull Activity activity) {
        super(activity, R.style.dialog_normal);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip_message);
        setCanceledOnTouchOutside(true);

        imgIcon = (ImageView) findViewById(R.id.img_icon);
        txtContent = (TextView) findViewById(R.id.txt_content);
    }

    public void show(TipsMessage tipsMessage) {
        show(tipsMessage, false);
    }

    public void show(TipsMessage tipsMessage, boolean finishActivity) {
        if (tipsMessage == null) {
            return;
        }
        super.show();
        initMessage(tipsMessage);

        if (finishActivity) {
            this.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    TipMessageDialog.this.activity.finish();
                }
            });
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TipMessageDialog.this.dismiss();
            }
        }, 1500);
    }

    private void initMessage(TipsMessage tipsMessage) {

        int iconResId = R.drawable.ic_done;
        if (tipsMessage.getStatus() == TipsStatus.ERROR) {
            iconResId = R.drawable.ic_error;
        }

        imgIcon.setImageResource(iconResId);
        txtContent.setText(tipsMessage.getMessage());
    }
}
