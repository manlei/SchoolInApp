package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.VerificationCode;

/**
 * Created by fwz on 2017/5/14.
 */
public interface VerificationCodeDao {
    public void add(VerificationCode code);

    public VerificationCode getCodeByEmailOrPhone(String emailOrPhone);

    public void remove(String emailOrPhone);

    public void update(VerificationCode code);
}
