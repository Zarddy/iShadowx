package club.zarddy.ishadowx;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

public class IShadowxApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化 Hawk key-value 数据存储组件
        Hawk.init(this).build();
    }
}
