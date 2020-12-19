package club.zarddy.library.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class DeviceUtils {

    /**
     * 复制文本内容到剪贴板
     */
    public static void copyToClipboard(Context context, CharSequence label, CharSequence text) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText(label, text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}
