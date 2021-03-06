package by.app.instagram.api;

import java.util.List;
import java.util.Map;

import by.app.instagram.model.fui.UserInfoMedia;
import by.app.instagram.model.fui.UserInfoTop;
import by.app.instagram.model.pui.PostsInfo;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface IInstagramApi {

    @POST("/api/auth")
    @FormUrlEncoded
    Observable<ResponseBody> login( @FieldMap Map<String, String> map);

    @POST("/users/info")
    @FormUrlEncoded
    Observable<ResponseBody> getUserInfo( @FieldMap Map<String, String> map);

    @POST("/users/{id}/media/info")
    @FormUrlEncoded
    Observable<ResponseBody> getUserInfoMedia(@Path("id") String user_id, @FieldMap Map<String, String> map);

    @POST("/top/{id}/likes")
    @FormUrlEncoded
    Observable<ResponseBody> getUserTopLikers(@Path("id") String user_id, @FieldMap Map<String, String> map);

    @POST("/top/{id}/comments")
    @FormUrlEncoded
    Observable<ResponseBody> getUserTopComments(@Path("id") String user_id, @FieldMap Map<String, String> map);

    @POST("/posts/{id}/")
    @FormUrlEncoded
    Observable<ResponseBody> getPostsInfo(@Path("id") String user_id, @FieldMap Map<String, String> map);

    @POST("/audience/{id}/")
    Observable<ResponseBody> getAudience(@Path("id") String user_id);

    @POST("/hashtags/{id}/")
    @FormUrlEncoded
    Observable<ResponseBody> getHashtags(@Path("id") String user_id, @FieldMap Map<String, String> map);

    @POST("/stalkers/{id}/")
    @FormUrlEncoded
    Observable<ResponseBody> getStalkers(@Path("id") String user_id, @FieldMap Map<String, String> map);

    @POST("/feed/{id}/")
    @FormUrlEncoded
    Observable<ResponseBody> getFeed(@Path("id") String user_id, @FieldMap Map<String, String> map);
}
