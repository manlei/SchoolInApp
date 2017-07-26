package cn.edu.nju.cs.seg.schooledinapp.service;


import cn.edu.nju.cs.seg.schooledinapp.model.OnlineUser;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * 主页服务API /api
 *
 */
public interface IndexService {

    @POST("login")
    Call<OnlineUser> signIn(@Body RequestBody body);

    @POST("login/confirm")
    Call<Void> signInConfirm(@Body RequestBody body);

    @POST("logout")
    Call<Void> signOut(@Body RequestBody body);

    @GET("index")
    Call<List<Map<String, Object>>> fetchIndexItems(
            @Query("offset") int offset,
            @Query("limit") int limit);

    @GET
    Call<ResponseBody> downAudioFrom(@Url String url);

}
