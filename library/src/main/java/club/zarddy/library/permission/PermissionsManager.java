package club.zarddy.library.permission;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import club.zarddy.library.dialog.DialogHelper;
import club.zarddy.library.dialog.OnDialogClickListener;

import java.util.ArrayList;

public class PermissionsManager {

    public final static int REQUEST_CODE = 0x9999;

    private Activity activity;
    private PermissionRequestCallback requestCallback;

    /**
     * 本程序中需要用到的权限
     */
    private final static String[] requestedPermissions = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public  PermissionsManager (Activity activity) {
        this.activity=activity;
    }

    /**
     * 检查所给的权限是否已被授权
     * @param permission 要检查的权限名字
     * @return true:已被授权；false:未被授权
     */
    public boolean checkPermission(String permission) {
        return  permission != null && (ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 请求授权,限内部调用
     * @param permissions 当为null时，表示遍历整个程序需要的权限，
     *                    当不为null时，表示请求传入的权限，此时permissions的有效length是1，其余大于1的权限请求将不会处理
     * @return true：已经授权，false：需要授权
     */
    public boolean requestPermissions(String[] permissions) {
        if (permissions==null) {
            permissions = requestedPermissions;
        }
        //过滤出目前还未被授权的权限
        String[] noGrantedPermissions;
        ArrayList<String> l = new ArrayList<String>();
        for (String s : permissions) {
            if (!checkPermission(s)) {
                l.add(s);
            }
        }

        if (l.size() == 0) {
            return true;
        }
        //一次性请求所有未被授权的权限
        noGrantedPermissions = (String[])l.toArray(new String[l.size()]);
        ActivityCompat.requestPermissions(activity, noGrantedPermissions, REQUEST_CODE);
        return false;
    }

    /**
     * 请求程序所有运行时需要申请的权限
     * @return true：已经授权，false：需要授权
     */
    public boolean requestPermissions() {
        return requestPermissions(null);
    }

    /**
     * 请求指定的单个权限
     * @param permission 给定的权限
     */
    public void requestPermission(String permission, int tipsResId) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            showRequestDialog(activity.getString(tipsResId));
        } else {
            requestPermissions(new String[]{permission});
        }
    }

    /**
     * 处理权限申请的回调方法
     */
    public void handle(int requestCode,String[] permissions,int[] grantResults) {
        if (requestCallback==null) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission success
                requestCallback.requestSuccess();
            } else {
                // Permission Denied
                requestCallback.requestFailed();
            }
        }
    }

    /**
     * 检测权限，如未被授权则申请，并继续处理业务
     * @param permission 需要使用的权限
     * @param requestCallback 回调类，负责处理具体业务，包含权限已被授权和申请后的情况
     */
    public void workWithPermission(String permission, int tipsResId, PermissionRequestCallback requestCallback) {
        if (permission == null||requestCallback == null) {
            return;
        }

        this.requestCallback = requestCallback;

        // 如果系统版本低于 6.0，则不检测权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            requestCallback.requestSuccess();
            return;
        }

        if (checkPermission(permission)) {
            requestCallback.requestSuccess();
        } else {
            requestPermission(permission, tipsResId);
        }
    }

    /**
     * 提示跳转到授权设置
     * @param content 提示内容
     */
    private void showRequestDialog(String content){
        DialogHelper.showConfirmDialog(activity, content, new OnDialogClickListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) { // 点击了确定，跳转至设置
                    Intent i = new Intent();
                    i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    i.setData(Uri.fromParts("package", activity.getPackageName(), null));
                    activity.startActivity(i);
                } else {
                    if (requestCallback != null) {
                        requestCallback.requestFailed();
                    }
                }
            }
        });
    }
}
