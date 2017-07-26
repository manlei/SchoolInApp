package cn.edu.nju.cs.seg.schooledinapp.activity;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.widgets.AudioPlayer;
import cn.edu.nju.cs.seg.schooledinapp.widgets.RichTextView;
import cn.edu.nju.cs.seg.schooledinapp.fragment.QuestionAnswerFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.Question;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioMemberItem;
import cn.edu.nju.cs.seg.schooledinapp.service.QuestionsService;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.StudiosService;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by luping on 2017/4/28.
 */

public class QuestionActivity extends BaseActivity {

    private static final String TAG = "QuestionActivity";

    public static final String PARAM_QUESTION_ID = "PARAM_QUESTION_ID";

    private static final int PUBLISH_ANSWER_REQUEST_CODE = 1;

    private int questionId;

    private Question question;

    @BindView(R.id.tb_question_toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_question_content)
    RichTextView tvContent;

    @BindView(R.id.ll_question_praise)
    LinearLayout llQuestionPraise;

    @BindView(R.id.iv_question_praise)
    ImageView ivQuestionPraise;

    @BindView(R.id.iv_question_praise_white)
    ImageView ivQuestionPraiseWhite;

    @BindView(R.id.ll_question_collect)
    LinearLayout llQuestionCollect;

    @BindView(R.id.iv_question_collect)
    ImageView ivQuestionCollect;

    @BindView(R.id.iv_question_collect_white)
    ImageView ivQuestionCollectWhite;

    @BindView(R.id.iv_question_edit_answer)
    ImageView ivQuestionEditAnswer;

    @BindView(R.id.tv_question_title)
    TextView tvQuestionTitle;

    @BindView(R.id.tv_question_created_at)
    TextView tvQuestionCreatedAt;

    @BindView(R.id.tv_question_heat)
    TextView tvQuestionHeat;

    @BindView(R.id.tv_question_supports)
    TextView tvQuestionSupports;

    @BindView(R.id.tv_question_total_answers)
    TextView tvQuestionAnswers;

    @BindView(R.id.tv_question_close)
    TextView tvQuestionClose;

    @BindView(R.id.tv_question_open)
    TextView tvQuestionOpen;

    @BindView(R.id.ll_question_text)
    LinearLayout llQuestionText;

    @BindView(R.id.rl_question_click)
    RelativeLayout rlQuestionClick;

    @BindView(R.id.tv_question_directed_to)
    TextView tvQuestionDirectedTo;

    @BindView(R.id.btn_question_play_audio)
    AudioPlayer apbPlayAudio;

    QuestionAnswerFragment questionAnswerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);

        questionId = getIntent().getIntExtra(PARAM_QUESTION_ID, -1);

        initView();

        int lineHeight = tvContent.getLineHeight();
        int measuredHeight = tvContent.getMeasuredHeight();
        int allHeight = measureTextViewHeight(tvContent.getText().toString(), 13, measuredHeight);
        DisplayMetrics dm = new DisplayMetrics();
        dm = getApplicationContext().getResources().getDisplayMetrics();
        float screenW = dm.widthPixels;
        float paddingLeft = tvContent.getPaddingLeft();
        float paddingRight = tvContent.getPaddingRight();

        int count = (int) Math
                .ceil((tvContent.getPaint().measureText(tvContent.getText().toString()) / (screenW
                        - paddingLeft - paddingRight - 159)));

        // 计算行数
        if (allHeight % lineHeight > 0) {
            count = allHeight / lineHeight + 1;
        } else {
            count = allHeight / lineHeight;
        }

        if (count > 2) {
            tvContent.setMaxLines(3);
            tvQuestionOpen.setVisibility(VISIBLE);
            tvQuestionClose.setVisibility(GONE);
        } else {
            tvContent.setMaxLines(99);
            tvQuestionOpen.setVisibility(GONE);
            tvQuestionClose.setVisibility(VISIBLE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PUBLISH_ANSWER_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    int size = questionAnswerFragment.getItemList().size();

                    questionAnswerFragment = QuestionAnswerFragment.newFragment(questionId);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.fl_question_answer, questionAnswerFragment);
                    transaction.commit();

                    tvQuestionAnswers.setText("" + (size + 1) + " 回答");
                }
            }
            break;
        }

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
     * 启动 QuestionActivity
     *
     * @param context    启动上下文
     * @param questionId 问题 ID
     */
    public static void startActivityWithParameters(Context context, int questionId) {
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(PARAM_QUESTION_ID, questionId);
        context.startActivity(intent);
    }


    /**
     * 初始化 View
     */
    private void initView() {
        // 初始化 toolbar
        toolbar.setTitle("问题");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionAnswerFragment = QuestionAnswerFragment.newFragment(questionId);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_question_answer, questionAnswerFragment);
        transaction.commit();

        addListener();
        addCollectListener();
        addPraiseListener();

        fetchQuestionAndInit();
    }


    /**
     * 获取问题并初始化
     */

    private void fetchQuestionAndInit() {
        QuestionsService questionsService = ApiClient.getQuestionsService();

        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(this, "正在加载问题...");

        questionsService.fetchOne(questionId).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                progressDialog.dismiss();

                switch (response.code()) {
                    case 200: {
                        question = response.body();

                        // 设置问题信息
                        tvQuestionTitle.setText(question.getTitle());
                        if ("text".equals(question.getType())) {
                            tvContent.setHtml(question.getContent(), 500);
                            tvContent.setOnClickListener(null);
                        } else {
                            tvContent.setHtml(
                                    getResources().getString(R.string.content_is_audio),
                                    1);
                            tvContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    apbPlayAudio.performClick();
                                }
                            });
                            apbPlayAudio.setAudioUrl(question.getAudioUrl());
                        }
                        tvQuestionCreatedAt.setText(new Date(question.getCreatedAt()).toLocaleString());
                        tvQuestionDirectedTo.setText(question.getDirectdeTo());
                        tvQuestionHeat.setText(Integer.toString(question.getHeat()) + " 热度");
                        tvQuestionSupports.setText(Integer.toString(question.getSupports()) + " 赞同");
                        tvQuestionAnswers.setText(Integer.toString(question.getAnswers()) + " 回答");

                        checkUserAndShowRequiredView();

                    }
                    break;

                    case 404: {
                        DialogUtil.showAlertDialog(QuestionActivity.this, "没有找到");
                    }
                    break;

                    case 503: {
                        DialogUtil.showAlertDialog(QuestionActivity.this, "服务器正忙");
                    }
                }

            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    /**
     * 检查用户权限并显示必要的View
     */
    private void checkUserAndShowRequiredView() {
        // 获取问题指定的工作室ID
        String[] tokens = question.getDirectedToUrl().split("/");
        int studioId = Integer.parseInt(tokens[tokens.length - 1]);

        // 获取工作室成员列表，检查当前用户是否是工作室成员，如果是，则显示编辑答案按钮
        StudiosService studiosService = ApiClient.getStudiosService();
        studiosService.fetchMembers(studioId).enqueue(new Callback<List<StudioMemberItem>>() {
            @Override
            public void onResponse(Call<List<StudioMemberItem>> call, Response<List<StudioMemberItem>> response) {
                switch (response.code()) {
                    case 200: {
                        List<StudioMemberItem> memberItems = response.body();
                        for (int i = 0; i < memberItems.size(); i++) {
                            if (memberItems.get(i).getId() == AppContext.getOnlineUser().getId()) {
                                ivQuestionEditAnswer.setVisibility(VISIBLE);
                                ivQuestionEditAnswer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (question == null) {
                                            return;
                                        }

                                        PopupMenu popupMenu = new PopupMenu(
                                                QuestionActivity.this,
                                                ivQuestionEditAnswer);

                                        popupMenu.getMenuInflater().inflate(
                                                R.menu.question_menu_select_publish_manner,
                                                popupMenu.getMenu());

                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item) {
                                                switch (item.getItemId()) {

                                                    case R.id.item_question_by_text:
                                                        PublishAnswerActivity.startActivityWithParamForResult(
                                                                QuestionActivity.this,
                                                                question.getId(),
                                                                PUBLISH_ANSWER_REQUEST_CODE
                                                        );
                                                        break;

                                                    case R.id.item_question_by_voice:
                                                        PublishAnswerByAudioActivity.startActivityWithParamForResult(
                                                                QuestionActivity.this,
                                                                question.getId(),
                                                                PUBLISH_ANSWER_REQUEST_CODE
                                                        );
                                                        break;

                                                }

                                                return true;
                                            }
                                        });

                                        popupMenu.show();

                                        return ;
                                    }
                                });
                                break;
                            }
                        }
                    }
                    break;

                    case 404: {
                        DialogUtil.showAlertDialog(QuestionActivity.this, "没有找到！");
                    }
                    break;

                    case 503: {
                        DialogUtil.showAlertDialog(QuestionActivity.this, "服务器正忙！");
                    }
                    break;
                }
            }

            @Override
            public void onFailure(Call<List<StudioMemberItem>> call, Throwable t) {
                Toast.makeText(QuestionActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * 添加监听器
     */
    private void addListener() {

        rlQuestionClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvQuestionClose.getVisibility() == VISIBLE) {
                    tvContent.setMaxLines(3);
                    tvQuestionClose.setVisibility(GONE);
                    tvQuestionOpen.setVisibility(VISIBLE);
                } else {
                    tvContent.setMaxLines(99);
                    tvQuestionOpen.setVisibility(GONE);
                    tvQuestionClose.setVisibility(VISIBLE);
                }
            }
        });

        tvQuestionDirectedTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tokens = question.getDirectedToUrl().split("/");
                int studioId = Integer.parseInt(tokens[tokens.length - 1]);
                StudioInfoActivity.startActivityWithParameters(QuestionActivity.this, studioId);
            }
        });

    }

    private void addCollectListener() {
        ivQuestionCollectWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 构建请求体
                RequestBody body = new JsonRequestBodyBuilder()
                        .append("password", AppContext.getOnlineUser().getPassword())
                        .append("question_id", questionId)
                        .build();

                UsersService usersService = ApiClient.getUsersService();
                usersService.collectAnQuestion(AppContext.getOnlineUser().getId(), body).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case 201: {
                                // 收藏成功
                                Toast.makeText(AppContext.getContext(), "已收藏", Toast.LENGTH_SHORT).show();
                                ivQuestionCollectWhite.setVisibility(GONE);
                                ivQuestionCollect.setVisibility(VISIBLE);
                            }
                            break;

                            case 401: {
                                DialogUtil.showAlertDialog(QuestionActivity.this, "没有权限！");
                            }
                            break;

                            case 404: {
                                DialogUtil.showAlertDialog(QuestionActivity.this, "没有找到！");
                            }
                            break;

                            case 503: {
                                DialogUtil.showAlertDialog(QuestionActivity.this, "服务器正忙！");
                            }
                            break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        DialogUtil.showAlertDialog(QuestionActivity.this, "请检查网络连接", "收藏失败");
                    }
                });
            }
        });
    }

    private void addPraiseListener() {
        ivQuestionPraiseWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 构建请求体
                RequestBody body = new JsonRequestBodyBuilder()
                        .append("supporter_email_or_phone", AppContext.getOnlineUser().getEmail())
                        .append("supporter_password", AppContext.getOnlineUser().getPassword())
                        .build();

                QuestionsService questionsService = ApiClient.getQuestionsService();
                questionsService.supportOneQuestion(questionId, body).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case 201: {
                                // 点赞成功
                                Toast.makeText(AppContext.getContext(), "已点赞", Toast.LENGTH_SHORT).show();
                                ivQuestionPraiseWhite.setVisibility(GONE);
                                ivQuestionPraise.setVisibility(VISIBLE);
                            }
                            break;

                            case 401: {
                                DialogUtil.showAlertDialog(QuestionActivity.this, "没有权限！");
                            }
                            break;

                            case 404: {
                                DialogUtil.showAlertDialog(QuestionActivity.this, "没有找到！");
                            }
                            break;

                            case 503: {
                                DialogUtil.showAlertDialog(QuestionActivity.this, "服务器正忙！");
                            }
                            break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        DialogUtil.showAlertDialog(QuestionActivity.this, "请检查网络连接", "点赞失败");
                    }
                });

            }
        });


        rlQuestionClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvQuestionClose.getVisibility() == View.VISIBLE) {
                    tvContent.setMaxLines(3);
                    tvQuestionClose.setVisibility(View.GONE);
                    tvQuestionOpen.setVisibility(View.VISIBLE);
                } else {
                    tvContent.setMaxLines(99);
                    tvQuestionOpen.setVisibility(View.GONE);
                    tvQuestionClose.setVisibility(View.VISIBLE);
                }
            }
        });

        tvQuestionDirectedTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tokens = question.getDirectedToUrl().split("/");
                int studioId = Integer.parseInt(tokens[tokens.length - 1]);
                StudioInfoActivity.startActivityWithParameters(QuestionActivity.this, studioId);
            }
        });

    }


    /**
     * 计算tvContent的高度
     */
    private int measureTextViewHeight(String text, int textSize, int deviceWidth) {

        TextView textView = new TextView(QuestionActivity.this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

}
