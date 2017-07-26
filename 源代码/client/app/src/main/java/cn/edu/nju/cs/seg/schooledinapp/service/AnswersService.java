package cn.edu.nju.cs.seg.schooledinapp.service;


import java.util.List;
import java.util.Map;
import cn.edu.nju.cs.seg.schooledinapp.model.Answer;
import cn.edu.nju.cs.seg.schooledinapp.model.AnswerCommentItem;
import cn.edu.nju.cs.seg.schooledinapp.model.UserQuestionItem;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * 评论服务API /api/comments
 *
 */
public interface AnswersService {

    @GET("answers/{answerId}")
    Call<Answer> fetchOne(@Path("answerId") int answerId);

    @GET("answers/{answerId}/comments")
    Call<List<AnswerCommentItem>> fetchComments(@Path("answerId") int answerId);

    //发送图片
    @Multipart
    @POST("answers")
    Call<Map<String, String>> addAnswer(@Part("description") RequestBody questionBody,
                                        @Part List<MultipartBody.Part> imageList,
                                        @Part List<MultipartBody.Part> audio);

}
