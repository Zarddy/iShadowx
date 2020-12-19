package club.zarddy.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class ApkUtils {

    /**
     * 安装apk
     * @param filepath 安装包文件路径
     */
    public static void installApk(Context context, String filepath) {
        File appFile = new File(filepath);
        if (!appFile.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, PackageUtils.getPackageName(context) + ".fileprovider", appFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            data = Uri.fromFile(appFile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
