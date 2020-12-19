package club.zarddy.library.util;

import androidx.annotation.NonNull;

/**
 * 屏幕类型
 */
public enum ScreenType {

    /** 手机 */
    PHONE("1"),
    /** 平板 */
    PAD("2"),
    /** 一体机 */
    ALL_IN_ONE("3");

    private String type;

    ScreenType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return type;
    }
}
