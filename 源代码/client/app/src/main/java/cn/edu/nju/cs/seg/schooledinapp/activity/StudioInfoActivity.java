package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.interf.OnAppBarStateChangedListener;
import cn.edu.nju.cs.seg.schooledinapp.model.Studio;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.StudiosService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudioInfoActivity extends BaseActivity {

    private static final String TAG = "StudioInfoActivity";

    public static final String PARAM_STUDIO_ID = "PARAM_STUDIO_ID";

    private static final int REQUEST_CODE_STUDIO_MEMBERS = 1;

    // 当前工作室ID
    private int studioId;

    // 当前工作室
    private Studio studio;

    // 管理员 ID
    private int managerId;

    private boolean updateWhenResume = false;

    @BindView(R.id.abl_studio_info_app_bar_layout)
    AppBarLayout ablAppBarLayout;

    @BindView(R.id.ctbl_studio_info_toolbar_layout)
    CollapsingToolbarLayout ctblToolbarLayout;

    @BindView(R.id.tb_studio_info_toolbar)
    Toolbar tbToolbar;

    @BindView(R.id.civ_studio_info_header_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_studio_info_header_name)
    TextView tvHeaderName;

    @BindView(R.id.tv_studio_info_name)
    TextView tvName;

    @BindView(R.id.tv_studio_info_manager)
    TextView tvManager;

    @BindView(R.id.tv_studio_info_members)
    TextView tvMembers;

    @BindView(R.id.tv_studio_info_questions)
    TextView tvQuestions;

    @BindView(R.id.tv_studio_info_essays)
    TextView tvEssays;

    @BindView(R.id.ll_studio_info_members)
    LinearLayout llMemebers;

    @BindView(R.id.ll_studio_info_questions)
    LinearLayout llQuestions;

    @BindView(R.id.ll_studio_info_essays)
    LinearLayout llEssays;

    @BindView(R.id.fab_studio_info_fab_menu)
    FloatingActionsMenu fabFabMenu;

    @BindView(R.id.fab_studio_info_fab_by_text)
    FloatingActionButton fabByText;

    @BindView(R.id.fab_studio_info_fab_by_voice)
    FloatingActionButton fabByVoice;

    @BindView(R.id.tv_studio_info_edit)
    TextView tvEdit;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_info);
        ButterKnife.bind(this);

        studioId = getIntent().getIntExtra(PARAM_STUDIO_ID, -1);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (updateWhenResume) {
            fetchStudioAndInit();
            updateWhenResume = false;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_STUDIO_MEMBERS && resultCode == RESULT_OK) {
            updateWhenResume = true;
        }

    }

    /**
     * 启动 StudioInfoActivity
     *
     * @param context 启动上下文
     * @param studioId 工作室 ID
     */
    public static void startActivityWithParameters(Context context, int studioId) {
        Intent intent = new Intent(context, StudioInfoActivity.class);
        intent.putExtra(PARAM_STUDIO_ID, studioId);
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
     * 检查管理员并且显示必要的 View
     *
     */
    private void checkManagerAndShowRequiredViews() {
        if (studio == null) {
            return ;
        }

        // 获取管理员 ID
        String[] tokens = studio.getManagerUrl().split("/");
        int managerId = Integer.parseInt(tokens[tokens.length - 1]);

        // 当前用户是管理员
        if (AppContext.getOnlineUser().getId() == managerId) {
            this.managerId = managerId;

            // 显示 编辑 按钮
            tvEdit.setVisibility(View.VISIBLE);
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditStudioInfoActivity.startActivityWithParameters(
                            StudioInfoActivity.this, studio);
                }
            });

            // 显示 发表文章 按钮
            fabFabMenu.setVisibility(View.VISIBLE);

            fabByText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (studio == null) {
                        return;
                    }

                    PublishEssayActivity.startActivityWithParameters(StudioInfoActivity.this,
                            studio.getName());
                }
            });

            fabByVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (studio == null) {
                        return;
                    }

                    PublishEssayByAudioActivity.startActivityWithParameters(StudioInfoActivity.this,
                            studio.getName());
                }
            });
        } else {
            // 隐藏
            tvEdit.setVisibility(View.GONE);
            fabFabMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新
     *
     */
    private void updateView() {
        if (studio == null) {
            return ;
        }

        // 设置工作室信息
        Glide.with(StudioInfoActivity.this)
                .load(studio.getAvatarUrl())
                .into(ivAvatar);
        tvHeaderName.setText(studio.getName());
        tvName.setText(studio.getName());
        tvManager.setText(studio.getManager());
        tvQuestions.setText(Integer.toString(studio.getQuestions()));
        tvEssays.setText(Integer.toString(studio.getEssays()));
        tvMembers.setText(Integer.toString(studio.getMembers()));
    }

    /**
     * 获取工作是信息并初始化 View
     *
     */
    private void fetchStudioAndInit() {
        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(this);

        StudiosService studiosService = ApiClient.getStudiosService();

        studiosService.fetchStudio(studioId).enqueue(new Callback<Studio>() {
            @Override
            public void onResponse(Call<Studio> call, Response<Studio> response) {
                progressDialog.dismiss();
                switch (response.code()) {
                    case 200: {
                        studio = response.body();

                        // 检查管理员并且显示必要的 View
                        checkManagerAndShowRequiredViews();

                        // 刷新
                        updateView();
                    } break;

                    case 404: {
                        DialogUtil.showAlertDialog(
                                StudioInfoActivity.this, R.string.error_message_404);
                    } break;

                    case 503: {
                        DialogUtil.showAlertDialog(
                                StudioInfoActivity.this, R.string.error_message_503);
                    } break;
                }
            }

            @Override
            public void onFailure(Call<Studio> call, Throwable t) {
                progressDialog.dismiss();
                DialogUtil.showAlertDialog(StudioInfoActivity.this,
                        R.string.error_message_network);
            }
        });
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

        llQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studio == null) {
                    return ;
                }

                StudioQuestionsActivity.startActivityWithParameters(
                        StudioInfoActivity.this, studio.getId());
            }
        });

        llEssays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studio == null) {
                    return ;
                }

                StudioEssaysActivity.startActivityWithParameters(
                        StudioInfoActivity.this, studio.getId());
            }
        });

        llMemebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studio == null) {
                    return ;
                }

                StudioMembersActivity.startActivityWithParametersForResult(
                        StudioInfoActivity.this,
                        REQUEST_CODE_STUDIO_MEMBERS,
                        studio.getId(),
                        StudioInfoActivity.this.managerId);
            }
        });
    }

}
