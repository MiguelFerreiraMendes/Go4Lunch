package com.miguel.go4lunch_p6;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallService {

    @GET("/maps/api/place/details/json")
    Call<JsonResponse> getRestaurant(@Query("key") String apiKey, @Query("place_id")String place_id, @Query("fields") String fields);

}
