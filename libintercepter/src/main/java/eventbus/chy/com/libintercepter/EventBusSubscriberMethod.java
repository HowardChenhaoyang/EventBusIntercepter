package eventbus.chy.com.libintercepter;

import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;

public class EventBusSubscriberMethod {
    public Method method;
    public ThreadMode threadMode;
    public Class<?> eventType;
    public int priority;
    public boolean sticky;
    /**
     * Used for efficient comparison
     */
    public String methodString;
}
