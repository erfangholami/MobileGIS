package dev.erfangh.mobilegis.DataModel.Retrofit;


import dev.erfangh.mobilegis.DataModel.Models.Address;
import dev.erfangh.mobilegis.DataModel.Models.FeedbackResponse;
import dev.erfangh.mobilegis.DataModel.Models.LoginResponse;
import dev.erfangh.mobilegis.DataModel.Models.SearchItem;
import dev.erfangh.mobilegis.DataModel.Models.SearchResult;
import dev.erfangh.mobilegis.DataModel.Models.SectorModel;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API
{
    //API should decler here
    @POST("Login")
    @FormUrlEncoded
    Observable<LoginResponse> login
    (
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("GetPrimaryResult")
    @FormUrlEncoded
    Observable<List<SearchResult>> getSearchResult
    (
            @Header("Token") String token,
            @Field("searchTerm") String searchString
    );

    @POST("GetFinalResult")
    @FormUrlEncoded
    Observable<SearchItem> getSearchItem
    (
            @Header("Token") String token,
            @Field("id") int id
    );

    @POST("MySendUserLocations")
    Observable<String> sendUserLocations
    (
            @Header("Content-Type") String type,
            @Header("Token") String token,
            @Body String jsonObject
    );

    @Multipart
    @POST("SendFile")
    Observable<Response<Void>> uploadFile
    (
            @Header("Content-Type") String type,
            @Header("Token") String token,
            @Part MultipartBody.Part file
    );

    @POST("GetAddress")
    @FormUrlEncoded
    Observable<Address> getAddress
    (
            @Header("Token") String token,
            @Field("lat") double lat,
            @Field("long") double lng
    );


    @POST("GetSector")
    Observable<List<SectorModel>> getSector
    (
            @Header("Token") String token
    );

    @POST("SendFeedback")
    @FormUrlEncoded
    Observable<FeedbackResponse> sendFeedback
    (
            @Header("Token") String token,
            @Field("Feedback") String feedback
    );
}
