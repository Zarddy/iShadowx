package club.zarddy.library.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import club.zarddy.library.R;

public class ToastUtils {

    public static void showText(Context context, @StringRes int resId) {
        showText(context, context.getText(resId), Toast.LENGTH_SHORT);
    }

    public static void showText(Context context, CharSequence text) {
        showText(context, text, Toast.LENGTH_SHORT);
    }

    public static void showText(Context context, CharSequence text, int duration) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        ImageView icon = (ImageView) toastView.findViewById(R.id.icon);
        TextView textView = (TextView) toastView.findViewById(R.id.text);
        textView.setText(text);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(toastView);
        toast.show();
    }
}
