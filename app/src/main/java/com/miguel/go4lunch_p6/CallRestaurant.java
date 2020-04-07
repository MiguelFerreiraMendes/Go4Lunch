package com.miguel.go4lunch_p6;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallRestaurant {

    private static final String API_KEY = "AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU" ;
    private static CallService callInformationService = null;

    public static CallService getCallInformationService(){
        if(callInformationService == null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(loggingInterceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/")
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            callInformationService = retrofit.create(CallService.class);
        }
        return  callInformationService;
    }

    public interface Callbacks {
        void onResponse(@Nullable List<JsonResponse> users);
        void onFailure();
    }

    public static void fetchRestaurant(Callbacks callbacks, String input, String locationbias){

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        Call<RestaurantResponse> call = getCallInformationService().getRestaurant(API_KEY, input, "textquery","place_id", locationbias);
        call.enqueue(new Callback<RestaurantResponse>() {

            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body().response);
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                Log.e("test", "throwable", t);
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
