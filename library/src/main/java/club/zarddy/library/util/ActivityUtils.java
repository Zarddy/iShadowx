package club.zarddy.library.util;

import android.app.Activity;

import java.util.Stack;

public class ActivityUtils {

    private static Stack<Activity> stack;
    private static ActivityUtils manager;

    public static synchronized ActivityUtils getInstance() {
        if (manager == null) {
            manager = new ActivityUtils();
            stack = new Stack<>();
        }
        return manager;
    }

    public synchronized void addActivity(Activity activity) {
        stack.add(activity);
    }

    public synchronized void removeActivity(Activity activity) {
//        stack.remove(activity);
        finishActivity(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> clazz) {
        for (Activity activity : stack) {
            if (activity.getClass().equals(clazz)) {
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            stack.remove(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : stack) {
            if (activity != null) {
                activity.finish();
            }
        }
        stack.clear();
    }
}
