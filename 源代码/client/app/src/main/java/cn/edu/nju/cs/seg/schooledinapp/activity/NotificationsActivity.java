package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.NotificationItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.NotificationsFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.NotificationItem;
import cn.edu.nju.cs.seg.schooledinapp.util.LogUtil;

public class NotificationsActivity extends BaseActivity {

    private static final String TAG = "NotificationsActivity";
    
    @BindView(R.id.tb_notifications_toolbar)
    Toolbar tbToolbar;

    @BindView(R.id.fl_notifications_fragment_placeholder)
    FrameLayout rlFragmentPlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
        }

        return true;
    }

    /**
     * 启动 NotificationsActivity
     *
     * @param context 启动上下文
     */
    public static void startActivityWithParameters(Context context) {
        Intent intent = new Intent(context, NotificationsActivity.class);
        context.startActivity(intent);
    }

    /**
     * 初始化 View
     *
     */
    private void initView() {
        // 设置 toolbar
        setSupportActionBar(tbToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 设置 Fragment
        NotificationsFragment notificationsFragment = NotificationsFragment.newFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_notifications_fragment_placeholder, notificationsFragment);
        transaction.commit();
    }

}
