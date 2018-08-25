package eventbus.chy.com.libintercepter;

import android.support.annotation.NonNull;

public interface EventBusProxyListener {
    void register(EventBusProxy eventBusProxy, Object subscriber);

    @NonNull
    Object beforePost(EventBusProxy eventBusProxy, Object event);

    void afterPost(EventBusProxy eventBusProxy, Object event);

    @NonNull
    Object beforePostSticky(EventBusProxy eventBusProxy, Object event);

    void afterPostSticky(EventBusProxy eventBusProxy, Object event);

    void unregister(EventBusProxy eventBusProxy, Object subscriber);
}
