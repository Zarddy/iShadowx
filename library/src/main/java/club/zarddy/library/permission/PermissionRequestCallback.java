package club.zarddy.library.permission;

/**
 * 权限请求回调
 */
public interface PermissionRequestCallback {
    /**
     * 权限获取成功
     */
    void requestSuccess();

    /**
     * 权限获取失败
     */
    void requestFailed();
}
