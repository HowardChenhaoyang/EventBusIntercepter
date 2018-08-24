package eventbus.chy.com.libintercepter;


import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.SubscriberMethod;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EventBusProxy extends EventBus {
    private EventBusProxyHandler mEventBusProxyHandler;
    private static final String TAG = EventBusProxy.class.getSimpleName();
    private EventBus originEventBus;

    public EventBusProxy(EventBus eventBus) {
        try {
            EventBusPropertiesManager.getInstance().copyProperties(this, eventBus);
        } catch (Exception exception) {
            originEventBus = eventBus;
            Log.e(TAG, exception.getMessage(), exception);
        }
    }

    @Override
    public void post(Object event) {
        if (originEventBus != null) {
            originEventBus.post(event);
            return;
        }
        if (mEventBusProxyHandler != null) {
            event = mEventBusProxyHandler.beforePost(event, false);
        }
        mEventBusProxyHandler.subscribers(getEventBusSubscriptions(event));
        super.post(event);
        if (mEventBusProxyHandler != null) {
            mEventBusProxyHandler.afterPost(event, false);
        }
    }

    private List<EventBusSubscription> getEventBusSubscriptions(Object event) {
        try {
            List<EventBusSubscription> eventBusSubscriptions = new ArrayList<>();
            List subscribers = EventBusPropertiesManager.getInstance().getEventBusSubscribers(this, event);
            if (subscribers == null) return null;
            for (Object object : subscribers) {
                EventBusSubscription eventBusSubscription = new EventBusSubscription();

                Field subscriberField = object.getClass().getDeclaredField("subscriber");
                subscriberField.setAccessible(true);
                Object subscriber = subscriberField.get(object);
                eventBusSubscription.subscriber = subscriber;

                Field subscriberMethodField = object.getClass().getDeclaredField("subscriberMethod");
                subscriberMethodField.setAccessible(true);
                SubscriberMethod subscriberMethod = (SubscriberMethod) subscriberMethodField.get(object);
                EventBusSubscriberMethod eventBusSubscriberMethod = new EventBusSubscriberMethod();
                EventBusPropertiesManager.getInstance().copyProperties(eventBusSubscriberMethod, subscriberMethod);
                eventBusSubscription.subscriberMethod = eventBusSubscriberMethod;
                eventBusSubscriptions.add(eventBusSubscription);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void postSticky(Object event) {
        if (originEventBus != null) {
            originEventBus.postSticky(event);
            return;
        }
        if (mEventBusProxyHandler != null) {
            event = mEventBusProxyHandler.beforePost(event, true);
        }
        super.postSticky(event);
        if (mEventBusProxyHandler != null) {
            mEventBusProxyHandler.afterPost(event, true);
        }
    }

    public void setEventBusProxyHandler(EventBusProxyHandler eventBusProxyHandler) {
        this.mEventBusProxyHandler = eventBusProxyHandler;
    }
}
