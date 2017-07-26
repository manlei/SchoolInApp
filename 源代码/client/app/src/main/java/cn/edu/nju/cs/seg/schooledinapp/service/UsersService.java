package cn.edu.nju.cs.seg.schooledinapp.service;

import java.util.List;
import java.util.Map;

import cn.edu.nju.cs.seg.schooledinapp.model.FavoriteAnswerItem;
import cn.edu.nju.cs.seg.schooledinapp.model.FavoriteEssayItem;
import cn.edu.nju.cs.seg.schooledinapp.model.FavoriteQuestionItem;
import cn.edu.nju.cs.seg.schooledinapp.model.SearchUserItem;
import cn.edu.nju.cs.seg.schooledinapp.model.User;
import cn.edu.nju.cs.seg.schooledinapp.model.UserAnswerItem;
import cn.edu.nju.cs.seg.schooledinapp.model.UserQuestionItem;
import cn.edu.nju.cs.seg.schooledinapp.model.UserStudioItem;
import okhttp3.MultipartBody;

import cn.edu.nju.cs.seg.schooledinapp.model.UserSupportItem;
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
 * 用户服务 API /api/users
 *
 */
public interface UsersService {

    @GET("users")
    Call<List<SearchUserItem>> fetch(
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("search") String queryText);

    @GET("users/{userId}")
    Call<User> fetchOne(@Path("userId") int userId);

    @GET("users/{userId}/favorites/questions")
    Call<List<FavoriteQuestionItem>> fetchFavoriteQuestions(@Path("userId") int userId);

    @GET("users/{userId}/favorites/answers")
    Call<List<FavoriteAnswerItem>> fetchFavoriteAnswers(@Path("userId") int userId);

    @GET("users/{userId}/favorites/essays")
    Call<List<FavoriteEssayItem>> fetchFavoriteEssays(@Path("userId") int userId);

    @GET("users/{userId}/studios")
    Call<List<UserStudioItem>> fetchStudios(@Path("userId") int userId);

    @GET("users/{userId}/questions")
    Call<List<UserQuestionItem>> fetchQuestions(@Path("userId") int userId);

    @GET("users/{userId}/answers")
    Call<List<UserAnswerItem>> fetchAnswers(@Path("userId") int userId);

    @GET("users/{userId}/notifications")
    Call<List<Map<String, Object>>> fetchNotifications(
        @Path("userId") int userId);

    @GET("users/{userId}/supports")
    Call<List<UserSupportItem>> fetchSupports(
            @Path("userId") int userId,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @POST("users/requestVerifyEmail")
    Call<Map<String, Long>> requestVerifyEmail(@Body RequestBody body);

    @POST("users/requestVerifyPhone")
    Call<Map<String, Long>> requestVerifyPhone(@Body RequestBody body);

    @POST("users")
    Call<Map<String, String>> createUser(@Body RequestBody body);

    @PATCH("users/{userId}")
    Call<Object> updateUser(@Path("userId") int userId, @Body RequestBody body);

    @Multipart
    @POST("questions")
    Call<Map<String, Object>> addQuestion(@Part("description") RequestBody questionBody,
                                       @Part List<MultipartBody.Part> imageList,
                                       @Part List<MultipartBody.Part> audio);

    @POST("users/{userId}/favorites/answers")
    Call<Void> collectAnAnswer(@Path("userId") int userId, @Body RequestBody body);

    @POST("users/{userId}/favorites/essays")
    Call<Void> collectAnEssay(@Path("userId") int userId, @Body RequestBody body);

    @POST("users/{userId}/favorites/questions")
    Call<Void> collectAnQuestion(@Path("userId") int userId, @Body RequestBody body);

    // @DELETE 注解不允许使用请求体
    @HTTP(method = "DELETE", path = "users/{userId}/notifications/{notificationId}", hasBody = true)
    Call<Object> setReadNotification(
            @Path("userId") int userId,
            @Path("notificationId") int notificationId,
            @Body RequestBody body);

    @HTTP(method = "DELETE", path = "users/{userId}/notifications", hasBody = true)
    Call<Object> setReadNotifications(@Path("userId") int userId, @Body RequestBody body);

    @Multipart
    @POST("users/{userId}/avatar")
    Call<Map<String, String>> uploadAvatar(@Path("userId") int userId,
                                           @Part("description") RequestBody body,
                                           @Part MultipartBody.Part avatar);

}
