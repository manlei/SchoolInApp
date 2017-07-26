package cn.edu.nju.cs.seg.schooledinapp.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.model.BusEventFilter;
import cn.edu.nju.cs.seg.schooledinapp.util.LogUtil;
import cn.jpush.android.api.JPushInterface;

public class NotificationsReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationsReceiver";

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (notificationManager == null) {
            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
            onReceiveCustomMessage(context, bundle);
        } else {
            LogUtil.e(TAG, "Unhandled intent: " + action);
        }

    }

    /**
     * 接收到 自定义消息
     *
     * @param context
     * @param bundle
     */
    private void onReceiveCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        AppContext.getBus().post(new BusEventFilter.NotificationReceivedEvent());
    }

}
