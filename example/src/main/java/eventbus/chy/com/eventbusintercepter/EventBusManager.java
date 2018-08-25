package eventbus.chy.com.eventbusintercepter;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import eventbus.chy.com.libintercepter.EventBusProxy;
import eventbus.chy.com.libintercepter.EventBusProxyHandler;
import eventbus.chy.com.libintercepter.EventBusSubscription;

public class EventBusManager {
    private final EventBus mEventBus;
    private static final String TAG = "EventBusManager";
    private static EventBusManager mEventBusManager;

    private EventBusManager() {
        EventBusProxy eventBusProxy = new EventBusProxy(new EventBus(), true);
        eventBusProxy.setEventBusProxyHandler(new EventBusProxyHandler() {
            @Override
            public Object beforePost(Object event, boolean isStick) {
                Log.d(TAG, "event is " + event + " isStick is " + isStick);
                return event;
            }

            @Override
            public void afterPost(Object event, boolean isStick) {
                Log.d(TAG, "event is " + event + " isStick is " + isStick);
            }

            @Override
            public void subscribers(List<EventBusSubscription> eventBusSubscriptions) {
                for (EventBusSubscription eventBusSubscription : eventBusSubscriptions) {
                    Log.d(TAG, eventBusSubscription.subscriber.toString());
                    Log.d(TAG, eventBusSubscription.subscriberMethod.toString());
                }
            }
        });
        mEventBus = eventBusProxy;
    }

    public static EventBus getEventBus() {
        return getInstance().mEventBus;
    }

    private static EventBusManager getInstance() {
        if (mEventBusManager == null) {
            synchronized (EventMessage.class) {
                if (mEventBusManager == null) {
                    mEventBusManager = new EventBusManager();
                }
            }
        }
        return mEventBusManager;
    }
}
