package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.dao.NotificationDaoImpl;
import cn.edu.nju.cs.seg.pojo.Notification;

import java.util.List;

/**
 * Created by fwz on 2017/7/9.
 */
public class NotificationService {
    private static NotificationDaoImpl dao = new NotificationDaoImpl();

    public static void remove(int id) {
        dao.remove(id);
    }

    public static int add(Notification notification) {
        dao.add(notification);
        return notification.getId();
    }

    public static Notification update(Notification notification) {
        return dao.update(notification);
    }

    public static Notification findNotificationById(int id) {
        return dao.findNotificationById(id);
    }

    public static List<Notification> findNotificationsByUserId(int userId) {
        return dao.findNotificationsByUserId(userId);
    }

}
