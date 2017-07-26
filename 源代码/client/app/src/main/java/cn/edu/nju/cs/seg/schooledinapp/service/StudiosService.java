package cn.edu.nju.cs.seg.schooledinapp.service;

import java.util.List;
import java.util.Map;

import cn.edu.nju.cs.seg.schooledinapp.model.SearchStudioItem;
import cn.edu.nju.cs.seg.schooledinapp.model.Studio;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioEssayItem;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioMemberItem;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioQuestionItem;
import cn.edu.nju.cs.seg.schooledinapp.model.UserQuestionItem;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 工作室服务API /api/studios
 *
 */
public interface StudiosService {

    @GET("studios")
    Call<List<SearchStudioItem>> fetch(
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("search") String queryText);

    @GET("studios/{studioId}")
    Call<Studio> fetchStudio(@Path("studioId") int studioId);

    @GET("studios/{studioId}/essays")
    Call<List<StudioEssayItem>> fetchEssays(@Path("studioId") int studioId);

    @GET("studios/{studioId}/questions")
    Call<List<StudioQuestionItem>> fetchQuestions(@Path("studioId") int studioId);

    @GET("studios/{studioId}/members")
    Call<List<StudioMemberItem>> fetchMembers(@Path("studioId") int studioId);

    @Multipart
    @POST("essays")
    Call<Map<String, Object>> addEssay(@Part("description") RequestBody questionBody,
                                       @Part List<MultipartBody.Part> imageList,
                                       @Part List<MultipartBody.Part> audio);

    @PATCH("studios/{studioId}")
    Call<Object> updateStudio(@Path("studioId") int studioId, @Body RequestBody body);

    @POST("studios/{studioId}/members")
    Call<Object> addMember(@Path("studioId") int studioId, @Body RequestBody body);

    @HTTP(method = "DELETE", path = "studios/{studioId}/members/{userId}", hasBody = true)
    Call<Void> deleteMember(@Path("studioId") int studioId, @Path("userId") int userId, @Body RequestBody body);

}
