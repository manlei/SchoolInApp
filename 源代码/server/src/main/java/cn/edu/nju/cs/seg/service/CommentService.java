package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.ServerContext;
import cn.edu.nju.cs.seg.dao.CommentDaoImpl;
import cn.edu.nju.cs.seg.pojo.Comment;
import cn.edu.nju.cs.seg.pojo.Notification;
import cn.edu.nju.cs.seg.pojo.Studio;
import cn.edu.nju.cs.seg.pojo.User;
import cn.edu.nju.cs.seg.util.JPushUtil;
import cn.jpush.api.push.model.PushPayload;

import java.util.List;

/**
 * Created by Clypso on 2017/5/5.
 */
public class CommentService {
    private static CommentDaoImpl dao = new CommentDaoImpl();

    public static Comment findCommentById(int id) {
        return dao.findCommentById(id);
    }

    public static List<Comment> findAllComments() {
        return dao.findAllComments();
    }

    public static boolean remove(int id) {
        return dao.remove(id);
    }

    public static int add(Comment comment) {
        Comment c = dao.add(comment);
        if (c == null) {
            return 0;
        }
        if (ServerConfig.NOTIFICATION) {


            if (c.getTarget() == Comment.COMMENT_FOR_ANSWER) {
                Notification notification = new Notification(
                        comment.getAnswer().getAnswerer(), comment);
                NotificationService.add(notification);
                if (ServerContext.logins.containsKey(comment.getAnswer().getAnswerer().getId() + "")) {
                    PushPayload payload = JPushUtil.buildPushObject_android_alias_msg(
                            comment.getAnswer().getAnswerer().getId() + "");
                    JPushUtil.sendPush(payload);
                }
            } else if (c.getTarget() == Comment.COMMENT_FOR_ESSAY) {
                Studio studio = comment.getEssay().getStudio();
                List<User> members = StudioService.findUsersByStudioId(studio.getId());
                for (User u : members) {

                    Notification notification = new Notification(
                            u, comment);
                    NotificationService.add(notification);
                    if (ServerContext.logins.containsKey(
                            u.getId() + "")) {
                        PushPayload payload = JPushUtil.buildPushObject_android_alias_msg(
                                u.getId() + "");
                        JPushUtil.sendPush(payload);
                    }
                }
            }
        }
        return c.getId();
    }


    public static List<Comment> findCommentsByEssayId(int essayId) {
        return dao.findCommentsByEssayId(essayId);
    }

    public static List<Comment> findCommentsByAnswerId(int answerId) {
        return dao.findCommentsByAnswerId(answerId);
    }


}
