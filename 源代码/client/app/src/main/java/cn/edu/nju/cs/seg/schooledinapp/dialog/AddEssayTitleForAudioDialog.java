package cn.edu.nju.cs.seg.schooledinapp.dialog;


import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.StudiosService;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEssayTitleForAudioDialog extends AddEssayTitleDialog {

    private String recordFilePath;

    public AddEssayTitleForAudioDialog(
            BaseActivity activity,
            DialogResultWithRequestCodeListener listener,
            String recordFilePath,
            String studioName) {

        super(activity, listener, null, studioName);
        this.recordFilePath = recordFilePath;

    }

    @Override
    public void requestPublishEssay() {

        //body
        RequestBody body = new JsonRequestBodyBuilder()
                .append("studio_manager_email_or_phone", AppContext.getOnlineUser().getEmail())
                .append("studio_manager_password", AppContext.getOnlineUser().getPassword())
                .append("studio",studioName)
                .append("essay_title",essayTitle)
                .append("type", "audio")
                .build();

        File originFile = new File(recordFilePath);
        RequestBody filePart = RequestBody.create(MediaType.parse("audio/*"), originFile);
        MultipartBody.Part file = MultipartBody.Part.createFormData("audio", originFile.getName(), filePart);

        StudiosService studiosService = ApiClient.getStudiosService();
        studiosService.addEssay(body, null, Arrays.asList(file)).enqueue(
                new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                switch(response.code()){
                    case 201:{
                        Toast.makeText(getContext(),"发布成功",Toast.LENGTH_LONG).show();
                        setResult(PUBLISH_SUCCEEDED);
                    }
                    break;
                    case 401:{
                        Toast.makeText(getContext(),"用户名或密码错误",Toast.LENGTH_LONG).show();
                        setResult(PUBLISH_FAILED);
                    }
                    break;
                    case 503:{
                        Toast.makeText(getContext(),"服务器忙",Toast.LENGTH_LONG).show();
                        setResult(PUBLISH_FAILED);
                    }
                    break;
                    default:{
                        Toast.makeText(getContext(),"服务器忙",Toast.LENGTH_LONG).show();
                        setResult(PUBLISH_FAILED);
                    }
                    break;
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        getActivity().getResources().getString(R.string.error_message_network),
                        Toast.LENGTH_LONG).show();
                setResult(PUBLISH_FAILED);
            }
        });

    }

}
