package club.zarddy.library.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtils {

    /**
     * 从Drawable中获取Bitmap对象
     * @param drawable 需要转换成bitmap的drawable对象
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        try {
            if (drawable == null) {
                return null;
            }

            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(
                    intrinsicWidth <= 0 ? 100 : intrinsicWidth,
                    intrinsicHeight <= 0 ? 100 : intrinsicHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;

        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap convertRotate(Bitmap bitmap, float degrees) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap emptyBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas canvas = new Canvas(emptyBitmap);
        Matrix m = new Matrix();
        // m.postScale(1, -1); //镜像垂直翻转
        m.postRotate(degrees); //旋转-90度
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m, true);
        canvas.drawBitmap(newBitmap,
                new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight()),
                new Rect(0, 0, w, h),
                null);
        return emptyBitmap;
    }

    public static Bitmap mirrorImage(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap emptyBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas canvas = new Canvas(emptyBitmap);
        Matrix m = new Matrix();
        // m.postScale(1, -1); //镜像垂直翻转
        m.postScale(-1, 1); // 镜像水平翻转
        // m.postRotate(180); //旋转-90度
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m, true);
        canvas.drawBitmap(newBitmap,
                new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight()),
                new Rect(0, 0, w, h),
                null);
        return emptyBitmap;
    }

    /**
     * 从Drawable中获取Bitmap对象
     * @param drawable 图片drawable对象
     * @param defaultSize 边的默认长度
     * @return 图片bitmap对象
     */
    public static Bitmap drawable2Bitmap(Drawable drawable, int defaultSize) {
        try {
            if (drawable == null) {
                return null;
            }

            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();

            float scale = defaultSize * 1f / intrinsicWidth;
            if (intrinsicHeight > intrinsicWidth) {
                scale = defaultSize * 1f / intrinsicHeight;
            }

            intrinsicWidth *= scale;
            intrinsicHeight *= scale;

            Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;

        } catch (Exception e) {
            return null;

        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * 根据最大宽度缩放图片
     * @param srcBitmap 原图片
     * @param maxWidth 最大宽度（单位：像素）
     * @return 缩放后的图片
     */
    public static Bitmap zoomBitmap(Bitmap srcBitmap, int maxWidth) {
        if (srcBitmap == null) {
            return null;
        }

        int srcBitmapWidth = srcBitmap.getWidth();
        if (maxWidth <= 0 || srcBitmapWidth <= maxWidth) {
            return srcBitmap;
        }

        // 缩放比
        float scale = maxWidth * 1f / srcBitmapWidth;
        return scaleBitmap(srcBitmap, scale);
    }

    /**
     * 根据最大宽度缩放图片
     * @param srcBitmap 原图片
     * @param scale 缩放比
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmap(Bitmap srcBitmap, float scale) {
        if (srcBitmap == null) {
            return null;
        }

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.preScale(scale, scale);

        Bitmap newBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, false);
        if (newBitmap.equals(srcBitmap)) {
            return newBitmap;
        }

        srcBitmap.recycle();
        return newBitmap;
    }
}
