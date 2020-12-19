package club.zarddy.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenUtils {

    /**
     * 获取状态栏高度（像素）
     * @return 状态栏高度
     */
    public static int getStatueBarHeight(Context context) {//拿取状态栏的高度
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return (int) context.getResources().getDimension(identifier);
        }
        return 0;
    }

    public static DisplayMetrics getDefaultMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (null == wm)
            return null;

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕ppi
     */
    public static int getScreenDpi(Context context) {
        DisplayMetrics metrics = getDefaultMetrics(context);
        if (null == metrics)
            return 0;

        return metrics.densityDpi;
    }

    /**
     * 获取屏幕宽度（像素）
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = getDefaultMetrics(context);
        if (null == metrics)
            return 0;

        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高度（像素）
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = getDefaultMetrics(context);
        if (null == metrics)
            return 0;

        return metrics.heightPixels;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getScreenDensity(context) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getScreenDensity(context) + 0.5f);
    }

    /**
     * 屏幕缩放率
     * @return 屏幕缩放率
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 设置屏幕是否保持常亮
     * @param state 是否常亮
     */
    public static void keepScreenOn(Activity activity, boolean state) {
        if (state) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /**
     * 判断设备是否大屏幕或者横屏
     * @return true：平板（横屏），false：手机（竖屏）
     */
    public static boolean isPadOrLandscape(Context context) {
        return ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
    }

    // 屏幕尺寸（英寸）
    private static double SCREEN_SIZE_INCH = 0;

    /**
     * 获取屏幕尺寸（英寸）
     */
    public static double getScreenSizeInch(Context context) {
        if (SCREEN_SIZE_INCH > 0) {
            return SCREEN_SIZE_INCH;
        }

        try {
            DisplayMetrics displayMetrics = getDefaultMetrics(context);
            if (displayMetrics == null) {
                SCREEN_SIZE_INCH = 0;
                return SCREEN_SIZE_INCH;
            }

            float xdpi = displayMetrics.xdpi;
            float ydpi = displayMetrics.ydpi;
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            // 这样可以计算屏幕的物理尺寸
            float width2 = (width / xdpi)*(width / xdpi);
            float height2 = (height / ydpi)*(width / xdpi);

            SCREEN_SIZE_INCH = Math.sqrt(width2+height2);
        } catch (Exception e) {
            SCREEN_SIZE_INCH = 0;
        }
        return SCREEN_SIZE_INCH;
    }

    /**
     * 获取设备屏幕类型，默认平板，当屏幕大于等于20寸，则作为一体机
     */
    public static ScreenType getDeviceScreenType(Context context) {
        if (getScreenSizeInch(context) >= 20D) { // 大于等于20寸的设备，默认当作一体机
            return ScreenType.ALL_IN_ONE;
        } else {
            if (isPadOrLandscape(context)) { // 如果是横屏设备或平板
                return ScreenType.PAD;
            } else {
                return ScreenType.PHONE;
            }
        }
    }

    /**
     * 将activity设为全屏
     * @param on 是否全屏
     */
    public static void setFullscreen(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (on) {
            winParams.flags |=  bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 保存窗口截图
     * @param dir 目标目录路径
     * @param filename 文件名
     * @return 如果保存成功，返回截图地址
     */
    public static String saveWindowScreenshot(final Activity activity, String dir, String filename) {
        View decorView = activity.getWindow().getDecorView();
        return saveViewCapture(activity, dir, filename, decorView);
    }

    /**
     * 保存View截图
     * @param dir 目标目录路径
     * @param filename 文件名
     * @return 如果保存成功，返回截图地址
     */
    public static String saveViewCapture(final Activity activity, String dir, String filename, View decorView) {
        return saveViewCapture(activity, dir, filename, decorView, true);
    }

    public static String saveViewCapture(final Activity activity, String dir, String filename, View decorView, boolean compress) {
        if (decorView == null) {
            return "";
        }

        String fileFullPath = dir + File.separator + filename;

        Bitmap srcBitmap = getViewBitmap(decorView);

        try {
            if (compress) {
                // 限制图片最大宽度为 1024px 进行缩放
                srcBitmap = BitmapUtils.zoomBitmap(srcBitmap, 1024);
            }

            File path = new File(dir);
            if (!path.exists()) {
                if (!path.mkdirs()) { // 如果创建目标目录路径失败
                    return null;
                }
            }

            File file = new File(fileFullPath);
            if (!file.exists()) {
                if (!file.createNewFile()) { // 如果创建目标文件失败
                    return null;
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();

            // 保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        } catch (Exception e) {
            return null;
        }

        return fileFullPath;
    }

    /**
     * 获取屏幕长宽比
     */
    public static float getScreenRate(Context context){
        float H = getScreenWidth(context);
        float W = getScreenHeight(context);
        return (H/W);
    }

    public static Bitmap getViewBitmap(View v) {
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }
}
