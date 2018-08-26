package eventbus.chy.com.libintercepter;

import android.support.annotation.CheckResult;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

public class EventBusProxyFactory {
    private static final String TAG = "EventBusProxyFactory";

    /**
     * 代理传入的eventBus。该eventBus不能是通过EventBus.getDefault()获取的对象。
     *
     * @param isDebug 由于使用了大量反射，所以在线上应该传入false
     * @param eventBus
     * @param eventBusProxyListener
     * @return 如果代理成功，则返回代理类，需要保存该返回的代理类进行事件post，注册等相关操作。如果代理失败或者是正式环境，则返回原对象。
     */
    @CheckResult
    public static EventBus eventBusProxy(boolean isDebug, EventBus eventBus, EventBusProxyListener eventBusProxyListener) {
        if (!isDebug) return eventBus;
        if (eventBus == EventBus.getDefault()) {
            throw new RuntimeException("eventBus is created by EventBus.getDefault(), please use defaultEventBusProxy or defaultEventBusProxy");
        }
        EventBusProxy eventBusProxy = new EventBusProxy(eventBus, isDebug);
        if (eventBusProxy.isAgent()) {
            eventBusProxy.setEventBusProxyListener(eventBusProxyListener);
            return eventBusProxy;
        }
        return eventBus;
    }

    /**
     * @See 代理传入的eventBus。该eventBus不能是通过EventBus.getDefault()获取的对象。
     * @param isDebug 由于使用了大量反射，所以在线上应该传入false
     * @param eventBus
     * @param eventBusProxyHandler
     * @return 如果代理成功，则返回代理类，需要保存该返回的代理类进行事件post，注册等相关操作。如果代理失败或者是正式环境，则返回原对象。
     */
    @CheckResult
    public static EventBus eventBusProxy(boolean isDebug, EventBus eventBus, EventBusProxyHandler eventBusProxyHandler) {
        EventBusProxyListenerImpl eventBusProxyListener = new EventBusProxyListenerImpl();
        eventBusProxyListener.setEventBusProxyHandler(eventBusProxyHandler);
        return eventBusProxy(isDebug, eventBus, eventBusProxyListener);
    }

    /**
     * 代理通过EventBus.getDefault()获取的对象。以后依然可以使用EventBus.getDefault()进行操作。
     * @param isDebug 由于使用了大量反射，所以在线上应该传入false
     * @param eventBusProxyListener
     * @return true 表示代理成功
     */
    public static boolean defaultEventBusProxy(boolean isDebug, EventBusProxyListener eventBusProxyListener) {
        if (!isDebug) return false;
        EventBusProxy eventBusProxy = replaceEventBusDefaultInstance(true);
        if (eventBusProxy == null) {
            return false;
        }
        eventBusProxy.setEventBusProxyListener(eventBusProxyListener);
        return true;
    }


    /**
     * 代理通过EventBus.getDefault()获取的对象。以后依然可以使用EventBus.getDefault()进行操作。
     * @param isDebug 由于使用了大量反射，所以在线上应该传入false
     * @param eventBusProxyHandler
     * @return true 表示代理成功
     */
    public static boolean defaultEventBusProxy(boolean isDebug, EventBusProxyHandler eventBusProxyHandler) {
        EventBusProxyListenerImpl eventBusProxyListener = new EventBusProxyListenerImpl();
        eventBusProxyListener.setEventBusProxyHandler(eventBusProxyHandler);
        return defaultEventBusProxy(isDebug, eventBusProxyListener);
    }

    private static EventBusProxy replaceEventBusDefaultInstance(boolean isDebug) {
        EventBus defaultEventBus = EventBus.getDefault();
        try {
            Field defaultEventBusField = defaultEventBus.getClass().getDeclaredField("defaultInstance");
            defaultEventBusField.setAccessible(true);
            EventBusProxy eventBusProxy = new EventBusProxy((EventBus) defaultEventBusField.get(null), isDebug);
            defaultEventBusField.set(null, eventBusProxy);
            return eventBusProxy;
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
