package cn.edu.nju.cs.seg.schooledinapp.model;


import android.support.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.Map;

public class MainItem implements MultiItemEntity, Serializable {

    private static final String TAG = "MainItem";

    // 公共部分
    public static final class CommonKey {
        public static final String TYPE_KEY = "type";
        public static final String ID_KEY = "id";
        public static final String TITLE_KEY = "title";
        public static final String HEAT_KEY = "heat";
        public static final String SUPPORTS_KEY = "supports";
        public static final String URL_KEY = "url";
        public static final String CREATED_AT_KEY = "created_at";

        private CommonKey() { }
    }

    // QUESTION 类型
    public static final int QUESTION = 1;
    public static final String STRING_QUESTION = "question";

    public static final class QuestionKey {
        public static String ANSWERS_KEY = "answers";
        public static String DIREDTED_TO_KEY = "directed_to";
        public static String ANSWERS_URL_KEY = "answers_url";

        private QuestionKey() { }
    }

    // ESSAY 类型
    public static final int ESSAY = 2;
    public static final String STRING_ESSAY = "essay";

    public static final class EssayKey {
        public static String COMMENTS_KEY = "comments";
        public static String STUDIO_KEY = "studio";
        public static String STUDIO_URL_KEY = "studio_url";
        public static String COMMENTS_URL_KEY = "comments_url";

        private EssayKey() { }
    }

    /**
     * 创建一个 MainItem
     *
     * @param item
     * @return
     */
    public static MainItem newMainItem(@NonNull Map<String, Object> item) {
        String type = (String) item.get(CommonKey.TYPE_KEY);
        MainItem notification = null;

        if (MainItem.STRING_QUESTION.equals(type)) {
            notification = new MainItem(QUESTION, item);
        } else if (MainItem.STRING_ESSAY.equals(type)) {
            notification = new MainItem(ESSAY, item);
        }

        return notification;
    }

    // 类型
    private int itemType;

    Map<String, Object> item;

    private MainItem(int itemType, @NonNull Map<String, Object> item) {
        this.itemType = itemType;
        this.item = item;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public Object get(String key) {
        return item.get(key);
    }

    public MainItem set(String k, Object v) {
        item.put(k, v);
        return this;
    }

    public int getId() {
        return (int)(double) item.get(CommonKey.ID_KEY);
    }

    public String getType() {
        return (String) item.get(CommonKey.TYPE_KEY);
    }

}
