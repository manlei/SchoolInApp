package cn.edu.nju.cs.seg.schooledinapp.service;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.model.Essay;
import cn.edu.nju.cs.seg.schooledinapp.model.EssayCommentItem;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioEssayItem;
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
 * 文章服务API /api/essays
 *
 */
public interface EssaysService {

    @GET("essays/{essayId}")
    Call<Essay> fetchOne(@Path("essayId") int essayId);

    @GET("essays/{essayId}/comments")
    Call<List<EssayCommentItem>> fetchComments(@Path("essayId") int essayId);

    @POST("essays/{essayId}/supports")
    Call<Void> supportOneEssay(@Path("essayId") int essayId, @Body RequestBody body);

    //发送图片
    @Multipart
    @POST("essays")
    Call<StudioEssayItem> addEssay(@Part("description") RequestBody questionBody,
                                   @Part List<MultipartBody.Part> imageList);

}
