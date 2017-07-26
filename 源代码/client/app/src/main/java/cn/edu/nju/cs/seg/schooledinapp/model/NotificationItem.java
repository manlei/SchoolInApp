package cn.edu.nju.cs.seg.schooledinapp.model;


import android.support.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NotificationItem implements MultiItemEntity, Serializable {

    private static final String TAG = "NotificationItem";

    // 公共部分
    public static final class CommonKey {
        public static final String TYPE_KEY = "type";
        public static final String ID_KEY = "id";
        public static final String HAS_READ_KEY = "has_read";

        private CommonKey() { }
    }

    // INVITED 类型
    public static final int INVITED = 1;
    public static final String STRING_INVITED = "invited";

    public static final class InvitedKey {
        public static String ASKER_NAME = "asker_name";
        public static String ASKER_ID = "asker_id";
        public static String ASKER_URL = "asker_url";
        public static String QUESTION_TITLE = "question_title";
        public static String QUESTION_ID = "question_id";
        public static String QUESTION_URL = "question_url";

        private InvitedKey() { }
    }

    /**
     * 工厂方法：创建一个 InvitedNotificationItem
     *
     * @param item
     * @return
     */
    public static NotificationItem InvitedNotificationItem(@NonNull Map<String, Object> item) {
        NotificationItem n = new NotificationItem(INVITED, item);

        String askerName = (String) item.get(InvitedKey.ASKER_NAME);
        n.set(InvitedKey.ASKER_NAME, askerName);

        String askerUrl = (String) item.get(InvitedKey.ASKER_URL);
        n.set(InvitedKey.ASKER_URL, askerUrl);

        String[] tokens = askerUrl.split("/");
        int askerId = Integer.parseInt( tokens[tokens.length - 1] );
        n.set(InvitedKey.ASKER_ID, askerId);

        String questionTitle = (String) item.get(InvitedKey.QUESTION_TITLE);
        n.set(InvitedKey.QUESTION_TITLE, questionTitle);

        String questionUrl = (String) item.get(InvitedKey.QUESTION_URL);
        n.set(InvitedKey.QUESTION_URL, questionUrl);

        tokens = questionUrl.split("/");
        int questionId = Integer.parseInt( tokens[tokens.length - 1] );
        n.set(InvitedKey.QUESTION_ID, questionId);

        return n;
    }

    // ANSWERED 类型
    public static final int ANSWERED = 2;
    public static final String STRING_ANSWERED = "answered";

    public static final class AnsweredKey {
        public static final String ANSWERER_NAME = "answerer_name";
        public static final String ANSWERER_URL = "answerer_url";
        public static final String ANSWERER_ID = "answerer_id";
        public static final String QUESTION_TITLE = "question_title";
        public static final String QUESTION_URL = "question_url";
        public static final String QUESTION_ID = "question_id";

        private AnsweredKey() { }
    }

    /**
     * 工厂方法：创建一个 AnsweredNotificationItem
     *
     * @param item
     * @return
     */
    public static NotificationItem AnsweredNotificationItem(@NonNull Map<String, Object> item) {
        NotificationItem n = new NotificationItem(ANSWERED, item);

        String answererName = (String) item.get(AnsweredKey.ANSWERER_NAME);
        n.set(AnsweredKey.ANSWERER_NAME, answererName);

        String answererUrl = (String) item.get(AnsweredKey.ANSWERER_URL);
        n.set(AnsweredKey.ANSWERER_URL, answererUrl);

        String[] tokens = answererUrl.split("/");
        int answererId = Integer.parseInt( tokens[tokens.length - 1] );
        n.set(AnsweredKey.ANSWERER_ID, answererId);

        String questionTitle = (String) item.get(AnsweredKey.QUESTION_TITLE);
        n.set(AnsweredKey.QUESTION_TITLE, questionTitle);

        String questionUrl = (String) item.get(AnsweredKey.QUESTION_URL);
        n.set(AnsweredKey.QUESTION_URL, questionUrl);

        tokens = questionUrl.split("/");
        int questionId = Integer.parseInt( tokens[tokens.length - 1] );
        n.set(AnsweredKey.QUESTION_ID, questionId);

        return n;
    }

    // COMMENTED 类型
    public static final int COMMENTED = 3;
    public static final String STRING_COMMENTED = "commented";

    public static final class CommentedKey {
        public static final String PARENT_TYPE = "parent_type";
        public static final String PARENT_URL = "parent_url";
        public static final String PARENT_ID = "parent_id";

        private CommentedKey() { }
    }

    /**
     * 工厂方法：创建一个 CommentedNotificationItem
     *
     * @param item
     * @return
     */
    public static NotificationItem CommentedNotificationItem(@NonNull Map<String, Object> item) {
        NotificationItem n = new NotificationItem(COMMENTED, item);

        String parentType = (String) item.get(CommentedKey.PARENT_TYPE);
        n.set(CommentedKey.PARENT_TYPE, parentType);

        String parentUrl = (String) item.get(CommentedKey.PARENT_URL);
        n.set(CommentedKey.PARENT_URL, parentUrl);

        String[] tokens = parentUrl.split("/");
        int parentId = Integer.parseInt( tokens[tokens.length - 1] );
        n.set(CommentedKey.PARENT_ID, parentId);

        return n;
    }

    /**
     * 创建一个 NotificationItem
     *
     * @param item
     * @return
     */
    public static NotificationItem newNotificationItem(@NonNull Map<String, Object> item) {
        String type = (String) item.get(CommonKey.TYPE_KEY);
        NotificationItem notification = null;

        if (NotificationItem.STRING_INVITED.equals(type)) {
            notification = NotificationItem.InvitedNotificationItem(item);
        } else if (NotificationItem.STRING_ANSWERED.equals(type)) {
            notification = NotificationItem.AnsweredNotificationItem(item);
        } else if (NotificationItem.STRING_COMMENTED.equals(type)) {
            notification = NotificationItem.CommentedNotificationItem(item);
        }

        return notification;
    }

    // 类型
    private int itemType;

    Map<String, Object> item = new HashMap<> ();

    private NotificationItem(int itemType, @NonNull Map<String, Object> item) {
        this.itemType = itemType;

        String type = (String) item.get(CommonKey.TYPE_KEY);
        this.item.put(CommonKey.TYPE_KEY, type);

        boolean hasRead = (boolean) item.get(CommonKey.HAS_READ_KEY);
        this.item.put(CommonKey.HAS_READ_KEY, hasRead);

        // gson 默认将 Number 转换为 double，因此这里需要两次强制转换
        int id = (int)(double) item.get(CommonKey.ID_KEY);
        this.item.put(CommonKey.ID_KEY, id);
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public Object get(String key) {
        return item.get(key);
    }

    public NotificationItem set(String k, Object v) {
        item.put(k, v);
        return this;
    }

    public int getId() {
        return (int) item.get(CommonKey.ID_KEY);
    }

    public String getType() {
        return (String) item.get(CommonKey.TYPE_KEY);
    }

    public boolean hasRead() {
        return (Boolean) item.get(CommonKey.HAS_READ_KEY);
    }

    public void setRead(boolean read) {
        this.item.put(CommonKey.HAS_READ_KEY, read);
    }

}
