package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.PublishAudioActivity;
import cn.edu.nju.cs.seg.schooledinapp.service.AnswersService;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PublishAnswerByAudioActivity extends PublishAudioActivity {

    public static final String PARAM_QUESTION_ID = "PARAM_QUESTION_ID";

    private int questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionId = getIntent().getIntExtra(PARAM_QUESTION_ID, -1);
    }

    @Override
    protected void publish(String recordFilePath) {
        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("answerer_email_or_phone", AppContext.getOnlineUser().getEmail())
                .append("answerer_password", AppContext.getOnlineUser().getPassword())
                .append("question_id", questionId)
                .append("type", "audio")
                .build();

        File originFile = new File(recordFilePath);
        RequestBody filePart = RequestBody.create(MediaType.parse("audio/*"), originFile);
        MultipartBody.Part file = MultipartBody.Part.createFormData("audio", originFile.getName(), filePart);

        AnswersService answersService = ApiClient.getAnswersService();
        answersService.addAnswer(body, null, Arrays.asList(file)).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                switch (response.code()) {
                    case 201: {
                        // 成功发布
                        Toast.makeText(AppContext.getContext(),
                                "发布成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                    }
                    break;

                    case 401: {
                        DialogUtil.showAlertDialog(PublishAnswerByAudioActivity.this, "没有权限！");
                        setResult(RESULT_CANCELED);
                    }
                    break;

                    case 404: {
                        DialogUtil.showAlertDialog(PublishAnswerByAudioActivity.this, "没有找到！");
                        setResult(RESULT_CANCELED);
                    }
                    break;

                    case 503: {
                        DialogUtil.showAlertDialog(PublishAnswerByAudioActivity.this, "服务器正忙！");
                        setResult(RESULT_CANCELED);
                    }
                    break;
                }

                finish();
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                DialogUtil.showAlertDialog(PublishAnswerByAudioActivity.this,
                        "请检查网络连接", "发布错误");
            }
        });

    }

    @Override
    protected int getTitleResourceId() {
        return R.string.manifest_label_answer;
    }

    /**
     * 启动 PublishAnswerByAudioActivity
     *
     * @param activity 启动该页面的上下文
     * @param id       id
     */
    public static void startActivityWithParamForResult(
            BaseActivity activity, int id, int requestId) {
        Intent intent = new Intent(activity, PublishAnswerByAudioActivity.class);
        intent.putExtra(PARAM_QUESTION_ID, id);
        activity.startActivityForResult(intent, requestId);
    }

}
