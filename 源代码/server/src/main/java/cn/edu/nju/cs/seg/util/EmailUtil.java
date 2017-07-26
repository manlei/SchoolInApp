package cn.edu.nju.cs.seg.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by fwz on 2017/5/11.
 */
public class EmailUtil {

    private static final String smtpServer = "smtp.163.com";
    private static final String user = "ffwzz666@163.com";
    private static final String password = "19960724eric";
    public static void sendEmail(String destMailAddress, String subject, String content) throws MessagingException {
        Properties props = new Properties();
        // 开启debug调试
//        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", smtpServer);
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        msg.setSubject(subject);
        // 设置邮件内容
        msg.setText(content);
        // 设置发件人
        msg.setFrom(new InternetAddress(user));

        Transport transport = session.getTransport();
        // 连接邮件服务器
        transport.connect(user, password);
        // 发送邮件
        transport.sendMessage(msg, new Address[] {new InternetAddress(destMailAddress)});
        // 关闭连接
        transport.close();
    }
}
