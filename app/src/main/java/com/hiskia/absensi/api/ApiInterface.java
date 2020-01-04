package com.hiskia.absensi.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yogi on 4/27/17.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("add-scan")
    Call<ResponseBody> scan(
            @Field("member_id") Long member_id);
}
