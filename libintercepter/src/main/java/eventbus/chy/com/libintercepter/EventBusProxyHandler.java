package eventbus.chy.com.libintercepter;


import android.support.annotation.NonNull;

import java.util.List;

public interface EventBusProxyHandler {
    /**
     * @param event
     * @param isStick
     * @return 此处返回不能为空，该event用于事件传递，因此可对原event进行替换。
     */
    @NonNull
    Object beforePost(Object event, boolean isStick);

    void afterPost(Object event, boolean isStick);

    void invokedSubscriptions(List<EventBusSubscription> eventBusSubscription);

    void invokedSubscriptionsWhenRegister(List<EventBusSubscription> eventBusSubscription);
}