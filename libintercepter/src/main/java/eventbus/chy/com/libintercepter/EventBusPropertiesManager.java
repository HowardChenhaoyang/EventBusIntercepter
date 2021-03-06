package eventbus.chy.com.libintercepter;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

class EventBusPropertiesManager {
    private static EventBusPropertiesManager mEventBusPropertiesManager;
    private static final String TAG = EventBusPropertiesManager.class.getSimpleName();

    private EventBusPropertiesManager() {
    }

    static EventBusPropertiesManager getInstance() {
        if (mEventBusPropertiesManager != null) {
            return mEventBusPropertiesManager;
        }
        synchronized (EventBusPropertiesManager.class) {
            if (mEventBusPropertiesManager == null) {
                mEventBusPropertiesManager = new EventBusPropertiesManager();
            }
        }
        return mEventBusPropertiesManager;
    }

    void copyProperties(Object dst, Class dstClass, Object src) throws NoSuchFieldException, IllegalAccessException {
        Field[] fields = src.getClass().getDeclaredFields();
        for (Field field : fields) {
            Field dstField = dstClass.getDeclaredField(field.getName());
            field.setAccessible(true);
            dstField.setAccessible(true);
            dstField.set(dst, field.get(src));
        }
    }

    <T extends EventBus> List getEventBusSubscribers(T t, Object event) {
        Field field = null;
        Class eventBusClass = t.getClass();
        try {
            do {
                try {
                    field = eventBusClass.getDeclaredField("subscriptionsByEventType");
                    break;
                } catch (NoSuchFieldException noSuchFieldException) {
                    eventBusClass = eventBusClass.getSuperclass();
                }
            } while (EventBus.class.isAssignableFrom(eventBusClass));
            field.setAccessible(true);
            Map map = (Map) field.get(t);
            List list = (List) map.get(event.getClass());
            return list;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
