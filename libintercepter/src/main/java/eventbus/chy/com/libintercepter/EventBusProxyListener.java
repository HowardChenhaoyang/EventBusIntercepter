package eventbus.chy.com.libintercepter;

import android.support.annotation.NonNull;

public interface EventBusProxyListener {
    void register(EventBusProxy eventBusProxy, Object subscriber);

    /**
     * @param eventBusProxy
     * @param event
     * @return 此处返回不能为空，该event用于事件传递，因此可对原event进行替换。
     */
    @NonNull
    Object beforePost(EventBusProxy eventBusProxy, Object event);

    void afterPost(EventBusProxy eventBusProxy, Object event);

    @NonNull
    Object beforePostSticky(EventBusProxy eventBusProxy, Object event);

    void afterPostSticky(EventBusProxy eventBusProxy, Object event);

    void unregister(EventBusProxy eventBusProxy, Object subscriber);
}
