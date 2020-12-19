package club.zarddy.library.permission;

import android.Manifest;
import android.app.Activity;

import club.zarddy.library.R;

public class PermissionHelper {

    /**
     * 检测是否有读写存储的权限
     * @param callback 权限回调
     */
    public static void checkWriteExternalStoragePermission(Activity activity, PermissionRequestCallback callback) {

        PermissionsManager manager = new PermissionsManager(activity);
        manager.workWithPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.toast_need_write_storage_permission, callback);
    }

    /**
     * 检测是否有拨打电话的权限
     * @param callback 权限回调
     */
    public static void checkCallPhonePermission(Activity activity, PermissionRequestCallback callback) {

        PermissionsManager manager = new PermissionsManager(activity);
        manager.workWithPermission(Manifest.permission.CALL_PHONE, R.string.toast_need_call_permission, callback);
    }

    /**
     * 检测是否有拍照权限
     */
    public static void checkCameraPermission(Activity activity, PermissionRequestCallback callback) {
        PermissionsManager manager = new PermissionsManager(activity);
        manager.workWithPermission(Manifest.permission.CAMERA, R.string.toast_need_camera_permission, callback);
    }

    /**
     * 检查指定到权限是否已获取，并请求权限
     * @param permission 权限
     * @return true:已被授权；false:未被授权
     */
    public static boolean requestPermissions(Activity activity, String permission) {
        PermissionsManager permissionsManager = new PermissionsManager(activity);
        return permissionsManager.requestPermissions(new String[]{permission});
    }
}
