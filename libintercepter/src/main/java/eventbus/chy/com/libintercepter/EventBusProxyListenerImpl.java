package eventbus.chy.com.libintercepter;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.SubscriberMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventBusProxyListenerImpl implements EventBusProxyListener {
    private static final String TAG = EventBusProxyListenerImpl.class.getSimpleName();
    private EventBusProxyHandler mEventBusProxyHandler;

    @Override
    public void register(EventBusProxy eventBusProxy, Object subscriber) {
        try {
            Field stickyEventsField = EventBus.class.getDeclaredField("stickyEvents");
            stickyEventsField.setAccessible(true);
            Map<Class<?>, Object> stickyEvents = (Map<Class<?>, Object>) stickyEventsField.get(eventBusProxy);
            Field subscriberMethodFinderField = EventBus.class.getDeclaredField("subscriberMethodFinder");
            subscriberMethodFinderField.setAccessible(true);
            Object subscriberMethodFinder = subscriberMethodFinderField.get(eventBusProxy);
            Method findSubscriberMethods = subscriberMethodFinder.getClass().getDeclaredMethod("findSubscriberMethods");
            findSubscriberMethods.setAccessible(true);
            List subscriberMethods = (List) findSubscriberMethods.invoke(subscriberMethodFinder, subscriber.getClass());
            if (subscriberMethods == null) return;
            List<EventBusSubscription> eventBusSubscriptions = new ArrayList<>();
            for (Object subscriberMethod : subscriberMethods) {
                EventBusSubscriberMethod eventBusSubscriberMethod = new EventBusSubscriberMethod();
                EventBusPropertiesManager.getInstance().copyProperties(eventBusSubscriberMethod, eventBusSubscriberMethod.getClass(), subscriberMethod);
                Object event = stickyEvents.get(eventBusSubscriberMethod.eventType);
                if (event != null) {
                    EventBusSubscription eventBusSubscription = new EventBusSubscription();
                    eventBusSubscription.subscriber = subscriber;
                    eventBusSubscription.subscriberMethod = eventBusSubscriberMethod;
                    eventBusSubscriptions.add(eventBusSubscription);
                }
            }
            mEventBusProxyHandler.invokedSubscriptionsWhenRegister(eventBusSubscriptions);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public Object beforePost(EventBusProxy eventBusProxy, Object event) {
        if (mEventBusProxyHandler != null) {
            event = mEventBusProxyHandler.beforePost(event, false);
            mEventBusProxyHandler.invokedSubscriptions(getEventBusSubscriptions(eventBusProxy, event));
        }
        return event;
    }

    @Override
    public void afterPost(EventBusProxy eventBusProxy, Object event) {
        if (mEventBusProxyHandler != null) {
            mEventBusProxyHandler.afterPost(event, false);
        }
    }

    @Override
    public Object beforePostSticky(EventBusProxy eventBusProxy, Object event) {
        if (mEventBusProxyHandler != null) {
            event = mEventBusProxyHandler.beforePost(event, true);
            mEventBusProxyHandler.invokedSubscriptions(getEventBusSubscriptions(eventBusProxy, event));
        }
        return event;
    }

    @Override
    public void afterPostSticky(EventBusProxy eventBusProxy, Object event) {
        if (mEventBusProxyHandler != null) {
            mEventBusProxyHandler.afterPost(event, true);
        }
    }

    @Override
    public void unregister(EventBusProxy eventBusProxy, Object subscriber) {

    }

    public void setEventBusProxyHandler(EventBusProxyHandler eventBusProxyHandler) {
        this.mEventBusProxyHandler = eventBusProxyHandler;
    }


    private List<EventBusSubscription> getEventBusSubscriptions(EventBusProxy eventBusProxy, Object event) {
        try {
            List<EventBusSubscription> eventBusSubscriptions = new ArrayList<>();
            List subscribers = EventBusPropertiesManager.getInstance().getEventBusSubscribers(eventBusProxy, event);
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
                EventBusPropertiesManager.getInstance().copyProperties(eventBusSubscriberMethod, eventBusSubscriberMethod.getClass(), subscriberMethod);
                eventBusSubscription.subscriberMethod = eventBusSubscriberMethod;
                eventBusSubscriptions.add(eventBusSubscription);
            }
            return eventBusSubscriptions;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}