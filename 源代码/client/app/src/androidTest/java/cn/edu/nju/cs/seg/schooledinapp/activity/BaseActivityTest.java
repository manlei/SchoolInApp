package cn.edu.nju.cs.seg.schooledinapp.activity;


import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import cn.edu.nju.cs.seg.schooledinapp.AppConfig;

@RunWith(AndroidJUnit4.class)
public class BaseActivityTest {

    public void waitUntilConnectionEnd() throws Exception {
        // 为了防止测试代码侵入，我们这里利用 sleep 来等待网络，而不是利用 idling resource
        Thread.sleep(AppConfig.CONNECTION_TIME_OUT_IN_MILLISECONDS + 100);
    }

}
