package in.voiceme.app.voiceme.services;

import java.util.List;

import in.voiceme.app.voiceme.login.LoginResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface WebService {
    @GET("posts.php")
    Observable<List<PostsModel>> getLatestFeed();

    @FormUrlEncoded
    @POST("posts.php")
    Observable<List<PostsModel>> getFollowers(@Field("follower") String user_id);

    @FormUrlEncoded
    @POST("posts.php")
    Observable<List<PostsModel>> getPopulars(@Field("popular") String booleann);

    @FormUrlEncoded
    @POST("posts.php")
    Observable<List<PostsModel>> getTrending(@Field("trending") String booleann);

    @GET("likes.php")
    Observable<LikesResponse> likes(@Query("user_id") int userId, @Query("post_id") String postId,
                                    @Query("like") int like,
                                    @Query("hug") int hug,
                                    @Query("same") int same,
                                    @Query("listen") int listen);

    @GET("unlikes.php")
    Observable<LikesResponse> unlikes(@Query("user_id") int userId, @Query("post_id") String postId,
                                    @Query("like") int like,
                                    @Query("hug") int hug,
                                    @Query("same") int same,
                                    @Query("listen") int listen);

    @FormUrlEncoded
    @POST("login.php")
    Observable<LoginResponse> login(
            @Field("name") String name,
            @Field("email") String email,
            @Field("location") String location,
            @Field("dob") String dateOfBirth,
            @Field("user_id") String userId,
            @Field("profile") String profile,
            @Field("gender") String gender
    );

}
