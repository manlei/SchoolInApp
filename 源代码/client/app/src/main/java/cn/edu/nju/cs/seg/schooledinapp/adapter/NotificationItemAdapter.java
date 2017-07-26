package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.model.NotificationItem;

public class NotificationItemAdapter
        extends BaseMultiItemQuickAdapter<NotificationItem, BaseViewHolder> {

    private Context context;

    private static final String TAG = "NotificationItemAdapter";

    public NotificationItemAdapter(Context context, List<NotificationItem> itemList) {
        super(itemList);
        addItemType(NotificationItem.INVITED, R.layout.notifications_item_invited);
        addItemType(NotificationItem.ANSWERED, R.layout.notifications_item_answered);
        addItemType(NotificationItem.COMMENTED, R.layout.notifications_item_commented);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NotificationItem item) {

        switch (helper.getItemViewType()) {

            case NotificationItem.INVITED: {

                helper.setText(R.id.tv_notifications_invited_asker_name,
                        (String) item.get(NotificationItem.InvitedKey.ASKER_NAME));
                helper.setText(R.id.tv_notifications_invited_question_title,
                        (String) item.get(NotificationItem.InvitedKey.QUESTION_TITLE));

                if (item.hasRead()) {
                    helper.setBackgroundColor(R.id.ll_notifications_invited_layout,
                            ContextCompat.getColor(context, R.color.white));
                } else {
                    helper.setBackgroundColor(R.id.ll_notifications_invited_layout,
                            ContextCompat.getColor(context, R.color.aliceBlue));
                }

            } break;

            case NotificationItem.ANSWERED: {

                helper.setText(R.id.tv_notifications_answered_answerer_name,
                        (String) item.get(NotificationItem.AnsweredKey.ANSWERER_NAME));
                helper.setText(R.id.tv_notifications_answered_question_title,
                        (String) item.get(NotificationItem.AnsweredKey.QUESTION_TITLE));

                if (item.hasRead()) {
                    helper.setBackgroundColor(R.id.ll_notifications_answered_layout,
                            ContextCompat.getColor(context, R.color.white));
                } else {
                    helper.setBackgroundColor(R.id.ll_notifications_answered_layout,
                            ContextCompat.getColor(context, R.color.aliceBlue));
                }

            } break;

            case NotificationItem.COMMENTED: {
                if (item.hasRead()) {
                    helper.setBackgroundColor(R.id.ll_notifications_commented_layout,
                            ContextCompat.getColor(context, R.color.white));
                } else {
                    helper.setBackgroundColor(R.id.ll_notifications_commented_layout,
                            ContextCompat.getColor(context, R.color.aliceBlue));
                }
            } break;

        }

    }

}
