package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.AnswerCommentsActivity;
import cn.edu.nju.cs.seg.schooledinapp.activity.EssayCommentsActivity;
import cn.edu.nju.cs.seg.schooledinapp.activity.QuestionActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.NotificationItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseRecyclerViewFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.NotificationItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationsFragment extends BaseRecyclerViewFragment<NotificationItem>
        implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {

    private static final String TAG = "NotificationsFragment";

    // Header View
    LinearLayout headerView;

    TextView tvMarkAll;

    @Override
    protected BaseQuickAdapter onCreateAdapter() {
        initHeaderView();

        BaseQuickAdapter adapter = new NotificationItemAdapter(getActivity(), null);
        adapter.setOnItemClickListener(this);
        adapter.addHeaderView(headerView);

        return adapter;
    }

    @Override
    protected void onRefresh(BaseQuickAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        // 发送请求
        UsersService usersService = ApiClient.getUsersService();

        usersService.fetchNotifications(AppContext.getOnlineUser().getId())
                .enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {

                switch (response.code()) {

                    case 200: {
                        List<Map<String, Object>> body = response.body();

                        List<NotificationItem> notificationItems = new ArrayList<NotificationItem>();
                        NotificationItem notification;

                        for (Map<String, Object> item : body) {
                            notification = NotificationItem.newNotificationItem(item);

                            if (notification != null) {
                                notificationItems.add(notification);
                            }
                        }

                        if (notificationItems.size() != 0) {
                            setUpRecyclerView(notificationItems);
                        }

                        refreshEnd();
                    } break;

                    case 404: {
                        refreshEnd();
                        DialogUtil.showAlertDialog(getActivity(), R.string.error_message_404);
                    } break;

                    case 503: {
                        refreshEnd();
                        DialogUtil.showAlertDialog(getActivity(), R.string.error_message_503);
                    } break;

                }

            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                refreshEnd();
                DialogUtil.showAlertDialog(getActivity(), R.string.error_message_network);
            }
        });
        
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<NotificationItem> itemList = getItemList();
        NotificationItem item =  itemList.get(position);

        // 设置已读
        if (!item.hasRead()) {
            item.setRead(true);
            requestReadNotification(item.getId());
        }

        switch (item.getItemType()) {
            case NotificationItem.INVITED: {
                onInvitedItemClick(item);
            } break;

            case NotificationItem.ANSWERED: {
                onAnsweredItemClick(item);
            } break;

            case NotificationItem.COMMENTED: {
                onCommentedItemClick(item);
            } break;
        }


        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_notifications_mark_all: {
                onMarkAllClick();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 创建一个 NotificationsFragment
     *
     * @return NotificationsFragment
     */
    public static NotificationsFragment newFragment() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    /**
     * MarkAll 按钮被点击
     *
     */
    private void onMarkAllClick() {

        List<NotificationItem> itemList = getItemList();
        List<Integer> notificationsId = new ArrayList<>();
        for (NotificationItem item : itemList) {
            item.setRead(true);
            notificationsId.add(item.getId());
        }
        requestReadNotifications(notificationsId);
        getAdapter().notifyDataSetChanged();

    }

    /**
     * INVITED 通知 被点击
     *
     * @param item
     */
    private void onInvitedItemClick(NotificationItem item) {
        int questionId =  (int) item.get(NotificationItem.InvitedKey.QUESTION_ID);
        QuestionActivity.startActivityWithParameters(getActivity(), questionId);
    }

    /**
     * ANSWERED 通知 被点击
     *
     * @param item
     */
    private void onAnsweredItemClick(NotificationItem item) {
        int questionId =  (int) item.get(NotificationItem.AnsweredKey.QUESTION_ID);
        QuestionActivity.startActivityWithParameters(getActivity(), questionId);
    }

    /**
     * COMMENTED 通知 被点击
     *
     * @param item
     */
    private void onCommentedItemClick(NotificationItem item) {
        int id =  (int) item.get(NotificationItem.CommentedKey.PARENT_ID);
        String type = (String) item.get(NotificationItem.CommentedKey.PARENT_TYPE);

        if ("essay".equals(type)) {
            EssayCommentsActivity.startActivityWithParameters(getActivity(), id);
        } else if ("answer".equals(type)) {
            AnswerCommentsActivity.startActivityWithParameters(getActivity(), id);
        }
    }

    /**
     * 发送已读通知
     *
     * @param notificationId
     */
    private void requestReadNotification(int notificationId) {
        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("password", AppContext.getOnlineUser().getPassword())
                .build();

        // 发送
        UsersService usersService = ApiClient.getUsersService();
        usersService.setReadNotification(AppContext.getOnlineUser().getId(), notificationId, body)
                .enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) { }

                @Override
                public void onFailure(Call<Object> call, Throwable t) { }
            });
    }

    /**
     * 发送已读通知
     *
     * @param notificationsId
     */
    private void requestReadNotifications(List<Integer> notificationsId) {
        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("password", AppContext.getOnlineUser().getPassword())
                .append("notifications_id", notificationsId)
                .build();

        // 发送
        UsersService usersService = ApiClient.getUsersService();
        usersService.setReadNotifications(AppContext.getOnlineUser().getId(), body)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) { }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) { }
                });
    }

    /**
     * 初始化 Header View
     *
     */
    private void initHeaderView() {

        headerView = (LinearLayout) getActivity().getLayoutInflater().inflate(
                R.layout.header_notifications_rv_header,
                (ViewGroup) getRecyclerView().getParent(),
                false);

        tvMarkAll = (TextView) headerView.findViewById(R.id.tv_notifications_mark_all);
        tvMarkAll.setOnClickListener(this);

    }

}
