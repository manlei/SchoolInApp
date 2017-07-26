package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Notification;

import java.util.List;

/**
 * Created by Clypso on 2017/6/24.
 */
public interface NotificationDao {

    public void remove(int id);

    public Notification add(Notification notification);

    public Notification update(Notification notification);

    public Notification findNotificationById(int id);

    public List<Notification> findNotificationsByUserId(int userId);

}
