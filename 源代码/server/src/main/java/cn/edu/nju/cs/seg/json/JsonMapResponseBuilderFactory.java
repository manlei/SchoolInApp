package cn.edu.nju.cs.seg.json;

import cn.edu.nju.cs.seg.pojo.*;

/**
 * Created by fwz on 2017/5/30.
 */
public class JsonMapResponseBuilderFactory {
    public static UserJsonMapBuilder createUserJsonMapBuilder(User user) {
        return new UserJsonMapBuilder(user);
    }

    public static QuestionJsonMapBuilder createQuestionJsonMapBuilder(Question question) {
        return new QuestionJsonMapBuilder(question);
    }

    public static AnswerJsonMapBuilder createAnswerJsonMapBuilder(Answer answer) {
        return new AnswerJsonMapBuilder(answer);
    }

    public static EssayJsonMapBuilder createEssayJsonMapBuilder(Essay essay) {

        return new EssayJsonMapBuilder(essay);
    }

    public static StudioJsonMapBuilder createStudioJsonMapBuilder(Studio studio) {
        return new StudioJsonMapBuilder(studio);
    }

    public static CommentJsonMapBuilder createCommentJsonMapBuilder(Comment comment) {
        return new CommentJsonMapBuilder(comment);
    }

    public static NotificationJsonMapBuilder createNotificationJsonMapBuilder(Notification notification) {
        return new NotificationJsonMapBuilder(notification);
    }
}
