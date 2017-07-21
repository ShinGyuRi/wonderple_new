package kr.co.easterbunny.wonderple.model;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by scona on 2017-01-12.
 */

public interface HttpService {


    @FormUrlEncoded
    @POST("logincheck_an.php")
    Call<JsonObject> loginCheck(@Field("name") String name,
                            @Field("type") String type,
                            @Field("idnum") String idNum);

    @FormUrlEncoded
    @POST("logincheck_email_an.php")
    Call<JsonObject>  loginCheckEmail(@Field("email") String email,
                                   @Field("type") String type,
                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("pushregister_an.php")
    Call<JsonObject> pushRegister(@Field("uid") String uid,
                              @Field("pushid") String pushId,
                              @Field("picture") String picture);

    @FormUrlEncoded
    @POST("signup_an.php")
    Call<JsonObject> signUp(@Field("name") String name,
                            @Field("type") String type,
                            @Field("picture") String picture,
                            @Field("idnum") String idNum);

    @FormUrlEncoded
    @POST("signup_email_an.php")
    Call<JsonObject> signUpEmail(@Field("email") String email,
                             @Field("name") String name,
                             @Field("password") String password,
                             @Field("type") String type);

    @FormUrlEncoded
    @POST("pwrequest_an.php")
    Call<JsonObject> pwRequest(@Field("email") String email);

    @FormUrlEncoded
    @POST("loadmain_an.php")
    Call<LoadMainResult> loadMain(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadmoremain_an.php")
    Call<LoadMainResult> loadMoreMain(@Field("uid") String uid,
                                      @Field("num") String num);

    @FormUrlEncoded
    @POST("loaddata_an.php")
    Call<LoadPostResult> loadPost(@Field("iid") String iid,
                                  @Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadcomment_an.php")
    Call<LoadCommentResult> loadComment(@Field("iid") String iid,
                                        @Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadnextcomment_an.php")
    Call<LoadCommentResult> loadNextComment(@Field("iid") String iid,
                                            @Field("uid") String uid,
                                            @Field("num") String num);

    @FormUrlEncoded
    @POST("loadtaguserlist_an.php")
    Call<LoadMentionUserListResult> loadUserList(@Field("iid") String iid,
                                                 @Field("uid") String uid);

    @FormUrlEncoded
    @POST("wonderpleapply_an.php")
    Call<JsonObject> wonderpleApply(@Field("iid") String iid,
                                    @Field("uid") String uid,
                                    @Field("auid") String auid);

    @FormUrlEncoded
    @POST("followapply_an.php")
    Call<JsonObject> followApply(@Field("uid") String uid,
                                 @Field("auid") String auid);

    @FormUrlEncoded
    @POST("callinimage_an.php")
    Call<JsonObject> callImage(@Field("iid") String iid,
                               @Field("uid") String uid,
                               @Field("reasons") String reasons,
                               @Field("thecase") String theCase);

    @FormUrlEncoded
    @POST("checkcallin_an.php")
    Call<JsonObject> checkCall(@Field("iid") String iid,
                               @Field("uid") String uid);

    @FormUrlEncoded
    @POST("commentapply_an.php")
    Call<JsonObject> commentApply(@Field("iid") String iid,
                                  @Field("uid") String uid,
                                  @Field("detail") String detail);

    @FormUrlEncoded
    @POST("commentapplyto_an.php")
    Call<JsonObject> commentApplyTo(@Field("iid") String iid,
                                    @Field("uid") String uid,
                                    @Field("cid") String cid,
                                    @Field("tagcount") String tagCount,
                                    @Field("tag[]") List<String> tag);

    @FormUrlEncoded
    @POST("checkcallincomment_an.php")
    Call<JsonObject> checkCallIn(@Field("cid") String cid,
                                 @Field("uid") String uid);

    @FormUrlEncoded
    @POST("callincomment_an.php")
    Call<JsonObject> callInComment(@Field("cid") String cid,
                                   @Field("uid") String uid,
                                   @Field("reasons") String reasons,
                                   @Field("thecase") String theCase);

    @FormUrlEncoded
    @POST("commentdelete_an.php")
    Call<JsonObject> commentDelete(@Field("cid") String cid,
                                   @Field("casenum") String caseNum);

    @FormUrlEncoded
    @POST("loadwonderple_an.php")
    Call<LoadWonderpleResult> loadWonderple(@Field("iid") String iid,
                                            @Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadmorewonderple.an.php")
    Call<LoadWonderpleResult> loadMoreWonderple(@Field("iid") String iid,
                                                @Field("uid") String uid,
                                                @Field("num") String num);

    @FormUrlEncoded
    @POST("loadwonderpeople_an.php")
    Call<LoadWonderResult> loadWonder(@Field("iid") String iid,
                                      @Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadmorewonderpeople_an.php")
    Call<LoadWonderResult> loadMoreWonder(@Field("iid") String iid,
                                          @Field("uid") String uid,
                                          @Field("num") String num);

    @FormUrlEncoded
    @POST("applywonder_an.php")
    Call<JsonObject> applyWonder(@Field("iid") String iid,
                                 @Field("uid") String uid,
                                 @Field("auid") String auid,
                                 @Field("tagcount") String tagCount,
                                 @Field("tag[]") List<String> tag);

    @FormUrlEncoded
    @POST("loadprofile_an.php")
    Call<LoadProfileResult> loadProfile(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadmorepost_an.php")
    Call<LoadProfileResult> loadMorePost(@Field("uid") String uid,
                                                    @Field("num") String num);

    @FormUrlEncoded
    @POST("loadvisitprofile_an.php")
    Call<LoadProfileResult> loadVisitProfile(@Field("uid") String uid,
                                             @Field("auid") String auid);

    @FormUrlEncoded
    @POST("deleteimage_an.php")
    Call<JsonObject> deleteImage(@Field("iid") String iid);

    @FormUrlEncoded
    @POST("pictureset_an.php")
    Call<JsonObject> setProfile(@Field("uid") String uid,
                                @Field("iid") String iid,
                                @Field("typeof") String typeOf);

    @FormUrlEncoded
    @POST("loadtagpouch_an.php")
    Call<LoadTagPouchResult> loadTagPouch(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadeventdata_an.php")
    Call<LoadEventDataResult> loadEvent(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("searchtag_an.php")
    Call<SearchTagResult> searchTag(@Field("keyword") String keyword);

    @Multipart
    @POST("upload_an.php")
    Call<JsonObject> uploadImage(@PartMap Map<String, RequestBody> largeImage);

    @FormUrlEncoded
    @POST("uploadmeta_an.php")
    Call<JsonObject> uploadData(@Field("uid") String uid,
                                @Field("iid") String iid,
                                @Field("desc") String desc,
                                @Field("tagcount") String tagCount,
                                @Field("cid[]") List<String> cid,
                                @Field("coid[]") List<String> category,
                                @Field("colorcount") String colorCount,
                                @Field("color[]") List<String> color,
                                @Field("theme") String theme);

    @FormUrlEncoded
    @POST("uploadhashtag_an.php")
    Call<JsonObject> uploadHashtag(@Field("iid") String iid,
                                   @Field("tagcount") String tagCount,
                                   @Field("tag[]") List<String> tag);

    @FormUrlEncoded
    @POST("loadnews_an.php")
    Call<LoadNewsResult> loadNews(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadmorenews_an.php")
    Call<LoadNewsResult> loadMoreNews(@Field("uid") String uid,
                                      @Field("num") String num);

    @FormUrlEncoded
    @POST("refreshcount_an.php")
    Call<JsonObject> newsRefresh(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("loadfollownews_an.php")
    Call<LoadFollowNewsResult> loadFollowNews(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("followconfirm_an.php")
    Call<JsonObject> followConfirm(@Field("uid") String uid,
                                   @Field("auid") String auid);

    @FormUrlEncoded
    @POST("followdelete_an.php")
    Call<JsonObject> followDelete(@Field("uid") String uid,
                                  @Field("auid") String auid);

    @FormUrlEncoded
    @POST("loadpouch_an.php")
    Call<LoadPouchResult> loadPouch(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("loaddiary_an.php")
    Call<LoadDiaryResult> loadDiary(@Field("uid") String uid,
                                    @Field("cid") String cid);

    @FormUrlEncoded
    @POST("loadcommentforitem_an.php")
    Call<LoadTalkResult> loadTalk(@Field("uid") String uid,
                                  @Field("cid") String cid);

    @FormUrlEncoded
    @POST("searchcosmetic_an.php")
    Call<SearchCosmeticResult> searchCosmetic(@Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("applypouch_an.php")
    Call<JsonObject> applyPouch(@Field("uid") String uid,
                                @Field("cid") String cid);
}
