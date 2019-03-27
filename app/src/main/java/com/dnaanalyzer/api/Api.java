package com.dnaanalyzer.api;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("/searchrsid")
    Single<ApiResponse> search(@Field("userName") String userName,
                               @Field("rsid") String rsid);
}
