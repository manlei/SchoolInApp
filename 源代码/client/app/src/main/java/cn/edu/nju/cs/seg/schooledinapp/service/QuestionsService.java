package cn.edu.nju.cs.seg.schooledinapp.service;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.model.Question;
import cn.edu.nju.cs.seg.schooledinapp.model.QuestionAnswerItem;
import cn.edu.nju.cs.seg.schooledinapp.model.SearchQuestionItem;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by manlei on 2017/5/6.
 * 说明：
 */

public interface QuestionsService {

    @GET("questions/{questionId}")
    Call<Question> fetchOne(@Path("questionId") int questionId);

    @GET("questions")
    Call<List<SearchQuestionItem>> fetch(@Query("offset") int offset, @Query("limit") int limit, @Query("search") String queryText);

    @GET("questions/{questionId}/answers")
    Call<List<QuestionAnswerItem>> fetchAnswers(@Path("questionId") int questionId);

    @POST("questions/{questionId}/supports")
    Call<Void> supportOneQuestion(@Path("questionId") int questionId, @Body RequestBody body);

    @HTTP(method = "DELETE", path = "questions/{questionId}", hasBody = true)
    Call<Void> deleteQuestion(@Path("questionId") int questionId, @Body RequestBody body);
}
