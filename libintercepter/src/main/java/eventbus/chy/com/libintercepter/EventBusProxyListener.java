package eventbus.chy.com.libintercepter;

public interface EventBusProxyListener {
    void register(EventBusProxy eventBusProxy, Object subscriber);

    Object beforePost(EventBusProxy eventBusProxy, Object event);

    void afterPost(EventBusProxy eventBusProxy, Object event);

    Object beforePostSticky(EventBusProxy eventBusProxy, Object event);

    void afterPostSticky(EventBusProxy eventBusProxy, Object event);

    void unregister(EventBusProxy eventBusProxy, Object subscriber);
}
