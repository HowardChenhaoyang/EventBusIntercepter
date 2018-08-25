package eventbus.chy.com.libintercepter;


import java.util.List;

public interface EventBusProxyHandler {
    Object beforePost(Object event, boolean isStick);

    void afterPost(Object event, boolean isStick);

    void invokedSubscriptions(List<EventBusSubscription> eventBusSubscription);

    void invokedSubscriptionsWhenRegister(List<EventBusSubscription> eventBusSubscription);
}