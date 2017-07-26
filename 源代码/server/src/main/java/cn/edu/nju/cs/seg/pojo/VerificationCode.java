package cn.edu.nju.cs.seg.pojo;

import javax.persistence.*;

/**
 * Created by fwz on 2017/5/14.
 */
@Entity
@Table(name = "verification_code")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String emailOrPhone;

    private String code;

    private long retainUtil;

    public VerificationCode() {
    }

    public VerificationCode(String emailOrPhone, String code, long retainUtil) {
        this.emailOrPhone = emailOrPhone;
        this.code = code;
        this.retainUtil = retainUtil;
    }

    public int getId() {
        return id;
    }


    public String getEmailOrPhone() {
        return emailOrPhone;
    }

    public void setEmailOrPhone(String emailOrPhone) {
        this.emailOrPhone = emailOrPhone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getRetainUtil() {
        return retainUtil;
    }

    public void setRetainUtil(long retainUtil) {
        this.retainUtil = retainUtil;
    }
}
