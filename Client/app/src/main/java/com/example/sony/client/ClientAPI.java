package com.example.sony.client;

import com.example.sony.client.models.RequestForLogin;
import com.example.sony.client.models.RequestForRegister;
import com.example.sony.client.models.RequestForUploadPaper;
import com.example.sony.client.models.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by sony on 2017/4/16.
 */

public interface ClientAPI {
    @POST("/server/checkLogin.php")
    Call<ServerResponse> login(@Body RequestForLogin reqLogin);

    @POST("/server/registerUser.php")
    Call<ServerResponse> register(@Body RequestForRegister user);

    @POST("/server/uploadPaper.php")
    Call<ServerResponse> uploadPaper(@Body RequestForUploadPaper paper);

    @GET("/server/files/{filename}")
    @Streaming
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Path("filename") String filename);


    @GET("/server/deletePaper.php")
    Call<ServerResponse> deletePaper(@Query("id") int id);

    @Multipart
    @POST("/server/upload.php")
    Call<ServerResponse> upload(
            @Part MultipartBody.Part file,
            @Part("file") RequestBody name
    );
}
