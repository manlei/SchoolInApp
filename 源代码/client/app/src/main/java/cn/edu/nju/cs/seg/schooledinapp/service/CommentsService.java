package cn.edu.nju.cs.seg.schooledinapp.service;


import cn.edu.nju.cs.seg.schooledinapp.model.Comment;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * 评论服务API /api/comments
 *
 */
public interface CommentsService {

    @GET("comments/{commentId}")
    Call<Comment> fetchOne(@Path("commentId") int commentId);

    @POST("comments")
    Call<Map<String, String>> addComment (@Body RequestBody body);

}
