package cn.edu.nju.cs.seg.schooledinapp.activity;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.widgets.AudioPlayer;
import cn.edu.nju.cs.seg.schooledinapp.widgets.RichTextView;
import cn.edu.nju.cs.seg.schooledinapp.model.Essay;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.EssaysService;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class EssayActivity extends BaseActivity {

    private static final String TAG = "EssayActivity";

    public static final String PARAM_ESSAY_ID = "PARAM_ESSAY_ID";

    private int essayId;

    private String content;

    @BindView(R.id.tb_essay_toolbar)
    Toolbar toolbar;

    @BindView(R.id.ctl_essay_toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.abl_essay_app_bar_layout)
    AppBarLayout appBar;

    @BindView(R.id.ll_essay_title)
    LinearLayout lvTitle;

    @BindView(R.id.ll_essay_praise)
    LinearLayout llEssayPraise;

    @BindView(R.id.iv_essay_praise)
    ImageView ivEssayPraise;

    @BindView(R.id.iv_essay_praise_white)
    ImageView ivEssayPraiseWhite;

    @BindView(R.id.ll_essay_collect)
    LinearLayout llEssayCollect;

    @BindView(R.id.iv_essay_collect)
    ImageView ivEssayCollect;

    @BindView(R.id.iv_essay_collect_white)
    ImageView ivEssayCollectWhite;

    @BindView(R.id.ll_essay_comment)
    LinearLayout llEssayComment;

    @BindView(R.id.civ_essay_header_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_essay_author_name)
    TextView tvAuthorName;

    @BindView(R.id.tv_essay_author_introduction)
    TextView tvAuthorIntro;

    @BindView(R.id.tv_content_essay_content)
    RichTextView tvEssayContent;

    @BindView(R.id.tv_essay_title)
    TextView tvEssayTitle;

    @BindView(R.id.tv_essay_created_at)
    TextView tvEssayCreatedAt;

    @BindView(R.id.btn_essay_play_audio)
    AudioPlayer apbPlayAudio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay);
        ButterKnife.bind(this);

        essayId = getIntent().getIntExtra(PARAM_ESSAY_ID, -1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle("文章");
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.trans));
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        initView();
       // showRequiredView();
    }

    /**
     * 初始化View
     */
    private void initView() {

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    lvTitle.setVisibility(VISIBLE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    lvTitle.setVisibility(GONE);
                } else {
                    lvTitle.setVisibility(VISIBLE);
                }
            }
        });

        fetchEssayAndInit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 启动 EssayActivity
     *
     * @param context 启动上下文
     * @param essayId 文章 ID
     */
    public static void startActivityWithParameters(Context context, int essayId) {
        Intent intent = new Intent(context, EssayActivity.class);
        intent.putExtra(PARAM_ESSAY_ID, essayId);
        context.startActivity(intent);
    }

    /**
     * 获取文章并初始化
     */
    public void fetchEssayAndInit() {
        EssaysService essaysService = ApiClient.getEssaysService();

        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(this, "正在加载文章...");

        essaysService.fetchOne(essayId).enqueue(new Callback<Essay>() {
            @Override
            public void onResponse(Call<Essay> call, Response<Essay> response) {
                progressDialog.dismiss();

                switch (response.code()) {
                    case 200: {
                        Essay essay = response.body();

                        // 设置文章信息
                        Glide.with(EssayActivity.this)
                                .load(essay.getStudioAvatarUrl())
                                .into(ivAvatar);
                        tvAuthorName.setText(essay.getStudio());
                        tvAuthorIntro.setText(essay.getStudioBio());
                        tvEssayTitle.setText(essay.getTitle());
                        if ("text".equals(essay.getType())) {
                            tvEssayContent.setHtml(essay.getContent(), 500);
                            tvEssayContent.setOnClickListener(null);
                        } else if("audio".equals(essay.getType())) {
                            tvEssayContent.setHtml(
                                    getResources().getString(R.string.content_is_audio),
                                    1);
                            tvEssayContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    apbPlayAudio.performClick();
                                }
                            });
                            apbPlayAudio.setAudioUrl(essay.getAudioUrl());
                        }
                        tvEssayCreatedAt.setText(new Date(essay.getCreatedAt()).toLocaleString());
                        // 添加监听器
                        addListener(essay);
                        addSupportListener();
                        addCollectListener();
                    }
                    break;

                    case 404: {
                        DialogUtil.showAlertDialog(EssayActivity.this, "没有找到");
                    }
                    break;

                    case 503: {
                        DialogUtil.showAlertDialog(EssayActivity.this, "服务器正忙");
                    }
                    break;
                }
            }

            @Override
            public void onFailure(Call<Essay> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 添加监听器
     *
     * @param essay 文章信息
     */
    private void addListener(final Essay essay) {

        llEssayComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EssayCommentsActivity.startActivityWithParameters(EssayActivity.this, essay.getId());
            }
        });
    }

    private void addSupportListener() {
        ivEssayPraiseWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 构建请求体
                RequestBody body = new JsonRequestBodyBuilder()
                        .append("supporter_email_or_phone", AppContext.getOnlineUser().getEmail())
                        .append("supporter_password", AppContext.getOnlineUser().getPassword())
                        .build();

                EssaysService essaysService = ApiClient.getEssaysService();
                essaysService.supportOneEssay(essayId, body).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case 201: {
                                // 点赞成功
                                Toast.makeText(AppContext.getContext(), "已点赞", Toast.LENGTH_SHORT).show();
                                ivEssayPraiseWhite.setVisibility(GONE);
                                ivEssayPraise.setVisibility(VISIBLE);
                            }
                            break;

                            case 401: {
                                DialogUtil.showAlertDialog(EssayActivity.this, "没有权限！");
                            }
                            break;

                            case 404: {
                                DialogUtil.showAlertDialog(EssayActivity.this, "没有找到！");
                            }
                            break;

                            case 503: {
                                DialogUtil.showAlertDialog(EssayActivity.this, "服务器正忙！");
                            }
                            break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        DialogUtil.showAlertDialog(EssayActivity.this, "请检查网络连接", "点赞失败");
                    }
                });
            }
        });

    }

    private void addCollectListener() {
        ivEssayCollectWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 构建请求体
                RequestBody body = new JsonRequestBodyBuilder()
                        .append("password", AppContext.getOnlineUser().getPassword())
                        .append("essay_id", essayId)
                        .build();

                UsersService usersService = ApiClient.getUsersService();
                usersService.collectAnEssay(AppContext.getOnlineUser().getId(), body).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case 201: {
                                // 收藏成功
                                Toast.makeText(AppContext.getContext(), "已收藏", Toast.LENGTH_SHORT).show();
                                ivEssayCollectWhite.setVisibility(GONE);
                                ivEssayCollect.setVisibility(VISIBLE);
                            }
                            break;

                            case 401: {
                                DialogUtil.showAlertDialog(EssayActivity.this, "没有权限！");
                            }
                            break;

                            case 404: {
                                DialogUtil.showAlertDialog(EssayActivity.this, "没有找到！");
                            }
                            break;

                            case 503: {
                                DialogUtil.showAlertDialog(EssayActivity.this, "服务器正忙！");
                            }
                            break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        DialogUtil.showAlertDialog(EssayActivity.this, "请检查网络连接", "收藏失败");
                    }
                });
            }
        });

    }

}
