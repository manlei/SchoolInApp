package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.fragment.AnswerCommentsFragment;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.CommentsService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnswerCommentsActivity extends BaseActivity {

    private static final String TAG = "AnswerCommentsActivity";

    public static final String PARAM_ANSWER_ID = "PARAM_ANSWER_ID";

    private int answerId;

    @BindView(R.id.tb_comment_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rl_comment_divider)
    View divider;

    @BindView(R.id.iv_comment_send_msg)
    ImageView ivSendMsg;

    @BindView(R.id.et_comment_msg)
    EditText etMsg;

    @BindView(R.id.ll_comment_msg)
    LinearLayout llMsg;

    @BindView(R.id.rl_comment_msg)
    RelativeLayout rlMsg;

    AnswerCommentsFragment answerCommentsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        answerId = getIntent().getIntExtra(PARAM_ANSWER_ID, -1);
        initView();
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
     * 启动 AnswerCommentsActivity
     *
     * @param context 启动上下文
     * @param id      ID
     */
    public static void startActivityWithParameters(Context context, int id) {
        Intent intent = new Intent(context, AnswerCommentsActivity.class);
        intent.putExtra(PARAM_ANSWER_ID, id);
        context.startActivity(intent);
    }

    /**
     * 初始化 View
     */
    private void initView() {
        // 设置toolbar
        toolbar.setTitle("评论");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        answerCommentsFragment = AnswerCommentsFragment.newFragment(answerId);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_comments, answerCommentsFragment);
        transaction.commit();

        addListener();

    }


    /**
     * 添加事件监听器
     */
    private void addListener() {

        ivSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = etMsg.getText().toString();
                String parentType = "answer";

                if (content == null || content.isEmpty()) {
                    Toast.makeText(AppContext.getContext(),
                            "请输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 构建请求体
                final RequestBody body = new JsonRequestBodyBuilder()
                        .append("commenter_email_or_phone", AppContext.getOnlineUser().getEmail())
                        .append("commenter_password", AppContext.getOnlineUser().getPassword())
                        .append("content", content)
                        .append("parent_type", parentType)
                        .append("parent_id", answerId)
                        .build();

                CommentsService commentsService = ApiClient.getCommentsService();
                commentsService.addComment(body).enqueue(new Callback<Map<String, String>>() {

                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        etMsg.setText("");
                        switch (response.code()) {
                            case 201: {
                                // 成功评论
                              //  Toast.makeText(AppContext.getContext(),
                              //          "评论成功", Toast.LENGTH_SHORT).show();
                                answerCommentsFragment.reFetchData();
                            }
                            break;

                            case 401: {
                                DialogUtil.showAlertDialog(AnswerCommentsActivity.this, "没有权限！");
                            }
                            break;

                            case 404: {
                                DialogUtil.showAlertDialog(AnswerCommentsActivity.this, "没有找到！");
                            }
                            break;

                            case 503: {
                                DialogUtil.showAlertDialog(AnswerCommentsActivity.this, "服务器正忙！");
                            }
                            break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        DialogUtil.showAlertDialog(AnswerCommentsActivity.this,
                                "请检查网络连接", "评论错误");
                    }
                });

            }
        });
    }

}