package com.codewithdeva.pagination;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainInterface {

    @GET("v2/list")
    Call<List<MainData>> stringCall(@Query("page") int page, @Query("limit") int limit);
}
