package eventbus.chy.com.eventbusintercepter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventMessage eventMessage = new EventMessage();
                eventMessage.message = "from SecondActivity";
                EventBus.getDefault().post(eventMessage);
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SecondActivity.class);
        context.startActivity(starter);
    }
}
