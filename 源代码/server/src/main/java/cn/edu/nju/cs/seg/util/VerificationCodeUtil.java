package cn.edu.nju.cs.seg.util;

import java.util.Random;

/**
 * Created by fwz on 2017/5/12.
 */
public class VerificationCodeUtil {
    public static String getVerificationCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += random.nextInt(10);
        }
        return code;
    }
}
