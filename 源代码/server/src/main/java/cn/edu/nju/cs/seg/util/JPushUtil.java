package cn.edu.nju.cs.seg.util;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;

/**
 * Created by fwz on 2017/7/9.
 */
public class JPushUtil {

    public static PushPayload buildPushObject_android_alias_msg(String alias) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.alias(alias))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent("new message")
                        .build())
                .build();
    }

    public static void sendPush(PushPayload payload) {
        PushRunnable push = new PushRunnable(payload);
        new Thread(push).start();
    }

    private static class PushRunnable implements Runnable {
        PushPayload pushPayload;

        public PushRunnable(PushPayload pushPayload) {
            this.pushPayload = pushPayload;
        }

        @Override
        public void run() {
            try {
                JPushClient jpushClient =
                        new JPushClient(ServerConfig.MASTER_SECRET,
                                ServerConfig.APP_KEY, null, ClientConfig.getInstance());
                PushResult result = jpushClient.sendPush(pushPayload);
                System.out.println("Got result - " + result);

            } catch (APIConnectionException e) {
                // Connection error, should retry later
                System.out.println("JPush: Connection error, should retry later");

            } catch (APIRequestException e) {
                // Should review the error, and fix the request
                System.out.println("JPush: Should review the error, and fix the request");
            }
        }

    }

}
