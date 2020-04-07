package com.miguel.go4lunch_p6;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallService {

    @GET("svc/mostpopular/v2/emailed/7.json")
    Call<RestaurantResponse> getRestaurant(@Query("key") String apiKey, @Query("input")String input, @Query("inputtype")String textquery, @Query("fields")String place_id, @Query("locationbias")String locationbias);

}
