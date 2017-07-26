package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.interf.OnAppBarStateChangedListener;
import cn.edu.nju.cs.seg.schooledinapp.model.Studio;

public class EditStudioInfoActivity extends BaseActivity {

    private static final String TAG = "EditStudioInfoActivity";

    public static final String PARAM_STUDIO = "PARAM_STUDIO";

    // 当前工作室
    private Studio studio;

    @BindView(R.id.abl_edit_studio_info_app_bar_layout)
    AppBarLayout ablAppBarLayout;

    @BindView(R.id.ctbl_edit_studio_info_toolbar_layout)
    CollapsingToolbarLayout ctblToolbarLayout;

    @BindView(R.id.tb_edit_studio_info_toolbar)
    Toolbar tbToolbar;

    @BindView(R.id.civ_edit_studio_info_header_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_edit_studio_info_header_name)
    TextView tvHeaderName;

    @BindView(R.id.et_edit_studio_info_name)
    EditText etName;

    @BindView(R.id.et_edit_studio_info_manager)
    EditText etManager;

    @BindView(R.id.tv_edit_studio_info_save)
    TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_studio_info);
        ButterKnife.bind(this);

        studio = (Studio) getIntent().getSerializableExtra(PARAM_STUDIO);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    /**
     * 启动 EditStudioInfoActivity
     *
     * @param context 启动上下文
     * @param studio 工作室
     */
    public static void startActivityWithParameters(Context context, Studio studio) {
        Intent intent = new Intent(context, EditStudioInfoActivity.class);
        intent.putExtra(PARAM_STUDIO, studio);
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

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        // 添加监听器
        addListeners();

        // 获取工作室信息并初始化
        fetchStudioAndInit();
    }

    /**
     * 获取工作是信息并初始化 View
     *
     */
    private void fetchStudioAndInit() {
        // 设置工作室信息
        Glide.with(EditStudioInfoActivity.this)
                .load(studio.getAvatarUrl())
                .into(ivAvatar);
        tvHeaderName.setText(studio.getName());
        etName.setText(studio.getName());
        etManager.setText(studio.getManager());
    }

    /**
     * 添加事件监听器
     *
     */
    private void addListeners() {
        ablAppBarLayout.addOnOffsetChangedListener(new OnAppBarStateChangedListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                switch (state) {
                    case CLOSE_COLLAPSEC:
                        String title = studio.getName();

                        if (title == null || "".equals(title)) {
                            title = tvHeaderName.getHint().toString();
                        }

                        ctblToolbarLayout.setTitle(title);
                        ctblToolbarLayout.setTitleEnabled(true);
                        break;

                    case CLOSE_EXPANDED:
                        ctblToolbarLayout.setTitle("");
                        ctblToolbarLayout.setTitleEnabled(false);
                        break;
                }
            }
        });

        // 保存
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AppContext.getContext(), "Save", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
