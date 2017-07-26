package cn.edu.nju.cs.seg.schooledinapp.activity;


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
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.widgets.AudioPlayer;
import cn.edu.nju.cs.seg.schooledinapp.widgets.RichTextView;
import cn.edu.nju.cs.seg.schooledinapp.model.Answer;
import cn.edu.nju.cs.seg.schooledinapp.service.AnswersService;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class AnswerActivity extends BaseActivity {

    private static final String TAG = "AnswerActivity";

    public static final String PARAM_ANSWER_ID = "PARAM_ANSWER_ID";

    //当前回答ID
    private int answerId;

    Answer mAnswer;

    @BindView(R.id.tb_answer_toolbar)
    Toolbar toolbar;

    @BindView(R.id.ctbl_answer_toolbar_layout)
    CollapsingToolbarLayout ctblToolbarLayout;

    @BindView(R.id.abl_answer_app_bar_layout)
    AppBarLayout appBar;

    @BindView(R.id.ll_answer_collect)
    LinearLayout llAnswerCollect;

    @BindView(R.id.iv_answer_collect)
    ImageView ivAnswerCollect;

    @BindView(R.id.iv_answer_collect_white)
    ImageView ivAnswerCollectWhite;

    @BindView(R.id.ll_answer_comment)
    LinearLayout llAnswerComment;

    @BindView(R.id.ll_answer_question_title)
    LinearLayout lvTitle;

    @BindView(R.id.civ_answer_header_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_answer_author_name)
    TextView tvAuthorName;

    @BindView(R.id.tv_answer_author_introduction)
    TextView tvAuthorIntro;

    @BindView(R.id.tv_content_answer_content)
    RichTextView tvAnswerContent;

    @BindView(R.id.tv_answer_question_title)
    TextView tvAnswerQuestionTitle;

    @BindView(R.id.tv_answer_created_at)
    TextView tvAnswerCreatedAt;

    @BindView(R.id.btn_answer_play_audio)
    AudioPlayer apbPlayAudio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.bind(this);

        answerId = getIntent().getIntExtra(PARAM_ANSWER_ID, -
                1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctblToolbarLayout.setTitle("回答");
        ctblToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.trans));
        ctblToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

        initView();
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

        fetchAnswerAndInit();
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
     * 启动 AnswerActivity
     *
     * @param context  启动上下文
     * @param answerId 回答 ID
     */
    public static void startActivityWithParameters(Context context, int answerId) {
        Intent intent = new Intent(context, AnswerActivity.class);
        intent.putExtra(PARAM_ANSWER_ID, answerId);
        context.startActivity(intent);
    }

    /**
     * 获取答案并初始化
     */
    public void fetchAnswerAndInit() {
        AnswersService answersService = ApiClient.getAnswersService();

        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(this, "正在加载答案...");

        answersService.fetchOne(answerId).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                progressDialog.dismiss();

                switch (response.code()) {
                    case 200: {
                        Answer answer = response.body();

                        // 设置答案信息
                        Glide.with(AnswerActivity.this)
                                .load(answer.getAnswererAvatarUrl())
                                .into(ivAvatar);
                        tvAuthorName.setText(answer.getAnswerer());
                        tvAuthorIntro.setText(answer.getAnswererBio());
                        tvAnswerQuestionTitle.setText(answer.getQuestionTitle());
                        if ("text".equals(answer.getType())) {
                            tvAnswerContent.setHtml(answer.getContent(), 500);
                            tvAnswerContent.setOnClickListener(null);
                        } else if("audio".equals(answer.getType())) {
                            tvAnswerContent.setHtml(
                                    getResources().getString(R.string.content_is_audio),
                                    1);
                            tvAnswerContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    apbPlayAudio.performClick();
                                }
                            });
                            apbPlayAudio.setAudioUrl(answer.getAudioUrl());
                        }
                        tvAnswerCreatedAt.setText(new Date(answer.getCreatedAt()).toLocaleString());

                        // 添加监听器
                        addListener(answer);
                        addCollectListener();
                    }
                    break;

                    case 404: {
                        DialogUtil.showAlertDialog(
                                AnswerActivity.this, R.string.error_message_404);
                    }
                    break;

                    case 503: {
                        DialogUtil.showAlertDialog(
                                AnswerActivity.this, R.string.error_message_503);
                    }
                    break;
                }
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 添加监听器
     *
     * @param answer 答案信息
     */
    private void addListener(final Answer answer) {
        llAnswerComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnswerCommentsActivity.startActivityWithParameters(AnswerActivity.this, answer.getId());
            }
        });
    }

    private void addCollectListener() {
        ivAnswerCollectWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 构建请求体
                RequestBody body = new JsonRequestBodyBuilder()
                        .append("password", AppContext.getOnlineUser().getPassword())
                        .append("answer_id", answerId)
                        .build();

                UsersService usersService = ApiClient.getUsersService();
                usersService.collectAnAnswer(AppContext.getOnlineUser().getId(), body).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case 201: {
                                // 收藏成功
                                Toast.makeText(AppContext.getContext(), "已收藏", Toast.LENGTH_SHORT).show();
                                ivAnswerCollectWhite.setVisibility(GONE);
                                ivAnswerCollect.setVisibility(VISIBLE);
                            }
                            break;

                            case 401: {
                                DialogUtil.showAlertDialog(AnswerActivity.this, "没有权限！");
                            }
                            break;

                            case 404: {
                                DialogUtil.showAlertDialog(AnswerActivity.this, "没有找到！");
                            }
                            break;

                            case 503: {
                                DialogUtil.showAlertDialog(AnswerActivity.this, "服务器正忙！");
                            }
                            break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        DialogUtil.showAlertDialog(AnswerActivity.this, "请检查网络连接", "收藏失败");
                    }
                });
            }
        });

    }

}