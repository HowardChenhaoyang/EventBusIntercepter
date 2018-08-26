package eventbus.chy.com.libintercepter;


import android.util.Log;

import org.greenrobot.eventbus.EventBus;

class EventBusProxy extends EventBus {
    private EventBusProxyListener mEventBusProxyListener;
    private static final String TAG = EventBusProxy.class.getSimpleName();
    private EventBus originEventBus;

    EventBusProxy(EventBus eventBus, boolean isDebug) {
        if (!isDebug) {
            originEventBus = eventBus;
            return;
        }
        try {
            EventBusPropertiesManager.getInstance().copyProperties(this, getClass().getSuperclass(), eventBus);
        } catch (Exception exception) {
            originEventBus = eventBus;
            Log.e(TAG, exception.getMessage(), exception);
        }
    }

    boolean isAgent() {
        return originEventBus == null;
    }

    void setEventBusProxyListener(EventBusProxyListener eventBusProxyListener) {
        this.mEventBusProxyListener = eventBusProxyListener;
    }

    @Override
    public void register(Object subscriber) {
        super.register(subscriber);
        if (mEventBusProxyListener != null) {
            mEventBusProxyListener.register(this, subscriber);
        }
    }

    @Override
    public void post(Object event) {
        if (originEventBus != null) {
            originEventBus.post(event);
            return;
        }
        if (mEventBusProxyListener != null) {
            event = mEventBusProxyListener.beforePost(this, event);
        }
        super.post(event);
        if (mEventBusProxyListener != null) {
            mEventBusProxyListener.afterPost(this, event);
        }
    }

    @Override
    public void postSticky(Object event) {
        if (originEventBus != null) {
            originEventBus.postSticky(event);
            return;
        }
        if (mEventBusProxyListener != null) {
            event = mEventBusProxyListener.beforePostSticky(this, event);
        }
        super.postSticky(event);
        if (mEventBusProxyListener != null) {
            mEventBusProxyListener.afterPostSticky(this, event);
        }
    }

    @Override
    public synchronized void unregister(Object subscriber) {
        super.unregister(subscriber);
        if (mEventBusProxyListener != null) {
            mEventBusProxyListener.unregister(this, subscriber);
        }
    }
}
