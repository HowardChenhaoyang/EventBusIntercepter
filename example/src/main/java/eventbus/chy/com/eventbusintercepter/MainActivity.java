package eventbus.chy.com.eventbusintercepter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import eventbus.chy.com.libintercepter.EventBusProxyFactory;
import eventbus.chy.com.libintercepter.EventBusProxyHandler;
import eventbus.chy.com.libintercepter.EventBusSubscription;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        EventBusManager.getEventBus().register(this);
        EventBus.getDefault().register(this);
        EventBusProxyFactory.defaultEventBusProxy(true, new EventBusProxyHandler() {
            @Override
            public Object beforePost(Object event, boolean isStick) {
                return event;
            }

            @Override
            public void afterPost(Object event, boolean isStick) {

            }

            @Override
            public void invokedSubscriptions(List<EventBusSubscription> eventBusSubscription) {

            }

            @Override
            public void invokedSubscriptionsWhenRegister(List<EventBusSubscription> eventBusSubscription) {

            }
        });
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMessage eventMessage = new EventMessage();
                eventMessage.message = "from mainActivity";
//                EventBusManager.getEventBus().post(eventMessage);
                EventBus.getDefault().post(eventMessage);
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.start(MainActivity.this);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventMessage eventMessage) {
        Log.d(TAG, " eventMessage is " + eventMessage);
        Toast.makeText(this, eventMessage.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        EventBusManager.getEventBus().unregister(this);
    }
}