package cn.edu.nju.cs.seg.util;

import cn.jpush.api.push.model.PushPayload;
import org.junit.Test;

/**
 * Created by fwz on 2017/7/9.
 */
public class JPushUtilTests {
    @Test
    public void testSendPush() throws Exception {
        PushPayload payload = JPushUtil.buildPushObject_android_alias_msg("1");
        JPushUtil.sendPush(payload);
    }

}
