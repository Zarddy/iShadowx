package club.zarddy.library.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    /**
     * 旋转Bitmap
     * @param bitmap 目标bitmap
     * @param rotateDegree 旋转的角度
     * @return 得到旋转后的bitmap
     */
    public static Bitmap getRotateBitmap(Bitmap bitmap, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    /**
     * 保存Bitmap到sdcard
     * @param bitmap 图片对象
     * @param dstPath 目标文件存储路径
     * @return 如果保存成功，则返回文件路径
     */
    public static String saveBitmap(Bitmap bitmap, String dstPath){
        try {
            File file = new File(dstPath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return "";
                }
            }

            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            fout.close();

            return dstPath;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
