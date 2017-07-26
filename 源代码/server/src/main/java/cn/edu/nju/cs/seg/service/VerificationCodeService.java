package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.dao.VerificationCodeDaoImpl;
import cn.edu.nju.cs.seg.pojo.VerificationCode;

/**
 * Created by fwz on 2017/5/14.
 */
public class VerificationCodeService {
    private static VerificationCodeDaoImpl dao = new VerificationCodeDaoImpl();
    public static void add(VerificationCode code) {
        dao.add(code);
    }

    public static VerificationCode findCodeByEmailOrPhone(String emailOrPhone) {
        return dao.getCodeByEmailOrPhone(emailOrPhone);
    }

    public static void remove(String emailOrPhone) {
        dao.remove(emailOrPhone);
    }

    public static void update(VerificationCode code) {
        dao.update(code);
    }
}
