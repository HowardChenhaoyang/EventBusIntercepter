package eventbus.chy.com.libintercepter;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

public class EventBusProxyFactory {
    private static final String TAG = "EventBusProxyFactory";

    public static EventBus eventBusProxy(boolean isDebug, EventBus eventBus) {
        if (!isDebug) return eventBus;
        if (eventBus == EventBus.getDefault()) {
            throw new RuntimeException("eventBus is created by EventBus.getDefault(), please use defaultEventBusProxy or defaultEventBusProxy");
        }
        EventBusProxy eventBusProxy = new EventBusProxy(eventBus, isDebug);
        if (eventBusProxy.isAgent()) {
            return eventBusProxy;
        }
        return eventBus;
    }

    public static boolean defaultEventBusProxy(boolean isDebug, EventBusProxyListener eventBusProxyListener) {
        if (!isDebug) return false;
        EventBusProxy eventBusProxy = replaceEventBusDefaultInstance(true);
        if (eventBusProxy == null) {
            return false;
        }
        eventBusProxy.setEventBusProxyListener(eventBusProxyListener);
        return true;
    }

    public static boolean defaultEventBusProxy(boolean isDebug, EventBusProxyHandler eventBusProxyHandler) {
        if (!isDebug) return false;
        EventBusProxy eventBusProxy = replaceEventBusDefaultInstance(true);
        if (eventBusProxy == null) {
            return false;
        }
        EventBusProxyListenerImpl eventBusProxyListener = new EventBusProxyListenerImpl();
        eventBusProxy.setEventBusProxyListener(eventBusProxyListener);
        eventBusProxyListener.setEventBusProxyHandler(eventBusProxyHandler);
        return true;
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
