package club.zarddy.library.util.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.ViewConfiguration;

import club.zarddy.library.util.ImageUtils;
import club.zarddy.library.util.ScreenUtils;

import java.lang.reflect.Method;
import java.util.List;

public class LiveCameraUtils {

    private Activity mActivity;
    private Camera mCamera;
    private boolean isPreviewing = false;
    private LiveCameraParamUtils mCameraParam;
    private LiveCameraCallback mCallback;

    private Bitmap mCameraBitmap = null;

    public interface LiveCameraCallback {
        /** 成功打开摄像头 */
        void onOpenedLiveCamera();

        /** 显示控制按钮 */
        void showLiveCameraToolButtons();

        /** 隐藏控制按钮 */
        void hideLiveCameraToolButtons();

        /**
         * 确定使用照片
         * @param photoPath 成功保存照片的路径
         */
        void useLiveCameraPhoto(String photoPath);
    }

    public LiveCameraUtils(Activity activity, LiveCameraCallback cameraCallback) {
        mActivity = activity;
        mCameraParam = LiveCameraParamUtils.getInstance(activity);
        mCallback = cameraCallback;
    }

    /**
     * 打开Camera
     */
    public void openCamera() {
        try {
            mCamera = Camera.open();
            if (mCamera != null) {
                mCallback.onOpenedLiveCamera();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 开启预览
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        if (isPreviewing) {
            if (mCamera != null) {
                mCamera.stopPreview();
            }
            return;
        }

        if (mCamera != null) {
            try {
                Camera.Parameters cameraParameters = mCamera.getParameters();
                cameraParameters.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式

                int screenWidth = ScreenUtils.getScreenWidth(mActivity);
                int screenHeight = ScreenUtils.getScreenHeight(mActivity);
                cameraParameters.setPictureSize(screenWidth, screenHeight);
                //去掉减除虚拟按键的高度，兼容性问题
                cameraParameters.setPreviewSize(screenWidth, screenHeight);

                mCamera.setDisplayOrientation(0);
                List<String> focusModes = cameraParameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                mCamera.setParameters(cameraParameters);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();// 开启预览
            } catch (Exception e) {
                // 如果出错，则使用旧方法再设置一次
                setCameraParametersOldMethod(holder, previewRate);
            }

            isPreviewing = true;
        }
    }

    // 旧的设置方法
    private void setCameraParametersOldMethod(SurfaceHolder holder, float previewRate) {
        if (mCamera != null) {
            try {
                Camera.Parameters cameraParameters = mCamera.getParameters();
                cameraParameters.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式

                // 设置PreviewSize和PictureSize
                Camera.Size pictureSize = mCameraParam.getPropPictureSize(cameraParameters.getSupportedPictureSizes(), previewRate, 640);
                cameraParameters.setPictureSize(pictureSize.width, pictureSize.height);
                //去掉减除虚拟按键的高度，兼容性问题
                Camera.Size previewSize = mCameraParam.getPropPreviewSize(cameraParameters.getSupportedPreviewSizes(), previewRate, 640);
                cameraParameters.setPreviewSize(previewSize.width, previewSize.height);

                mCamera.setDisplayOrientation(0);
                List<String> focusModes = cameraParameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                mCamera.setParameters(cameraParameters);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();// 开启预览
            } catch (Exception e) {
                e.printStackTrace();
            }

            isPreviewing = true;
        }
    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照
     */
    public void doTakePicture() {
        if (isPreviewing && (mCamera != null)) {
            try {
                mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量 */
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        // 快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
        public void onShutter() {

        }
    };
    Camera.PictureCallback mRawCallback = new Camera.PictureCallback() {
        // 拍摄的未压缩原数据的回调,可以为null
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        // 对jpeg图像数据的回调,最重要的一个回调
        public void onPictureTaken(byte[] data, Camera camera) {
            mCameraBitmap = null;
            if (null != data) {
                mCameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// data是字节数据，将其解析成位图
                mCamera.stopPreview();
                isPreviewing = false;

                if (mCallback != null) {
                    // 显示确定、取消按钮
                    mCallback.showLiveCameraToolButtons();
                }
            }
        }
    };

    /**
     * 取消确认张片
     */
    public void cancel() {
        // 再次进入预览
        mCamera.startPreview();
        isPreviewing = true;

        if (mCallback != null) {
            // 隐藏控制按钮
            mCallback.hideLiveCameraToolButtons();
        }
    }

    // 确认使用照片
    public String sure(String filepath) {
        // 保存图片到sdcard
        if (null != mCameraBitmap) {
            // 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
            // 图片竟然不能旋转了，故这里要旋转下
            Bitmap rotaBitmap = ImageUtils.getRotateBitmap(mCameraBitmap, 0.0f);
            filepath = ImageUtils.saveBitmap(rotaBitmap, filepath);
        }
        mCallback.useLiveCameraPhoto(filepath);
        return filepath;
    }

    // 获取虚拟按键的高度
    public int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     */
    private String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }
}
